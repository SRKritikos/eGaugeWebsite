/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.utils.SessionUtils;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Steven Kritikos
 */
@SessionScoped
@ManagedBean(name="user")
public class UserBean implements Serializable{
    
    private String user;
    private String userRole;
    private String preferredCampus;
    private Boolean inQueue;
    private Boolean charging;
    private String firstName;
    private String lastName;
    private String userEmail;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getPreferredCampus() {
        return preferredCampus;
    }

    public void setPreferredCampus(String preferredCampus) {
        this.preferredCampus = preferredCampus;
    }

    public Boolean getCharging() {
        return charging;
    }

    public void setCharging(Boolean charging) {
        this.charging = charging;
    }

    public Boolean getInQueue() {
        return inQueue;
    }

    public void setInQueue(Boolean inQueue) {
        this.inQueue = inQueue;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    
    public String logoutUser() {
        try {
            HttpSession session = SessionUtils.getSession();
            session.invalidate(); 
        } catch (Exception e) {
            System.out.println("Failed to logout");
        }
       return "/index.xhtml?faces-redirect=true";
    }
    
}
