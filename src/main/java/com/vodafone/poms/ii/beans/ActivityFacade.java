/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.Users;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amr
 */
@Stateless
public class ActivityFacade extends AbstractFacade<Activity> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActivityFacade() {
        super(Activity.class);
    }

    public List<Activity> findCorrelatedItems(Users loggedInUser) {
        return em.createNativeQuery("select * " +
                                    "from activity " +
                                    "where activity_id  in (select activity_id from asp_po_j_activity) "
                + " and area in (select area_name from users_j_areas where username = '"+loggedInUser.getUsername()+"') "
                ,Activity.class).getResultList();
    }

    public List<Activity> findUncorrelatedItems(Users loggedInUser) {
        return em.createNativeQuery("select * " +
                                    "from activity " +
                                    "where activity_id not in (select activity_id from asp_po_j_activity) "
                + " and activity_id not in (select activity_id from vendor_po_j_activity) "
                + " and area in (select area_name from users_j_areas where username = '"+loggedInUser.getUsername()+"') "
                ,Activity.class).getResultList();
    }

    public List<Activity> findExportItems(Date fromDate, Date toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return em.createNativeQuery("select * from activity where activity_date between '"+sdf.format(fromDate)+"' and '"+sdf.format(toDate)+"'", Activity.class).getResultList();
    }

    public List<Activity> findUserItems(Users loggedInUser) {
        return em.createNativeQuery("select * from activity "
                + " where area in (select area_name from users_j_areas where username = '"+loggedInUser.getUsername()+"') ", Activity.class).getResultList();
    }

   
    
}
