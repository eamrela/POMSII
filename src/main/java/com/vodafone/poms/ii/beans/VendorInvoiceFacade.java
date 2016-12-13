/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.DomainNames;
import com.vodafone.poms.ii.entities.VendorInvoice;
import com.vodafone.poms.ii.entities.VendorMd;
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
public class VendorInvoiceFacade extends AbstractFacade<VendorInvoice> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VendorInvoiceFacade() {
        super(VendorInvoice.class);
    }

    public VendorInvoice merge(VendorInvoice selected) {
        return em.merge(selected);
    }

    public List<VendorInvoice> findByMdNumber(String editInvoiceNumber) {
        return em.createNativeQuery("select * from vendor_invoice where invoice_number='"+editInvoiceNumber+"'",VendorInvoice.class).getResultList();
    }

    public List<VendorInvoice> findDashboardItems(Date start, Date end,String domains) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return em.createNativeQuery(" select * from vendor_invoice  " +
                                    " where invoice_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"' " +
                                    " and invoice_value > 0 "
                                    + (!domains.contains("*")?"and  md_id in " +
                                    " (select id from vendor_md where vendor_po_id in  " +
                                    " (select po_number from vendor_po where domain_name in ("+domains+")))":""), VendorInvoice.class).getResultList();
    }

    public List<VendorInvoice> findRelatedItems(VendorMd selected) {
        return em.createNativeQuery("select * from vendor_invoice where md_id = "+selected.getId(), VendorInvoice.class).getResultList();
    }
    
}
