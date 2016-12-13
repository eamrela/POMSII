/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.VendorMd;
import com.vodafone.poms.ii.entities.VendorPo;
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
public class VendorMdFacade extends AbstractFacade<VendorMd> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VendorMdFacade() {
        super(VendorMd.class);
    }

    public List<VendorMd> findByMdNumber(String editMdNumber) {
        return em.createNativeQuery("select * from vendor_md where md_number='"+editMdNumber+"'",VendorMd.class).getResultList();
    }

    public VendorMd merge(VendorMd selected) {
        return em.merge(selected);
    }

    public List<VendorMd> findDashboardItems(String domains) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return em.createNativeQuery(" select * " +
                                    " from vendor_md vmd "
                + (!domains.contains("*")?" where vendor_po_id in  " +
                                    " (select po_number from vendor_po where domain_name in ("+domains+"))":""), 
                VendorMd.class).getResultList();
    }

    public List<VendorMd> findRelatedItems(VendorPo selected) {
        return em.createNativeQuery("select * from vendor_md where vendor_po_id = '"+selected.getPoNumber()+"'",VendorMd.class).getResultList();
    }
    
}
