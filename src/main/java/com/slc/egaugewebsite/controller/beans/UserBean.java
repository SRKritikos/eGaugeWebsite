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
@ManagedBean(name="user", eager = true)
public class UserBean implements Serializable{
    
    private String username;
    private String userRole;
    private String preferredCampus;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    
    
    public void logoutUser() {
        try {
            HttpSession session = SessionUtils.getSession();
            session.invalidate(); 
        } catch (Exception e) {
            System.out.println("Failed to logout");
        }
       
    }
    
}
