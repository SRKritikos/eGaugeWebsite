/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.data.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Steven Kritikos
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "Users_Entity.findAll", query = "SELECT u FROM Users_Entity u"),
    @NamedQuery(name = "Users_Entity.findByUserId", query = "SELECT u FROM Users_Entity u WHERE u.userId = :userId"),
    @NamedQuery(name = "Users_Entity.findByEmail", query = "SELECT u FROM Users_Entity u WHERE u.email = :email"),
    @NamedQuery(name = "Users_Entity.findByPreferredCampus", query = "SELECT u FROM Users_Entity u WHERE u.preferredCampus = :preferredCampus"),
    @NamedQuery(name = "Users_Entity.findByTimeEnteredQueue", query = "SELECT u FROM Users_Entity u WHERE u.timeEnteredQueue = :timeEnteredQueue"),
    @NamedQuery(name = "Users_Entity.findByAvailaleStartTime", query = "SELECT u FROM Users_Entity u WHERE u.availableStartTime = :availableStartTime"),
    @NamedQuery(name = "Users_Entity.findByAvailableEndTime", query = "SELECT u FROM Users_Entity u WHERE u.availableEndTime = :availableEndTime"),
    @NamedQuery(name = "Users_Entity.findByIsActive", query = "SELECT u FROM Users_Entity u WHERE u.isActive = :isActive"),
    @NamedQuery(name = "Users_Entity.findByExtendIimeTries", query = "SELECT u FROM Users_Entity u WHERE u.extendTimeTries = :extendTimeTries"),
    @NamedQuery(name = "Users_Entity.getQueue", query = "SELECT u FROM Users_Entity u WHERE u.deviceId = :device ORDER BY u.timeEnteredQueue ASC")})
public class Users_Entity implements Serializable {

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
    @Column(name = "timeEndedCharging")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeEndedCharging;
    @Column(name = "availableStartTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date availableStartTime;
    @Column(name = "timeStartedCharging")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStartedCharging;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "firstName")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "lastName")
    private String lastName;

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
    @Column(name = "timeEnteredQueue")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeEnteredQueue;
    @Column(name = "availableEndTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date availableEndTime;
    @Column(name = "isActive")
    private Boolean isActive;
    @Column(name = "extendTimeTries")
    private Integer extendTimeTries;
    @JoinColumn(name = "roleId", referencedColumnName = "roleId")
    @ManyToOne
    private Userroles_Entity roleId;
    @JoinColumn(name = "deviceId", referencedColumnName = "deviceId")
    @ManyToOne
    private Device_Entity deviceId;

    public Users_Entity() {
    }

    public Users_Entity(String userId) {
        this.userId = userId;
    }

    public Users_Entity(String userId, String email, byte[] password, byte[] passwordSalt, Date timeEnteredQueue) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.passwordSalt = passwordSalt;
        this.timeEnteredQueue = timeEnteredQueue;
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


    public Date getTimeEnteredQueue() {
        return timeEnteredQueue;
    }

    public void setTimeEnteredQueue(Date timeEnteredQueue) {
        this.timeEnteredQueue = timeEnteredQueue;
    }

    public Date getAvailableEndTime() {
        return availableEndTime;
    }

    public void setAvailableEndTime(Date availableEndTime) {
        this.availableEndTime = availableEndTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getExtendTimeTries() {
        return extendTimeTries;
    }

    public void setExtendTimeTries(Integer extendTimeTries) {
        this.extendTimeTries = extendTimeTries;
    }

    public Userroles_Entity getRoleId() {
        return roleId;
    }

    public void setRoleId(Userroles_Entity roleId) {
        this.roleId = roleId;
    }

    public Device_Entity getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Device_Entity deviceId) {
        this.deviceId = deviceId;
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
        return "com.slc.egaugewebsite.data.entities.Users[ userId=" + userId + " ]";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public Date getTimeStartedCharging() {
        return timeStartedCharging;
    }

    public void setTimeStartedCharging(Date timeStartedCharging) {
        this.timeStartedCharging = timeStartedCharging;
    }


    public Date getAvailableStartTime() {
        return availableStartTime;
    }

    public void setAvailableStartTime(Date availableStartTime) {
        this.availableStartTime = availableStartTime;
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

    public Date getTimeEndedCharging() {
        return timeEndedCharging;
    }

    public void setTimeEndedCharging(Date timeEndedCharging) {
        this.timeEndedCharging = timeEndedCharging;
    }
}
