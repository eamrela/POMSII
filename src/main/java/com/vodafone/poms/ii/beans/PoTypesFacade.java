/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.beans;

import com.vodafone.poms.ii.entities.PoTypes;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amr
 */
@Stateless
public class PoTypesFacade extends AbstractFacade<PoTypes> {

    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PoTypesFacade() {
        super(PoTypes.class);
    }
    
}
