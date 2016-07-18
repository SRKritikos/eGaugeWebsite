/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.data.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Steven Kritikos
 * email: stevenrktitikos@outlook.com
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "Users_Entity.findAll", query = "SELECT u FROM Users_Entity u"),
    @NamedQuery(name = "Users_Entity.findByUserId", query = "SELECT u FROM Users_Entity u WHERE u.userId = :userId"),
    @NamedQuery(name = "Users_Entity.findByEmail", query = "SELECT u FROM Users_Entity u WHERE u.email = :email"),
    @NamedQuery(name = "Users_Entity.findByPreferredCampus", query = "SELECT u FROM Users_Entity u WHERE u.preferredCampus = :preferredCampus")})
public class Users_Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "userId")
    private String userId;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
    @Size(max = 10)
    @Column(name = "preferredCampus")
    private String preferredCampus;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "password")
    private byte[] password;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "passwordSalt")
    private byte[] passwordSalt;
    @JoinColumn(name = "roleId", referencedColumnName = "roleId")
    @ManyToOne
    private Userroles_Entity roleId;

    public Users_Entity() {
    }

    public Users_Entity(String userId) {
        this.userId = userId;
    }

    public Users_Entity(String userId, String email, byte[] password, byte[] passwordSalt) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.passwordSalt = passwordSalt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferredCampus() {
        return preferredCampus;
    }

    public void setPreferredCampus(String preferredCampus) {
        this.preferredCampus = preferredCampus;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public Userroles_Entity getRoleId() {
        return roleId;
    }

    public void setRoleId(Userroles_Entity roleId) {
        this.roleId = roleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users_Entity)) {
            return false;
        }
        Users_Entity other = (Users_Entity) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.slc.egaugewebsite.data.entities.Users_Entity[ userId=" + userId + " ]";
    }

}
