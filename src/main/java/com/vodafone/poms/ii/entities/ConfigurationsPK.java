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
 * @author eamrela
 */
@Embeddable
public class ConfigurationsPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "conf_name")
    private String confName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "conf_enviroment")
    private String confEnviroment;

    public ConfigurationsPK() {
    }

    public ConfigurationsPK(String confName, String confEnviroment) {
        this.confName = confName;
        this.confEnviroment = confEnviroment;
    }

    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName;
    }

    public String getConfEnviroment() {
        return confEnviroment;
    }

    public void setConfEnviroment(String confEnviroment) {
        this.confEnviroment = confEnviroment;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (confName != null ? confName.hashCode() : 0);
        hash += (confEnviroment != null ? confEnviroment.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConfigurationsPK)) {
            return false;
        }
        ConfigurationsPK other = (ConfigurationsPK) object;
        if ((this.confName == null && other.confName != null) || (this.confName != null && !this.confName.equals(other.confName))) {
            return false;
        }
        if ((this.confEnviroment == null && other.confEnviroment != null) || (this.confEnviroment != null && !this.confEnviroment.equals(other.confEnviroment))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.ConfigurationsPK[ confName=" + confName + ", confEnviroment=" + confEnviroment + " ]";
    }
    
}
