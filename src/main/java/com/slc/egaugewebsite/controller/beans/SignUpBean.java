/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.UserController;
import com.slc.egaugewebsite.utils.UserRole;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name = "signupbean", eager = true)
@RequestScoped
public class SignUpBean {
    private String email;
    private String password;
    private String preferredCampus;

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

    public String getPreferredCampus() {
        return preferredCampus;
    }

    public void setPreferredCampus(String preferredCampus) {
        this.preferredCampus = preferredCampus;
    }
    
    public String signUpUser() {
        UserController uc = new UserController();
        try {
            boolean result = uc.createUser(email, password, preferredCampus, UserRole.defaultuser);
            if (!result) {
                return "signup";
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "index";
    }
}
