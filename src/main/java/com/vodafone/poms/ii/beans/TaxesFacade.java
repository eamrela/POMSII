/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.Taxes;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amr
 */
@Stateless
public class TaxesFacade extends AbstractFacade<Taxes> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TaxesFacade() {
        super(Taxes.class);
    }

    public Taxes findCurrentTaxes() {
        return (Taxes) em.createNativeQuery("select * from taxes order by activation_date desc limit 1",Taxes.class).getSingleResult();
    }
    
}
