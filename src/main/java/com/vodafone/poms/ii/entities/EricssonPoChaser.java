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
@Table(name = "ericsson_po_chaser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EricssonPoChaser.findAll", query = "SELECT e FROM EricssonPoChaser e")
    , @NamedQuery(name = "EricssonPoChaser.findById", query = "SELECT e FROM EricssonPoChaser e WHERE e.id = :id")
    , @NamedQuery(name = "EricssonPoChaser.findByChaserName", query = "SELECT e FROM EricssonPoChaser e WHERE e.chaserName = :chaserName")
    , @NamedQuery(name = "EricssonPoChaser.findByChaserNumber", query = "SELECT e FROM EricssonPoChaser e WHERE e.chaserNumber = :chaserNumber")})
public class EricssonPoChaser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "chaser_name")
    private String chaserName;
    @Size(max = 2147483647)
    @Column(name = "chaser_number")
    private String chaserNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poChaser")
    private Collection<VendorPo> vendorPoCollection;

    public EricssonPoChaser() {
    }

    public EricssonPoChaser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChaserName() {
        return chaserName;
    }

    public void setChaserName(String chaserName) {
        this.chaserName = chaserName;
    }

    public String getChaserNumber() {
        return chaserNumber;
    }

    public void setChaserNumber(String chaserNumber) {
        this.chaserNumber = chaserNumber;
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
        if (!(object instanceof EricssonPoChaser)) {
            return false;
        }
        EricssonPoChaser other = (EricssonPoChaser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.temp.EricssonPoChaser[ id=" + id + " ]";
    }
    
}
