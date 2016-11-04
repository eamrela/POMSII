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
@Table(name = "po_types")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PoTypes.findAll", query = "SELECT p FROM PoTypes p")
    , @NamedQuery(name = "PoTypes.findByTypeName", query = "SELECT p FROM PoTypes p WHERE p.typeName = :typeName")
    , @NamedQuery(name = "PoTypes.findByDescription", query = "SELECT p FROM PoTypes p WHERE p.description = :description")})
public class PoTypes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "type_name")
    private String typeName;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poType")
    private Collection<AspPo> aspPoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poType")
    private Collection<VendorPo> vendorPoCollection;

    public PoTypes() {
    }

    public PoTypes(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<AspPo> getAspPoCollection() {
        return aspPoCollection;
    }

    public void setAspPoCollection(Collection<AspPo> aspPoCollection) {
        this.aspPoCollection = aspPoCollection;
    }

    @XmlTransient
    public Collection<VendorPo> getVendorPoCollection() {
        return vendorPoCollection;
    }

    public void setVendorPoCollection(Collection<VendorPo> vendorPoCollection) {
        this.vendorPoCollection = vendorPoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typeName != null ? typeName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PoTypes)) {
            return false;
        }
        PoTypes other = (PoTypes) object;
        if ((this.typeName == null && other.typeName != null) || (this.typeName != null && !this.typeName.equals(other.typeName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.PoTypes[ typeName=" + typeName + " ]";
    }
    
}
