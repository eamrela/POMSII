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
@Table(name = "sites")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sites.findAll", query = "SELECT s FROM Sites s")
    , @NamedQuery(name = "Sites.findBySitePhysical", query = "SELECT s FROM Sites s WHERE s.sitePhysical = :sitePhysical")
    , @NamedQuery(name = "Sites.findByGfRt", query = "SELECT s FROM Sites s WHERE s.gfRt = :gfRt")})
public class Sites implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "site_physical")
    private String sitePhysical;
    @Size(max = 2147483647)
    @Column(name = "gf_rt")
    private String gfRt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "site")
    private Collection<Activity> activityCollection;

    public Sites() {
    }

    public Sites(String sitePhysical) {
        this.sitePhysical = sitePhysical;
    }

    public String getSitePhysical() {
        return sitePhysical;
    }

    public void setSitePhysical(String sitePhysical) {
        this.sitePhysical = sitePhysical;
    }

    public String getGfRt() {
        return gfRt;
    }

    public void setGfRt(String gfRt) {
        this.gfRt = gfRt;
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
        hash += (sitePhysical != null ? sitePhysical.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sites)) {
            return false;
        }
        Sites other = (Sites) object;
        if ((this.sitePhysical == null && other.sitePhysical != null) || (this.sitePhysical != null && !this.sitePhysical.equals(other.sitePhysical))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.Sites[ sitePhysical=" + sitePhysical + " ]";
    }
    
}
