/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.VendorOwner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amr
 */
@Stateless
public class VendorOwnerFacade extends AbstractFacade<VendorOwner> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VendorOwnerFacade() {
        super(VendorOwner.class);
    }

    public VendorOwner findByName(String cellValue) {
        List<VendorOwner> owners = em.createNativeQuery("select * from vendor_owner where owner_name like '%"+cellValue+"%'",VendorOwner.class).getResultList();
        if(owners.size()>0){
            return owners.get(0);
        }
        return null;
    }
    
}
