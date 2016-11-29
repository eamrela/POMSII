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
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "vendor_md")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VendorMd.findAll", query = "SELECT v FROM VendorMd v")
    , @NamedQuery(name = "VendorMd.findById", query = "SELECT v FROM VendorMd v WHERE v.id = :id")
    , @NamedQuery(name = "VendorMd.findByMdDeserved", query = "SELECT v FROM VendorMd v WHERE v.mdDeserved = :mdDeserved")
    , @NamedQuery(name = "VendorMd.findByMdFactor", query = "SELECT v FROM VendorMd v WHERE v.mdFactor = :mdFactor")
    , @NamedQuery(name = "VendorMd.findByMdValue", query = "SELECT v FROM VendorMd v WHERE v.mdValue = :mdValue")
    , @NamedQuery(name = "VendorMd.findByMdDate", query = "SELECT v FROM VendorMd v WHERE v.mdDate = :mdDate")
    , @NamedQuery(name = "VendorMd.findByMdNumber", query = "SELECT v FROM VendorMd v WHERE v.mdNumber = :mdNumber")
    , @NamedQuery(name = "VendorMd.findByInvoiced", query = "SELECT v FROM VendorMd v WHERE v.invoiced = :invoiced")
    , @NamedQuery(name = "VendorMd.findByRemainingInMd", query = "SELECT v FROM VendorMd v WHERE v.remainingInMd = :remainingInMd")
    , @NamedQuery(name = "VendorMd.findBySysDate", query = "SELECT v FROM VendorMd v WHERE v.sysDate = :sysDate")})
public class VendorMd implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mdId")
    private Collection<VendorInvoice> vendorInvoiceCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "md_deserved")
    private BigInteger mdDeserved;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "md_factor")
    private Double mdFactor;
    @Column(name = "md_value")
    private BigInteger mdValue;
    @Column(name = "md_date")
    @Temporal(TemporalType.DATE)
    private Date mdDate;
    @Size(max = 2147483647)
    @Column(name = "md_number")
    private String mdNumber;
    @Column(name = "invoiced")
    private Boolean invoiced;
    @Column(name = "remaining_in_md")
    private BigInteger remainingInMd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sys_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sysDate;
    @JoinColumn(name = "creator", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Users creator;
    @JoinColumn(name = "vendor_po_id", referencedColumnName = "po_number")
    @ManyToOne
    private VendorPo vendorPoId;

    public VendorMd() {
    }

    public VendorMd(Long id) {
        this.id = id;
    }

    public VendorMd(Long id, Date sysDate) {
        this.id = id;
        this.sysDate = sysDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getMdDeserved() {
        return mdDeserved;
    }

    public void setMdDeserved(BigInteger mdDeserved) {
        this.mdDeserved = mdDeserved;
    }

    public Double getMdFactor() {
        return mdFactor;
    }

    public void setMdFactor(Double mdFactor) {
        this.mdFactor = mdFactor;
        calculateValue();
        calculateRemaining();
    }

    public BigInteger getMdValue() {
        return mdValue;
    }

    public void setMdValue(BigInteger mdValue) {
        this.mdValue = mdValue;
        calculateFactor();
        calculateRemaining();
    }

    public Date getMdDate() {
        return mdDate;
    }

    public void setMdDate(Date mdDate) {
        this.mdDate = mdDate;
    }

    public String getMdNumber() {
        return mdNumber;
    }

    public void setMdNumber(String mdNumber) {
        this.mdNumber = mdNumber;
    }

    public Boolean getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(Boolean invoiced) {
        this.invoiced = invoiced;
    }

    public BigInteger getRemainingInMd() {
        return remainingInMd;
    }

    public void setRemainingInMd(BigInteger remainingInMd) {
        this.remainingInMd = remainingInMd;
    }

    public Date getSysDate() {
        return sysDate;
    }

    public void setSysDate(Date sysDate) {
        this.sysDate = sysDate;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    public VendorPo getVendorPoId() {
        return vendorPoId;
    }

    public void setVendorPoId(VendorPo vendorPoId) {
        this.vendorPoId = vendorPoId;
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
        if (!(object instanceof VendorMd)) {
            return false;
        }
        VendorMd other = (VendorMd) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.VendorMd[ id=" + id + " ]";
    }
    
     private void calculateValue() {
        if(mdFactor!=null)
        mdValue = BigInteger.valueOf(BigDecimal.valueOf(mdFactor).multiply(BigDecimal.valueOf(mdDeserved.intValue())).intValue());
    }
    
    private void calculateFactor() {
        if(mdValue!=null)
        mdFactor = Double.valueOf(mdValue.floatValue()/mdDeserved.floatValue());
    }
    
    private void calculateRemaining() {
        if(mdValue!=null)
          remainingInMd = mdDeserved.subtract(mdValue);
    }

    @XmlTransient
    public Collection<VendorInvoice> getVendorInvoiceCollection() {
        return vendorInvoiceCollection;
    }

    public void setVendorInvoiceCollection(Collection<VendorInvoice> vendorInvoiceCollection) {
        this.vendorInvoiceCollection = vendorInvoiceCollection;
    }
    
}
