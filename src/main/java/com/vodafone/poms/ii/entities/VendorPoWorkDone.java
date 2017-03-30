/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.VendorPo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Amr
 */
@Entity
@Table(name = "vendor_po_work_done")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VendorPoWorkDone.findAll", query = "SELECT v FROM VendorPoWorkDone v")
    , @NamedQuery(name = "VendorPoWorkDone.findById", query = "SELECT v FROM VendorPoWorkDone v WHERE v.id = :id")
    , @NamedQuery(name = "VendorPoWorkDone.findByWorkDone", query = "SELECT v FROM VendorPoWorkDone v WHERE v.workDone = :workDone")
    , @NamedQuery(name = "VendorPoWorkDone.findByWorkValue", query = "SELECT v FROM VendorPoWorkDone v WHERE v.workValue = :workValue")
    , @NamedQuery(name = "VendorPoWorkDone.findByInsertionDate", query = "SELECT v FROM VendorPoWorkDone v WHERE v.insertionDate = :insertionDate")})
public class VendorPoWorkDone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "work_done")
    private Double workDone;
    @Column(name = "work_value")
    private BigDecimal workValue;
    @Column(name = "insertion_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertionDate;
    @JoinColumn(name = "v_po_id", referencedColumnName = "po_number")
    @ManyToOne
    private VendorPo vPoId;

    public VendorPoWorkDone() {
    }

    public VendorPoWorkDone(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWorkDone() {
        if(workDone!=null){
        DecimalFormat df=new DecimalFormat("#.00");
        return Double.valueOf(df.format(workDone));
        }return workDone;
    }

    public void setWorkDone(Double workDone) {
        this.workDone = workDone;
        if(this.workDone!=null && this.vPoId!=null)
        this.workValue = BigDecimal.valueOf(this.vPoId.getServiceValue().floatValue()*this.workDone);
    }

    public BigDecimal getWorkValue() {
        return workValue;
    }

    public void setWorkValue(BigDecimal workValue) {
        this.workValue = workValue;
        if(this.workValue!=null && this.vPoId!=null)
        this.workDone = Double.valueOf(this.workValue.floatValue()/this.vPoId.getServiceValue().floatValue());
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public VendorPo getVPoId() {
        return vPoId;
    }

    public void setVPoId(VendorPo vPoId) {
        this.vPoId = vPoId;
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
        if (!(object instanceof VendorPoWorkDone)) {
            return false;
        }
        VendorPoWorkDone other = (VendorPoWorkDone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.temp.VendorPoWorkDone[ id=" + id + " ]";
    }
    
}
