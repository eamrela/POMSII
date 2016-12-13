/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Amr
 */
@Entity
@Table(name = "activity_code")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivityCode.findAll", query = "SELECT a FROM ActivityCode a")
    , @NamedQuery(name = "ActivityCode.findByMaterialId", query = "SELECT a FROM ActivityCode a WHERE a.materialId = :materialId")
    , @NamedQuery(name = "ActivityCode.findByDescription", query = "SELECT a FROM ActivityCode a WHERE a.description = :description")
    , @NamedQuery(name = "ActivityCode.findByQuantityRequested", query = "SELECT a FROM ActivityCode a WHERE a.quantityRequested = :quantityRequested")
    , @NamedQuery(name = "ActivityCode.findByVendorPrice", query = "SELECT a FROM ActivityCode a WHERE a.vendorPrice = :vendorPrice")
    , @NamedQuery(name = "ActivityCode.findBySubcontractorPrice", query = "SELECT a FROM ActivityCode a WHERE a.subcontractorPrice = :subcontractorPrice")
    , @NamedQuery(name = "ActivityCode.findByUm", query = "SELECT a FROM ActivityCode a WHERE a.um = :um")
    , @NamedQuery(name = "ActivityCode.findByUmPercent", query = "SELECT a FROM ActivityCode a WHERE a.umPercent = :umPercent")})
public class ActivityCode implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "material_id")
    private String materialId;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "quantity_requested")
    private Integer quantityRequested;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "vendor_price")
    private Float vendorPrice;
    @Column(name = "subcontractor_price")
    private Float subcontractorPrice;
    @Column(name = "um")
    private Float um;
    @Column(name = "um_percent")
    private Float umPercent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activityCode")
    private Collection<Activity> activityCollection;

    public ActivityCode() {
    }

    public ActivityCode(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(Integer quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public Float getVendorPrice() {
        return vendorPrice;
    }

    public void setVendorPrice(Float vendorPrice) {
        this.vendorPrice = vendorPrice;
        if(this.vendorPrice!=null && this.subcontractorPrice!=null){
        this.um = this.vendorPrice-this.subcontractorPrice;
        this.umPercent = this.um/this.vendorPrice;
        }
    }

    public Float getSubcontractorPrice() {
        return subcontractorPrice;
    }

    public void setSubcontractorPrice(Float subcontractorPrice) {
        this.subcontractorPrice = subcontractorPrice;
        if(this.vendorPrice!=null && this.subcontractorPrice!=null){
        this.um = this.vendorPrice-this.subcontractorPrice;
        this.umPercent = this.um/this.vendorPrice;
        }
    }

    public Float getUm() {
        return um;
    }

    public void setUm(Float um) {
        this.um = um;
    }

    public Float getUmPercent() {
        return umPercent;
    }

    public void setUmPercent(Float umPercent) {
        this.umPercent = umPercent;
    }

    @XmlTransient
    public Collection<Activity> getActivityCollection() {
        return activityCollection;
    }

    public void setActivityCollection(Collection<Activity> activityCollection) {
        this.activityCollection = activityCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (materialId != null ? materialId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
       
        if (!(object instanceof ActivityCode)) {
            return false;
        }
        ActivityCode other = (ActivityCode) object;
        if ((this.materialId == null && other.materialId != null) || (this.materialId != null && !this.materialId.equals(other.materialId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.ActivityCode[ materialId=" + materialId + " ]";
    }
    
}
