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
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username")
    , @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password")
    , @NamedQuery(name = "Users.findByEnabled", query = "SELECT u FROM Users u WHERE u.enabled = :enabled")
    , @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email")
    , @NamedQuery(name = "Users.findByFirstLastName", query = "SELECT u FROM Users u WHERE u.firstLastName = :firstLastName")})
public class Users implements Serializable {

    @OneToMany(mappedBy = "creator")
    private Collection<AspPo> aspPoCollection;

    @OneToMany(mappedBy = "creator")
    private Collection<VendorPo> vendorPoCollection;
    @OneToMany(mappedBy = "creator")
    private Collection<VendorInvoice> vendorInvoiceCollection;
    @OneToMany(mappedBy = "creator")
    private Collection<VendorMd> vendorMdCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "username")
    private String username;
    @Size(max = 2147483647)
    @Column(name = "password")
    private String password;
    @Column(name = "enabled")
    private Short enabled;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 2147483647)
    @Column(name = "email")
    private String email;
    @Size(max = 2147483647)
    @Column(name = "first_last_name")
    private String firstLastName;
    @OneToMany(mappedBy = "username")
    private Collection<UsersRoles> usersRolesCollection;

    public Users() {
    }

    public Users(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Short getEnabled() {
        return enabled;
    }

    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    @XmlTransient
    public Collection<UsersRoles> getUsersRolesCollection() {
        return usersRolesCollection;
    }

    public void setUsersRolesCollection(Collection<UsersRoles> usersRolesCollection) {
        this.usersRolesCollection = usersRolesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.temp.Users[ username=" + username + " ]";
    }

    @XmlTransient
    public Collection<VendorPo> getVendorPoCollection() {
        return vendorPoCollection;
    }

    public void setVendorPoCollection(Collection<VendorPo> vendorPoCollection) {
        this.vendorPoCollection = vendorPoCollection;
    }

    @XmlTransient
    public Collection<VendorInvoice> getVendorInvoiceCollection() {
        return vendorInvoiceCollection;
    }

    public void setVendorInvoiceCollection(Collection<VendorInvoice> vendorInvoiceCollection) {
        this.vendorInvoiceCollection = vendorInvoiceCollection;
    }

    @XmlTransient
    public Collection<VendorMd> getVendorMdCollection() {
        return vendorMdCollection;
    }

    public void setVendorMdCollection(Collection<VendorMd> vendorMdCollection) {
        this.vendorMdCollection = vendorMdCollection;
    }

    @XmlTransient
    public Collection<AspPo> getAspPoCollection() {
        return aspPoCollection;
    }

    public void setAspPoCollection(Collection<AspPo> aspPoCollection) {
        this.aspPoCollection = aspPoCollection;
    }
    
}
