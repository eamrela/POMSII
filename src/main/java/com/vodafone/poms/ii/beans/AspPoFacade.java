/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.AspPo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amr
 */
@Stateless
public class AspPoFacade extends AbstractFacade<AspPo> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AspPoFacade() {
        super(AspPo.class);
    }

    public List<AspPo> findPOforActivity(List<Activity> selected) {
        if(!selected.isEmpty()){
            Float totalPriceASP = 0f;
            for (int i = 0; i < selected.size(); i++) {
                totalPriceASP+=selected.get(i).getTotalPriceAsp();
            }
        return em.createNativeQuery("select * from asp_po " +
                                    "where po_type = 'Extra Work' " +
                                    "and domain_name = '"+selected.get(0).getActivityType().getDomainName()+"' " +
                                    "and asp = '"+selected.get(0).getAsp().getSubcontractorName()+"' " +
                                    "and remaining_in_po >= "+totalPriceASP,AspPo.class).getResultList();
        }
        return null;
    }
    
}
