/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.VendorInvoice;
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

    public List<VendorInvoice> findDashboardItems(Date start, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return em.createNativeQuery(" select * from vendor_invoice  " +
                                    " where invoice_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"' " +
                                    " and invoice_value > 0 ", VendorInvoice.class).getResultList();
    }
    
}
