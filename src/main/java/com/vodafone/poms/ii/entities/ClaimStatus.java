/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
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
@Table(name = "claim_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClaimStatus.findAll", query = "SELECT c FROM ClaimStatus c")
    , @NamedQuery(name = "ClaimStatus.findByClaimName", query = "SELECT c FROM ClaimStatus c WHERE c.claimName = :claimName")
    , @NamedQuery(name = "ClaimStatus.findByDescription", query = "SELECT c FROM ClaimStatus c WHERE c.description = :description")})
public class ClaimStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "claim_name")
    private String claimName;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "claimStatus")
    private Collection<Activity> activityCollection;

    public ClaimStatus() {
    }

    public ClaimStatus(String claimName) {
        this.claimName = claimName;
    }

    public String getClaimName() {
        return claimName;
    }

    public void setClaimName(String claimName) {
        this.claimName = claimName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        hash += (claimName != null ? claimName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClaimStatus)) {
            return false;
        }
        ClaimStatus other = (ClaimStatus) object;
        if ((this.claimName == null && other.claimName != null) || (this.claimName != null && !this.claimName.equals(other.claimName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.ClaimStatus[ claimName=" + claimName + " ]";
    }
    
}
