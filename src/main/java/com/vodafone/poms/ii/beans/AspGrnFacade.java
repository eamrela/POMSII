/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.AspGrn;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amr
 */
@Stateless
public class AspGrnFacade extends AbstractFacade<AspGrn> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AspGrnFacade() {
        super(AspGrn.class);
    }

    public AspGrn merge(AspGrn selected) {
        return em.merge(selected);
    }

    public List<AspGrn> findByGRNNumber(String editGrnNumber) {
        return em.createNativeQuery("select * from asp_grn where grn_number='"+editGrnNumber+"'",AspGrn.class).getResultList();
    }
    
}
