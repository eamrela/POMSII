/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "asp_po")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AspPo.findAll", query = "SELECT a FROM AspPo a")
    , @NamedQuery(name = "AspPo.findByPoNumber", query = "SELECT a FROM AspPo a WHERE a.poNumber = :poNumber")
    , @NamedQuery(name = "AspPo.findByPoDate", query = "SELECT a FROM AspPo a WHERE a.poDate = :poDate")
    , @NamedQuery(name = "AspPo.findByPoDescription", query = "SELECT a FROM AspPo a WHERE a.poDescription = :poDescription")
    , @NamedQuery(name = "AspPo.findByFactor", query = "SELECT a FROM AspPo a WHERE a.factor = :factor")
    , @NamedQuery(name = "AspPo.findByServiceValue", query = "SELECT a FROM AspPo a WHERE a.serviceValue = :serviceValue")
    , @NamedQuery(name = "AspPo.findByPoValue", query = "SELECT a FROM AspPo a WHERE a.poValue = :poValue")
    , @NamedQuery(name = "AspPo.findByPoValueTaxes", query = "SELECT a FROM AspPo a WHERE a.poValueTaxes = :poValueTaxes")
    , @NamedQuery(name = "AspPo.findByWorkDone", query = "SELECT a FROM AspPo a WHERE a.workDone = :workDone")
    , @NamedQuery(name = "AspPo.findByRemainingInPo", query = "SELECT a FROM AspPo a WHERE a.remainingInPo = :remainingInPo")
    , @NamedQuery(name = "AspPo.findBySysDate", query = "SELECT a FROM AspPo a WHERE a.sysDate = :sysDate")
    , @NamedQuery(name = "AspPo.findByPoMargin", query = "SELECT a FROM AspPo a WHERE a.poMargin = :poMargin")
    , @NamedQuery(name = "AspPo.findByTaxes", query = "SELECT a FROM AspPo a WHERE a.taxes = :taxes")})
public class AspPo implements Serializable {

    @OneToMany(mappedBy = "aspPoId")
    private Collection<AspPoWorkDone> aspPoWorkDoneCollection;

