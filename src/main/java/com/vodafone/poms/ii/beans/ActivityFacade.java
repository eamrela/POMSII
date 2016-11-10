/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.Activity;
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

    public List<Activity> findCorrelatedItems() {
        return em.createNativeQuery("select * " +
                                    "from activity " +
                                    "where activity_id  in (select activity_id from asp_po_j_activity) "
                ,Activity.class).getResultList();
    }

    public List<Activity> findUncorrelatedItems() {
        return em.createNativeQuery("select * " +
                                    "from activity " +
                                    "where activity_id not in (select activity_id from asp_po_j_activity) "
                ,Activity.class).getResultList();
    }

   
    
}
