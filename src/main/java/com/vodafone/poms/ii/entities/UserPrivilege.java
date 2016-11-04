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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Amr
 */
@Entity
@Table(name = "user_privilege")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserPrivilege.findAll", query = "SELECT u FROM UserPrivilege u")
    , @NamedQuery(name = "UserPrivilege.findById", query = "SELECT u FROM UserPrivilege u WHERE u.id = :id")
    , @NamedQuery(name = "UserPrivilege.findByPrivilegeName", query = "SELECT u FROM UserPrivilege u WHERE u.privilegeName = :privilegeName")
    , @NamedQuery(name = "UserPrivilege.findByPrivilegeDescription", query = "SELECT u FROM UserPrivilege u WHERE u.privilegeDescription = :privilegeDescription")})
public class UserPrivilege implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "privilege_name")
    private String privilegeName;
    @Size(max = 2147483647)
    @Column(name = "privilege_description")
    private String privilegeDescription;
    @JoinTable(name = "roles_privileges", joinColumns = {
        @JoinColumn(name = "privilege_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "role_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<UserRole> userRoleCollection;

    public UserPrivilege() {
    }

    public UserPrivilege(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getPrivilegeDescription() {
        return privilegeDescription;
    }

    public void setPrivilegeDescription(String privilegeDescription) {
        this.privilegeDescription = privilegeDescription;
    }

    @XmlTransient
    public Collection<UserRole> getUserRoleCollection() {
        return userRoleCollection;
    }

    public void setUserRoleCollection(Collection<UserRole> userRoleCollection) {
        this.userRoleCollection = userRoleCollection;
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
        if (!(object instanceof UserPrivilege)) {
            return false;
        }
        UserPrivilege other = (UserPrivilege) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.UserPrivilege[ id=" + id + " ]";
    }
    
}
