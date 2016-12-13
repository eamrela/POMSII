/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.AspPoWorkDone;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amr
 */
@Stateless
public class AspPoWorkDoneFacade extends AbstractFacade<AspPoWorkDone> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AspPoWorkDoneFacade() {
        super(AspPoWorkDone.class);
    }

    public AspPoWorkDone merge(AspPoWorkDone selected) {
        return em.merge(selected);
    }

    public List<AspPoWorkDone> findRelatedItems(AspPo selected) {
        return em.createNativeQuery("select * from asp_po_work_done where asp_po_id='"+selected.getPoNumber()+"'", AspPoWorkDone.class).getResultList();
    }
    
}
