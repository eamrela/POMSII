/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.helpers;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Amr
 */
@Entity
public class MasterGraph implements Serializable{
    @Id
    @Column(name = "target_month")
    private Integer month;
    @Column(name = "ns_iso")
    private Float NS_ISO;
    @Column(name = "cos_iso")
    private Float COS_ISO;
    @Column(name = "um_iso")
    private Float UM_ISO;
    @Column(name = "ns_T")
    private Float NS_T;
    @Column(name = "cos_t")
    private Float COS_T;
    @Column(name = "um_t")
    private Float UM_T;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Float getNS_ISO() {
        return NS_ISO;
    }

    public void setNS_ISO(Float NS_ISO) {
        this.NS_ISO = NS_ISO;
    }

    public Float getCOS_ISO() {
        return COS_ISO;
    }

    public void setCOS_ISO(Float COS_ISO) {
        this.COS_ISO = COS_ISO;
    }

    public Float getUM_ISO() {
        return UM_ISO;
    }

    public void setUM_ISO(Float UM_ISO) {
        this.UM_ISO = UM_ISO;
    }

    public Float getNS_T() {
        return NS_T;
    }

    public void setNS_T(Float NS_T) {
        this.NS_T = NS_T;
    }

    public Float getCOS_T() {
        return COS_T;
    }

    public void setCOS_T(Float COS_T) {
        this.COS_T = COS_T;
    }

    public Float getUM_T() {
        return UM_T;
    }

    public void setUM_T(Float UM_T) {
        this.UM_T = UM_T;
    }
    
    
}
