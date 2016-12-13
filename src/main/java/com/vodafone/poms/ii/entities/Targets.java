/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eamrnas
 */
@Entity
@Table(name = "targets")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Targets.findAll", query = "SELECT t FROM Targets t"),
    @NamedQuery(name = "Targets.findByNsT", query = "SELECT t FROM Targets t WHERE t.nsT = :nsT"),
    @NamedQuery(name = "Targets.findByCosT", query = "SELECT t FROM Targets t WHERE t.cosT = :cosT"),
    @NamedQuery(name = "Targets.findByUmT", query = "SELECT t FROM Targets t WHERE t.umT = :umT"),
    @NamedQuery(name = "Targets.findByTargetMonth", query = "SELECT t FROM Targets t WHERE t.targetMonth = :targetMonth")})
public class Targets implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ns_t", precision = 8, scale = 8)
    private Float nsT;
    @Column(name = "cos_t", precision = 8, scale = 8)
    private Float cosT;
    @Column(name = "um_t", precision = 8, scale = 8)
    private Float umT;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "target_month", nullable = false)
    private Short targetMonth;

    public Targets() {
    }

    public Targets(Short targetMonth) {
        this.targetMonth = targetMonth;
    }

    public Float getNsT() {
        return nsT;
    }

    public void setNsT(Float nsT) {
        this.nsT = nsT;
    }

    public Float getCosT() {
        return cosT;
    }

    public void setCosT(Float cosT) {
        this.cosT = cosT;
    }

    public Float getUmT() {
        return umT;
    }

    public void setUmT(Float umT) {
        this.umT = umT;
    }

    public Short getTargetMonth() {
        return targetMonth;
    }

    public void setTargetMonth(Short targetMonth) {
        this.targetMonth = targetMonth;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (targetMonth != null ? targetMonth.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Targets)) {
            return false;
        }
        Targets other = (Targets) object;
        if ((this.targetMonth == null && other.targetMonth != null) || (this.targetMonth != null && !this.targetMonth.equals(other.targetMonth))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.Targets[ targetMonth=" + targetMonth + " ]";
    }
    
}
