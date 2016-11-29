/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Users;
import com.vodafone.poms.ii.entities.VendorMd;
import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "vendor_invoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VendorInvoice.findAll", query = "SELECT v FROM VendorInvoice v")
    , @NamedQuery(name = "VendorInvoice.findById", query = "SELECT v FROM VendorInvoice v WHERE v.id = :id")
    , @NamedQuery(name = "VendorInvoice.findByInvoiceDeserved", query = "SELECT v FROM VendorInvoice v WHERE v.invoiceDeserved = :invoiceDeserved")
    , @NamedQuery(name = "VendorInvoice.findByInvoiceFactor", query = "SELECT v FROM VendorInvoice v WHERE v.invoiceFactor = :invoiceFactor")
    , @NamedQuery(name = "VendorInvoice.findByInvoiceValue", query = "SELECT v FROM VendorInvoice v WHERE v.invoiceValue = :invoiceValue")
    , @NamedQuery(name = "VendorInvoice.findByInvoiceDate", query = "SELECT v FROM VendorInvoice v WHERE v.invoiceDate = :invoiceDate")
    , @NamedQuery(name = "VendorInvoice.findByInvoiceNumber", query = "SELECT v FROM VendorInvoice v WHERE v.invoiceNumber = :invoiceNumber")
    , @NamedQuery(name = "VendorInvoice.findByInvoiced", query = "SELECT v FROM VendorInvoice v WHERE v.invoiced = :invoiced")
    , @NamedQuery(name = "VendorInvoice.findByRemainingInInvoice", query = "SELECT v FROM VendorInvoice v WHERE v.remainingInInvoice = :remainingInInvoice")
    , @NamedQuery(name = "VendorInvoice.findBySysDate", query = "SELECT v FROM VendorInvoice v WHERE v.sysDate = :sysDate")})
public class VendorInvoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "invoice_deserved")
    private BigInteger invoiceDeserved;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "invoice_factor")
    private Double invoiceFactor;
    @Column(name = "invoice_value")
    private BigInteger invoiceValue;
    @Column(name = "invoice_date")
    @Temporal(TemporalType.DATE)
    private Date invoiceDate;
    @Size(max = 2147483647)
    @Column(name = "invoice_number")
    private String invoiceNumber;
    @Column(name = "invoiced")
    private Boolean invoiced;
    @Column(name = "remaining_in_invoice")
    private BigInteger remainingInInvoice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sys_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sysDate;
    @JoinColumn(name = "creator", referencedColumnName = "username")
    @ManyToOne
    private Users creator;
    @JoinColumn(name = "md_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private VendorMd mdId;

    public VendorInvoice() {
    }

    public VendorInvoice(Long id) {
        this.id = id;
    }

    public VendorInvoice(Long id, Date sysDate) {
        this.id = id;
        this.sysDate = sysDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getInvoiceDeserved() {
        return invoiceDeserved;
    }

    public void setInvoiceDeserved(BigInteger invoiceDeserved) {
        this.invoiceDeserved = invoiceDeserved;
    }

    public Double getInvoiceFactor() {
        return invoiceFactor;
    }

    public void setInvoiceFactor(Double invoiceFactor) {
        this.invoiceFactor = invoiceFactor;
    }

    public BigInteger getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(BigInteger invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Boolean getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(Boolean invoiced) {
        this.invoiced = invoiced;
    }

    public BigInteger getRemainingInInvoice() {
        return remainingInInvoice;
    }

    public void setRemainingInInvoice(BigInteger remainingInInvoice) {
        this.remainingInInvoice = remainingInInvoice;
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

    public VendorMd getMdId() {
        return mdId;
    }

    public void setMdId(VendorMd mdId) {
        this.mdId = mdId;
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
        if (!(object instanceof VendorInvoice)) {
            return false;
        }
        VendorInvoice other = (VendorInvoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.temp.VendorInvoice[ id=" + id + " ]";
    }
    
}
