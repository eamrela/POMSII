/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "configurations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Configurations.findAll", query = "SELECT c FROM Configurations c")
    , @NamedQuery(name = "Configurations.findByConfName", query = "SELECT c FROM Configurations c WHERE c.configurationsPK.confName = :confName")
    , @NamedQuery(name = "Configurations.findByConfValue", query = "SELECT c FROM Configurations c WHERE c.confValue = :confValue")
    , @NamedQuery(name = "Configurations.findByConfEnviroment", query = "SELECT c FROM Configurations c WHERE c.configurationsPK.confEnviroment = :confEnviroment")})
public class Configurations implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ConfigurationsPK configurationsPK;
    @Size(max = 2147483647)
    @Column(name = "conf_value")
    private String confValue;

    public Configurations() {
    }

    public Configurations(ConfigurationsPK configurationsPK) {
        this.configurationsPK = configurationsPK;
    }

    public Configurations(String confName, String confEnviroment) {
        this.configurationsPK = new ConfigurationsPK(confName, confEnviroment);
    }

    public ConfigurationsPK getConfigurationsPK() {
        return configurationsPK;
    }

    public void setConfigurationsPK(ConfigurationsPK configurationsPK) {
        this.configurationsPK = configurationsPK;
    }

    public String getConfValue() {
        return confValue;
    }

    public void setConfValue(String confValue) {
        this.confValue = confValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configurationsPK != null ? configurationsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configurations)) {
            return false;
        }
        Configurations other = (Configurations) object;
        if ((this.configurationsPK == null && other.configurationsPK != null) || (this.configurationsPK != null && !this.configurationsPK.equals(other.configurationsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.Configurations[ configurationsPK=" + configurationsPK + " ]";
    }
    
}
