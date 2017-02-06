/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Amr
 */
@Embeddable
public class VendorPoJAspPoValuePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "vendor_po_id")
    private String vendorPoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "asp_po_id")
    private String aspPoId;

    public VendorPoJAspPoValuePK() {
    }

    public VendorPoJAspPoValuePK(String vendorPoId, String aspPoId) {
        this.vendorPoId = vendorPoId;
        this.aspPoId = aspPoId;
    }

    public String getVendorPoId() {
        return vendorPoId;
    }

    public void setVendorPoId(String vendorPoId) {
        this.vendorPoId = vendorPoId;
    }

    public String getAspPoId() {
        return aspPoId;
    }

    public void setAspPoId(String aspPoId) {
        this.aspPoId = aspPoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vendorPoId != null ? vendorPoId.hashCode() : 0);
        hash += (aspPoId != null ? aspPoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VendorPoJAspPoValuePK)) {
            return false;
        }
        VendorPoJAspPoValuePK other = (VendorPoJAspPoValuePK) object;
        if ((this.vendorPoId == null && other.vendorPoId != null) || (this.vendorPoId != null && !this.vendorPoId.equals(other.vendorPoId))) {
            return false;
        }
        if ((this.aspPoId == null && other.aspPoId != null) || (this.aspPoId != null && !this.aspPoId.equals(other.aspPoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.VendorPoJAspPoValuePK[ vendorPoId=" + vendorPoId + ", aspPoId=" + aspPoId + " ]";
    }
    
}
