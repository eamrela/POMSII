/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.VendorPo;
import com.vodafone.poms.ii.entities.VendorPoWorkDone;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amr
 */
@Stateless
public class VendorPoWorkDoneFacade extends AbstractFacade<VendorPoWorkDone> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VendorPoWorkDoneFacade() {
        super(VendorPoWorkDone.class);
    }

    public VendorPoWorkDone merge(VendorPoWorkDone selected) {
        return em.merge(selected);
    }

    public List<VendorPoWorkDone> findRelatedItems(VendorPo selected) {
        return em.createNativeQuery("select * from vendor_po_work_done where v_po_id='"+selected.getPoNumber()+"'", VendorPoWorkDone.class).getResultList();
    }
    
}
