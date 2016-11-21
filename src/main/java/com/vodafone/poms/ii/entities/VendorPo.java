/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Amr
 */
@Entity
@Table(name = "vendor_po")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VendorPo.findAll", query = "SELECT v FROM VendorPo v")
    , @NamedQuery(name = "VendorPo.findByPoNumber", query = "SELECT v FROM VendorPo v WHERE v.poNumber = :poNumber")
    , @NamedQuery(name = "VendorPo.findByPoDate", query = "SELECT v FROM VendorPo v WHERE v.poDate = :poDate")
    , @NamedQuery(name = "VendorPo.findByPoDescription", query = "SELECT v FROM VendorPo v WHERE v.poDescription = :poDescription")
    , @NamedQuery(name = "VendorPo.findByFactor", query = "SELECT v FROM VendorPo v WHERE v.factor = :factor")
    , @NamedQuery(name = "VendorPo.findByServiceValue", query = "SELECT v FROM VendorPo v WHERE v.serviceValue = :serviceValue")
    , @NamedQuery(name = "VendorPo.findByPoValue", query = "SELECT v FROM VendorPo v WHERE v.poValue = :poValue")
    , @NamedQuery(name = "VendorPo.findByPoValueTaxes", query = "SELECT v FROM VendorPo v WHERE v.poValueTaxes = :poValueTaxes")
    , @NamedQuery(name = "VendorPo.findByWorkDone", query = "SELECT v FROM VendorPo v WHERE v.workDone = :workDone")
    , @NamedQuery(name = "VendorPo.findByRemainingInPo", query = "SELECT v FROM VendorPo v WHERE v.remainingInPo = :remainingInPo")
    , @NamedQuery(name = "VendorPo.findBySysDate", query = "SELECT v FROM VendorPo v WHERE v.sysDate = :sysDate")
    , @NamedQuery(name = "VendorPo.findByTaxes", query = "SELECT v FROM VendorPo v WHERE v.taxes = :taxes")})
public class VendorPo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "po_number")
    private String poNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "po_date")
    @Temporal(TemporalType.DATE)
    private Date poDate;
    @Size(max = 2147483647)
    @Column(name = "po_description")
    private String poDescription;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "factor")
    private Double factor;
    @Column(name = "service_value")
    private BigInteger serviceValue;
    @Column(name = "po_value")
    private BigInteger poValue;
    @Column(name = "md_deserved")
    private BigInteger mdDeserved;
    @Column(name = "po_value_taxes")
    private BigInteger poValueTaxes;
    @Column(name = "work_done")
    private Double workDone= 0.0;
    @Column(name = "remaining_in_po")
    private BigInteger remainingInPo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sys_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sysDate;
    @Column(name = "taxes")
    private Double taxes;
    @ManyToMany(mappedBy = "vendorPoCollection")
    private Collection<AspPo> aspPoCollection;
    @OneToMany(mappedBy = "vendorPoId")
    private Collection<VendorMd> vendorMdCollection;
    @JoinColumn(name = "domain_name", referencedColumnName = "domain_name")
    @ManyToOne(optional = false)
    private DomainNames domainName;
    @JoinColumn(name = "po_status", referencedColumnName = "status_name")
    @ManyToOne
    private PoStatus poStatus;
    @JoinColumn(name = "po_type", referencedColumnName = "type_name")
    @ManyToOne(optional = false)
    private PoTypes poType;
    @JoinColumn(name = "creator", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Users creator;

    public VendorPo() {
    }

    public VendorPo(String poNumber) {
        this.poNumber = poNumber;
    }

    public VendorPo(String poNumber, Date poDate, Date sysDate) {
        this.poNumber = poNumber;
        this.poDate = poDate;
        this.sysDate = sysDate;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Date getPoDate() {
        return poDate;
    }

    public void setPoDate(Date poDate) {
        this.poDate = poDate;
    }

    public String getPoDescription() {
        return poDescription;
    }

    public void setPoDescription(String poDescription) {
        this.poDescription = poDescription;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public BigInteger getServiceValue() {
        return serviceValue;
    }

    public void setServiceValue(BigInteger serviceValue) {
        this.serviceValue = serviceValue;
    }

    public BigInteger getPoValue() {
        
         if(factor!=null && serviceValue!=null){
//            poValue = serviceValue.multiply(BigInteger.valueOf(factor.intValue()));
            poValue = BigDecimal.valueOf(serviceValue.floatValue()*factor.floatValue()).toBigInteger();
        }
        return poValue;
    }

    public BigInteger getMdDeserved() {
        return mdDeserved;
    }

    public void setMdDeserved(BigInteger mdDeserved) {
        this.mdDeserved = mdDeserved;
    }

    
    public void setPoValue(BigInteger poValue) {
        this.poValue = poValue;
    }

    public BigInteger getPoValueTaxes() {
         if(taxes!=null && poValue!=null){
            poValueTaxes = poValue.add(BigDecimal.valueOf(poValue.intValue()*taxes).toBigInteger());
        }
        return poValueTaxes;
    }

    public void setPoValueTaxes(BigInteger poValueTaxes) {
        this.poValueTaxes = poValueTaxes;
    }

    public Double getWorkDone() {
        return workDone;
    }

    public void setWorkDone(Double workDone) {
        this.workDone = workDone;
        calculateMdDeserved();
    }

    public BigInteger getRemainingInPo() {
        return remainingInPo;
    }

    public void setRemainingInPo(BigInteger remainingInPo) {
        this.remainingInPo = remainingInPo;
    }

    public Date getSysDate() {
        return sysDate;
    }

    public void setSysDate(Date sysDate) {
        this.sysDate = sysDate;
    }

    public Double getTaxes() {
        return taxes;
    }

    public void setTaxes(Double taxes) {
        this.taxes = taxes;
    }

    @XmlTransient
    public Collection<AspPo> getAspPoCollection() {
        return aspPoCollection;
    }

    public void setAspPoCollection(Collection<AspPo> aspPoCollection) {
        this.aspPoCollection = aspPoCollection;
    }

//    @XmlTransient
    public Collection<VendorMd> getVendorMdCollection() {
        return vendorMdCollection;
    }

    public void setVendorMdCollection(Collection<VendorMd> vendorMdCollection) {
        this.vendorMdCollection = vendorMdCollection;
    }

    public DomainNames getDomainName() {
        return domainName;
    }

    public void setDomainName(DomainNames domainName) {
        this.domainName = domainName;
    }

    public PoStatus getPoStatus() {
        return poStatus;
    }

    public void setPoStatus(PoStatus poStatus) {
        this.poStatus = poStatus;
    }

    public PoTypes getPoType() {
        return poType;
    }

    public void setPoType(PoTypes poType) {
        this.poType = poType;
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
        hash += (poNumber != null ? poNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VendorPo)) {
            return false;
        }
        VendorPo other = (VendorPo) object;
        if ((this.poNumber == null && other.poNumber != null) || (this.poNumber != null && !this.poNumber.equals(other.poNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.VendorPo[ poNumber=" + poNumber + " ]";
    }

    private void calculateMdDeserved() {
        BigInteger deserved = BigDecimal.valueOf(serviceValue.floatValue()*workDone).toBigInteger();
        Object[] grns = getVendorMdCollection().toArray();
        for (Object grn : grns) {
            deserved = deserved.subtract(((VendorMd) grn).getMdDeserved());
        }
        setMdDeserved(deserved);
        setRemainingInPo(getRemainingInPo().subtract(getMdDeserved()));
    }

    
    
}
