/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;


import com.vodafone.poms.ii.entities.VendorPoJAspPoValue;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amr
 */
@Stateless
public class VendorPoJAspPoValueFacade extends AbstractFacade<VendorPoJAspPoValue> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VendorPoJAspPoValueFacade() {
        super(VendorPoJAspPoValue.class);
    }

    public List<VendorPoJAspPoValue> findAllItems(String poNumber) {
        return em.createNativeQuery("select * from vendor_po_j_asp_po_value where asp_po_id = '"+poNumber+"'", 
                VendorPoJAspPoValue.class).getResultList();
    }

    public List<VendorPoJAspPoValue> findVendorItems(String poNumber) {
         return em.createNativeQuery("select * from vendor_po_j_asp_po_value where vendor_po_id = '"+poNumber+"'", 
                VendorPoJAspPoValue.class).getResultList();
    }

    public VendorPoJAspPoValue findById(String poNumber, String poNumber0) {
        List<VendorPoJAspPoValue> result =  em.createNativeQuery("select * from vendor_po_j_asp_po_value where vendor_po_id = '"+poNumber+"' "
                + " and asp_po_id = '"+poNumber0+"'", 
                VendorPoJAspPoValue.class).getResultList();
        if(result.size()>0){
            return result.get(0);
        }
        return null;
    }
    
}