     @JoinColumn(name = "po_chaser", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EricssonPoChaser poChaser;
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
    @Column(name = "grn_deserved")
    private BigInteger grnDeserved;
    @Column(name = "po_value_taxes")
    private BigInteger poValueTaxes;
    @Column(name = "work_done")
    private Double workDone = 0.0;
    @Column(name = "remaining_in_po")
    private BigInteger remainingInPo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sys_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sysDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "po_margin")
    private double poMargin = 30.0;
    @Column(name = "taxes")
    private Double taxes;
    @ManyToMany(mappedBy = "aspPoCollection")
    private Collection<Activity> activityCollection;
    @JoinTable(name = "vendor_po_j_asp_po", joinColumns = {
        @JoinColumn(name = "asp_po_id", referencedColumnName = "po_number")}, inverseJoinColumns = {
        @JoinColumn(name = "vendor_po_id", referencedColumnName = "po_number")})
    @ManyToMany
    private Collection<VendorPo> vendorPoCollection;
    @JoinColumn(name = "domain_name", referencedColumnName = "domain_name")
    @ManyToOne(optional = false)
    private DomainNames domainName;
    @JoinColumn(name = "po_status", referencedColumnName = "status_name")
    @ManyToOne
    private PoStatus poStatus;
    @JoinColumn(name = "po_type", referencedColumnName = "type_name")
    @ManyToOne(optional = false)
    private PoTypes poType;
    @JoinColumn(name = "asp", referencedColumnName = "subcontractor_name")
    @ManyToOne(optional = false)
    private Subcontractors asp;
    @JoinColumn(name = "creator", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Users creator;
    @OneToMany(mappedBy = "aspPoId")
    private Collection<AspGrn> aspGrnCollection;

    public AspPo() {
    }

    public AspPo(String poNumber) {
        this.poNumber = poNumber;
    }

    public AspPo(String poNumber, Date poDate, Date sysDate, double poMargin) {
        this.poNumber = poNumber;
        this.poDate = poDate;
        this.sysDate = sysDate;
        this.poMargin = poMargin;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

     public EricssonPoChaser getPoChaser() {
        return poChaser;
    }

    public void setPoChaser(EricssonPoChaser poChaser) {
        this.poChaser = poChaser;
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

    public BigInteger getGrnDeserved() {
        
        return grnDeserved;
    }

    public void setGrnDeserved(BigInteger grnDeserved) {
        this.grnDeserved = grnDeserved;
    }
    
    public void setPoValue(BigInteger poValue) {
        this.poValue = poValue;
    }

    public BigInteger getPoValueTaxes() {
        if(taxes!=null && poValue!=null){
            poValueTaxes = poValue.add(BigDecimal.valueOf(poValue.intValue()*taxes/100).toBigInteger());
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
        calculateGRNDeserved();
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

    public double getPoMargin() {
        if(getActivityCollection()!=null){
        if(!getActivityCollection().isEmpty()){
             Object[] activities = getActivityCollection().toArray();
             Float totalASP = 0f;
             Float totalVendor = 0f;
            for (Object activitie : activities) {
                totalASP += ((Activity) activitie).getTotalPriceAsp();
                totalVendor += ((Activity) activitie).getTotalPriceVendor();
            }
             
             setPoMargin((1-(totalASP/totalVendor))*100.0);
        }
        }
        return poMargin;
    }

    public void setPoMargin(double poMargin) {
        this.poMargin = poMargin;
    }

    public Double getTaxes() {
        return taxes;
    }

    public void setTaxes(Double taxes) {
        this.taxes = taxes;
    }

    @XmlTransient
    public Collection<Activity> getActivityCollection() {
        return activityCollection;
    }

    public void setActivityCollection(Collection<Activity> activityCollection) {
        this.activityCollection = activityCollection;
    }

    @XmlTransient
    public Collection<VendorPo> getVendorPoCollection() {
        return vendorPoCollection;
    }

    public void setVendorPoCollection(Collection<VendorPo> vendorPoCollection) {
        this.vendorPoCollection = vendorPoCollection;
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

    public Subcontractors getAsp() {
        return asp;
    }

    public void setAsp(Subcontractors asp) {
        this.asp = asp;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    public void calculateGRNDeserved(){
        BigInteger deserved = BigDecimal.valueOf((serviceValue.floatValue()*workDone)).toBigInteger();
        BigInteger deservedI = BigDecimal.valueOf((serviceValue.floatValue()*workDone)).toBigInteger();
        Object[] grns = getAspGrnCollection().toArray();
        for (Object grn : grns) {
            deserved = deserved.subtract(((AspGrn) grn).getGrnDeserved());
        }

        setRemainingInPo(getPoValue().subtract(deservedI));
    }
    
//    @XmlTransient
    public Collection<AspGrn> getAspGrnCollection() {
        return aspGrnCollection;
    }

    public void setAspGrnCollection(Collection<AspGrn> aspGrnCollection) {
        this.aspGrnCollection = aspGrnCollection;
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
        if (!(object instanceof AspPo)) {
            return false;
        }
        AspPo other = (AspPo) object;
        if ((this.poNumber == null && other.poNumber != null) || (this.poNumber != null && !this.poNumber.equals(other.poNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.AspPo[ poNumber=" + poNumber + " ]";
    }

    @XmlTransient
    public Collection<AspPoWorkDone> getAspPoWorkDoneCollection() {
        return aspPoWorkDoneCollection;
    }

    public void setAspPoWorkDoneCollection(Collection<AspPoWorkDone> aspPoWorkDoneCollection) {
        this.aspPoWorkDoneCollection = aspPoWorkDoneCollection;
    }
    
}
