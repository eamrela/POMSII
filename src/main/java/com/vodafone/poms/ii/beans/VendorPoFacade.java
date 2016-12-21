/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.VendorPo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.joda.time.DateTime;

/**
 *
 * @author Amr
 */
@Stateless
public class VendorPoFacade extends AbstractFacade<VendorPo> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VendorPoFacade() {
        super(VendorPo.class);
    }

    public List<VendorPo> findPOforASPExtraWork(List<AspPo> selected) {
        if(selected!=null){
            if(!selected.isEmpty())
            {
                String poType = selected.get(0).getPoType().getTypeName();
                String domain = selected.get(0).getDomainName().getDomainName();
                BigInteger totalPOASPPrice = BigInteger.ZERO;
                    for (int i = 0; i < selected.size(); i++) {
                        totalPOASPPrice = 
                                totalPOASPPrice.add(
                                        BigDecimal.valueOf(
                                                selected.get(i).getPoValue().doubleValue()*(1+(selected.get(i).getPoMargin()/100.0))
                                        ).toBigInteger());
                    }
            return em.createNativeQuery("select * from vendor_po " +
                                    "where po_status != 'COMPLETED' and po_type = '"+poType+"' " +
                                    "and domain_name = '"+domain+"' " +
                                    "and remaining_in_po >= "+ totalPOASPPrice+"",VendorPo.class).getResultList();
            }
        }
        return null;
        
    }
    
    public List<VendorPo> findPOforASPService(List<AspPo> selected) {
        if(selected!=null){
            if(!selected.isEmpty())
            {
                String poType = selected.get(0).getPoType().getTypeName();
                String domain = selected.get(0).getDomainName().getDomainName();
                BigInteger totalPOASPPrice = BigInteger.ZERO;
                int MonthNumber = new DateTime(selected.get(0).getPoDate()).getMonthOfYear();
                    for (int i = 0; i < selected.size(); i++) {
                        totalPOASPPrice = 
                                totalPOASPPrice.add(selected.get(i).getPoValue());
                    }
            return em.createNativeQuery("select * from vendor_po " +
                                    "where po_status != 'COMPLETED' and po_type = '"+poType+"' " +
                                    "and domain_name = '"+domain+"' " +
                                    "and remaining_in_po >= "+ totalPOASPPrice+" "
                                            + "and extract(month from po_date) = "+MonthNumber,VendorPo.class).getResultList();
            }
        }
        return null;
        
    }

    public List<VendorPo> findExportItems(Date fromDate, Date toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return em.createNativeQuery("select * from vendor_po where po_status != 'COMPLETED' and po_date between '"+sdf.format(fromDate)+"' and '"+sdf.format(toDate)+"' ", 
                VendorPo.class).getResultList();
    }

    public List<VendorPo> findRemainingNotYetinvoiced(Date start, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return em.createNativeQuery(" select * " +
                                    "from vendor_po " +
                                    "where po_status != 'COMPLETED' and po_date between '"+sdf.format(start)+"' and  '"+sdf.format(end)+"'  " +
                                    "and md_deserved > 0 ", 
                VendorPo.class).getResultList();
    }

    public List<VendorPo> findMdNotYetGenerated(Date start, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return em.createNativeQuery(" select * " +
                                    "from vendor_po " +
                                    "where po_status != 'COMPLETED' "
                + " and po_date between '"+sdf.format(start)+"' and  '"+sdf.format(end)+"'  " +
                                    "and md_deserved > 0 ", 
                VendorPo.class).getResultList();
    }

    public List<VendorPo> findPOforActivity(List<Activity> selected) {
        if(!selected.isEmpty()){
            Float totalPrice = 0f;
            for (int i = 0; i < selected.size(); i++) {
                totalPrice+=selected.get(i).getTotalPriceVendor();
            }
        return em.createNativeQuery("select * from vendor_po " +
                                    "where po_status != 'COMPLETED'"
                + " and po_type = 'Extra Work' " +
                                    "and domain_name = '"+selected.get(0).getActivityType().getDomainName()+"' " +
                                    "and remaining_in_po >= "+totalPrice,VendorPo.class).getResultList();
        }
        return null;
    }

    public List<VendorPo> findAllOpen() {
        return em.createNativeQuery("select * from vendor_po where po_status != 'COMPLETED'", VendorPo.class).getResultList();
    }
    
}
