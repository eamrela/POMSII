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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Amr
 */
@Entity
@Table(name = "po_owner")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PoOwner.findAll", query = "SELECT p FROM PoOwner p")
    , @NamedQuery(name = "PoOwner.findById", query = "SELECT p FROM PoOwner p WHERE p.id = :id")
    , @NamedQuery(name = "PoOwner.findByOwnerName", query = "SELECT p FROM PoOwner p WHERE p.ownerName = :ownerName")
    , @NamedQuery(name = "PoOwner.findByOwnerNumber", query = "SELECT p FROM PoOwner p WHERE p.ownerNumber = :ownerNumber")})
public class PoOwner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "owner_name")
    private String ownerName;
    @Size(max = 2147483647)
    @Column(name = "owner_number")
    private String ownerNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poOwner")
    private Collection<VendorPo> vendorPoCollection;

    public PoOwner() {
    }

    public PoOwner(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerNumber() {
        return ownerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
        this.ownerNumber = ownerNumber;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PoOwner)) {
            return false;
        }
        PoOwner other = (PoOwner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.temp.PoOwner[ id=" + id + " ]";
    }
    
}
