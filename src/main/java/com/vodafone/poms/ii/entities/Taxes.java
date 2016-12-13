/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Amr
 */
@Entity
@Table(name = "taxes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Taxes.findAll", query = "SELECT t FROM Taxes t")
    , @NamedQuery(name = "Taxes.findById", query = "SELECT t FROM Taxes t WHERE t.id = :id")
    , @NamedQuery(name = "Taxes.findByTaxValue", query = "SELECT t FROM Taxes t WHERE t.taxValue = :taxValue")
    , @NamedQuery(name = "Taxes.findByActivationDate", query = "SELECT t FROM Taxes t WHERE t.activationDate = :activationDate")})
public class Taxes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "tax_value")
    private Double taxValue;
    @Column(name = "activation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activationDate;
    
    @Transient
    private SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/YYYY");
    @Transient
    String activationDateString;

    public String getActivationDateString() {
        return activationDateString=sdf.format(activationDate);
    }

    public void setActivationDateString(String activationDateString) {
        this.activationDateString = activationDateString;
    }
    public Taxes() {
    }

    public Taxes(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(Double taxValue) {
        this.taxValue = taxValue;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Taxes)) {
            return false;
        }
        Taxes other = (Taxes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.Taxes[ id=" + id + " ]";
    }
    
}
