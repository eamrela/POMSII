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

    public List<AspPo> findPOforActivity(Activity selected) {
        return em.createNativeQuery("select * from asp_po " +
                                    "where po_type = 'Extra Work' " +
                                    "and domain_name = '"+selected.getActivityType().getDomainName()+"' " +
                                    "and asp = '"+selected.getAsp().getSubcontractorName()+"' " +
                                    "and remaining_in_po >= "+selected.getTotalPriceAsp(),AspPo.class).getResultList();
    }
    
}
