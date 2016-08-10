/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.UserTransactionController;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name = "loginbean")
@RequestScoped
public class LoginBean implements Serializable {
    @ManagedProperty("#{user}")
    private UserBean user;
    @EJB
    private UserTransactionController usercontroller;
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
    
    
    public String loginUser() {
        try {
            Users_Entity userEntity = usercontroller.ValidateUser(this.email, this.password);
            // build user session
            this.user.setPreferredCampus(userEntity.getPreferredCampus());
            this.user.setUserRole(userEntity.getRoleId().getRoleName());
            this.user.setUser(userEntity.getUserId());
            this.user.setFirstName(userEntity.getFirstName());
            this.user.setLastName(userEntity.getLastName());
            this.user.setUserEmail(userEntity.getEmail());
            
            if (userEntity.getTimeEnteredQueue() != null) {
                // check if they are in queue depending on if the time they entered queue is not null 
                this.user.setInQueue(true);
                this.user.setCharging(userEntity.getIsActive());
            } else {
                this.user.setInQueue(false);
                this.user.setCharging(false);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return "index";
        }
        return "index";
    }
    
    
    
}
