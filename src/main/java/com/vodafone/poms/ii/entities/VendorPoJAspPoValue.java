/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Amr
 */
@Entity
@Table(name = "vendor_po_j_asp_po_value")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VendorPoJAspPoValue.findAll", query = "SELECT v FROM VendorPoJAspPoValue v")
    , @NamedQuery(name = "VendorPoJAspPoValue.findByVendorPoId", query = "SELECT v FROM VendorPoJAspPoValue v WHERE v.vendorPoJAspPoValuePK.vendorPoId = :vendorPoId")
    , @NamedQuery(name = "VendorPoJAspPoValue.findByAspPoId", query = "SELECT v FROM VendorPoJAspPoValue v WHERE v.vendorPoJAspPoValuePK.aspPoId = :aspPoId")
    , @NamedQuery(name = "VendorPoJAspPoValue.findByValueToTake", query = "SELECT v FROM VendorPoJAspPoValue v WHERE v.valueToTake = :valueToTake")})
public class VendorPoJAspPoValue implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VendorPoJAspPoValuePK vendorPoJAspPoValuePK;
    @Column(name = "value_to_take")
    private BigDecimal valueToTake;
    @JoinColumn(name = "asp_po_id", referencedColumnName = "po_number", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AspPo aspPo;
    @JoinColumn(name = "vendor_po_id", referencedColumnName = "po_number", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private VendorPo vendorPo;

    public VendorPoJAspPoValue() {
    }

    public VendorPoJAspPoValue(VendorPoJAspPoValuePK vendorPoJAspPoValuePK) {
        this.vendorPoJAspPoValuePK = vendorPoJAspPoValuePK;
    }

    public VendorPoJAspPoValue(String vendorPoId, String aspPoId) {
        this.vendorPoJAspPoValuePK = new VendorPoJAspPoValuePK(vendorPoId, aspPoId);
    }

    public VendorPoJAspPoValuePK getVendorPoJAspPoValuePK() {
        return vendorPoJAspPoValuePK;
    }

    public void setVendorPoJAspPoValuePK(VendorPoJAspPoValuePK vendorPoJAspPoValuePK) {
        this.vendorPoJAspPoValuePK = vendorPoJAspPoValuePK;
    }

    public BigDecimal getValueToTake() {
        return valueToTake;
    }

    public void setValueToTake(BigDecimal valueToTake) {
        this.valueToTake = valueToTake;
    }

    public AspPo getAspPo() {
        return aspPo;
    }

    public void setAspPo(AspPo aspPo) {
        this.aspPo = aspPo;
    }

    public VendorPo getVendorPo() {
        return vendorPo;
    }

    public void setVendorPo(VendorPo vendorPo) {
        this.vendorPo = vendorPo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vendorPoJAspPoValuePK != null ? vendorPoJAspPoValuePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VendorPoJAspPoValue)) {
            return false;
        }
        VendorPoJAspPoValue other = (VendorPoJAspPoValue) object;
        if ((this.vendorPoJAspPoValuePK == null && other.vendorPoJAspPoValuePK != null) || (this.vendorPoJAspPoValuePK != null && !this.vendorPoJAspPoValuePK.equals(other.vendorPoJAspPoValuePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.VendorPoJAspPoValue[ vendorPoJAspPoValuePK=" + vendorPoJAspPoValuePK + " ]";
    }
    
}
