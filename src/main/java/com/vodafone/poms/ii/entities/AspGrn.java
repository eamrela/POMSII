/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Amr
 */
@Entity
@Table(name = "asp_grn")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AspGrn.findAll", query = "SELECT a FROM AspGrn a")
    , @NamedQuery(name = "AspGrn.findById", query = "SELECT a FROM AspGrn a WHERE a.id = :id")
    , @NamedQuery(name = "AspGrn.findByGrnDeserved", query = "SELECT a FROM AspGrn a WHERE a.grnDeserved = :grnDeserved")
    , @NamedQuery(name = "AspGrn.findByGrnFactor", query = "SELECT a FROM AspGrn a WHERE a.grnFactor = :grnFactor")
    , @NamedQuery(name = "AspGrn.findByGrnValue", query = "SELECT a FROM AspGrn a WHERE a.grnValue = :grnValue")
    , @NamedQuery(name = "AspGrn.findByGrnDate", query = "SELECT a FROM AspGrn a WHERE a.grnDate = :grnDate")
    , @NamedQuery(name = "AspGrn.findByGrnNumber", query = "SELECT a FROM AspGrn a WHERE a.grnNumber = :grnNumber")
    , @NamedQuery(name = "AspGrn.findByInvoiced", query = "SELECT a FROM AspGrn a WHERE a.invoiced = :invoiced")
    , @NamedQuery(name = "AspGrn.findByRemainingInGrn", query = "SELECT a FROM AspGrn a WHERE a.remainingInGrn = :remainingInGrn")
    , @NamedQuery(name = "AspGrn.findBySysDate", query = "SELECT a FROM AspGrn a WHERE a.sysDate = :sysDate")})
public class AspGrn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "grn_deserved")
    private BigDecimal grnDeserved;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "grn_factor")
    private Double grnFactor;
    @Column(name = "grn_value")
    private BigDecimal grnValue;
    @Column(name = "grn_date")
    @Temporal(TemporalType.DATE)
    private Date grnDate;
    @Size(max = 2147483647)
    @Column(name = "grn_number")
    private String grnNumber;
    @Column(name = "invoiced")
    private Boolean invoiced;
    @Column(name = "remaining_in_grn")
    private BigDecimal remainingInGrn;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sys_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sysDate;
    @JoinColumn(name = "asp_po_id", referencedColumnName = "po_number")
    @ManyToOne
    private AspPo aspPoId;
    @JoinColumn(name = "creator", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Users creator;

    public AspGrn() {
    }

    public AspGrn(Long id) {
        this.id = id;
    }

    public AspGrn(Long id, Date sysDate) {
        this.id = id;
        this.sysDate = sysDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getGrnDeserved() {
        return grnDeserved;
    }

    public void setGrnDeserved(BigDecimal grnDeserved) {
        this.grnDeserved = grnDeserved;
    }

    public Double getGrnFactor() {
        return grnFactor;
    }

    public void setGrnFactor(Double grnFactor) {
        this.grnFactor = grnFactor;
        calculateValue();
        calculateRemaining();
    }

    public BigDecimal getGrnValue() {
        return grnValue;
    }

    public void setGrnValue(BigDecimal grnValue) {
        this.grnValue = grnValue;
        calculateFactor();
        calculateRemaining();
    }

    public Date getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(Date grnDate) {
        this.grnDate = grnDate;
    }

    public String getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
    }

    public Boolean getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(Boolean invoiced) {
        this.invoiced = invoiced;
    }

    public BigDecimal getRemainingInGrn() {
        return remainingInGrn;
    }

    public void setRemainingInGrn(BigDecimal remainingInGrn) {
        this.remainingInGrn = remainingInGrn;
    }

    public Date getSysDate() {
        return sysDate;
    }

    public void setSysDate(Date sysDate) {
        this.sysDate = sysDate;
    }

    public AspPo getAspPoId() {
        return aspPoId;
    }

    public void setAspPoId(AspPo aspPoId) {
        this.aspPoId = aspPoId;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
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
        if (!(object instanceof AspGrn)) {
            return false;
        }
        AspGrn other = (AspGrn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.AspGrn[ id=" + id + " ]";
    }

    private void calculateValue() {
        if(grnFactor!=null)
        grnValue = BigDecimal.valueOf(BigDecimal.valueOf(grnFactor).multiply(BigDecimal.valueOf(grnDeserved.intValue())).intValue());
    }
    
    private void calculateFactor() {
        if(grnValue!=null)
        grnFactor = Double.valueOf(grnValue.floatValue()/grnDeserved.floatValue());
    }
    
    private void calculateRemaining() {
        if(grnValue!=null)
        remainingInGrn = grnDeserved.subtract(grnValue);
    }
    
}
