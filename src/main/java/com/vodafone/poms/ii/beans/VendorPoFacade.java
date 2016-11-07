/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.VendorPo;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    public List<VendorPo> findPOforASP(AspPo selected) {
        return em.createNativeQuery("select * from vendor_po " +
                                    "where po_type = '"+selected.getPoType().getTypeName()+"' " +
                                    "and domain_name = '"+selected.getDomainName().getDomainName()+"' " +
                                    "and remaining_in_po >= "+ 
                                        BigInteger.valueOf((((Float)(selected.getPoValue().intValue()
                                                +selected.getPoValue().intValue()*(Float.valueOf(String.valueOf((selected.getPoMargin()/100))))))
                                                .intValue()))+
                                    "and (factor-work_done) >=  "+selected.getFactor(),VendorPo.class).getResultList();
    }
    
}
