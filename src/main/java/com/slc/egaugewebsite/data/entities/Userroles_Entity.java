/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.data.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Steven Kritikos
 * email: stevenrktitikos@outlook.com
 */
@Entity
@Table(name = "userroles")
@NamedQueries({
    @NamedQuery(name = "Userroles_Entity.findAll", query = "SELECT u FROM Userroles_Entity u"),
    @NamedQuery(name = "Userroles_Entity.findByRoleId", query = "SELECT u FROM Userroles_Entity u WHERE u.roleId = :roleId"),
    @NamedQuery(name = "Userroles_Entity.findByRoleName", query = "SELECT u FROM Userroles_Entity u WHERE u.roleName = :roleName")})
public class Userroles_Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "roleId")
    private Integer roleId;
    @Size(max = 12)
    @Column(name = "roleName")
    private String roleName;
    @OneToMany(mappedBy = "roleId")
    private List<Users_Entity> usersEntityList;

    public Userroles_Entity() {
    }

    public Userroles_Entity(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Users_Entity> getUsersEntityList() {
        return usersEntityList;
    }

    public void setUsersEntityList(List<Users_Entity> usersEntityList) {
        this.usersEntityList = usersEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleId != null ? roleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Userroles_Entity)) {
            return false;
        }
        Userroles_Entity other = (Userroles_Entity) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.slc.egaugewebsite.data.entities.Userroles_Entity[ roleId=" + roleId + " ]";
    }

}
