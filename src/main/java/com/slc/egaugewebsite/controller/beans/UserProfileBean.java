/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.UserTransactionController;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.utils.AuthenticationUtils;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name="userprofilebean")
@RequestScoped
public class UserProfileBean {
    @EJB
    private UserTransactionController usercontroller;
    @ManagedProperty("#{user}")
    private UserBean user;
    
    private String oldPassword;
    private String newPassword;
    private String preferredCampus;
    private Map<String,String> campusMap;

    public UserProfileBean() {
        this.campusMap = new HashMap<>();
        this.campusMap.put("Kingston", "Kingston");
        this.campusMap.put("Brockville", "Brockville");
        this.campusMap.put("Cornwall", "Cornwall");
    }
    
    
    
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPssword) {
        this.newPassword = newPssword;
    }

    public String getPreferredCampus() {
        return preferredCampus;
    }

    public void setPreferredCampus(String preferredCampus) {
        this.preferredCampus = preferredCampus;
    }

    public Map<String, String> getCampusMap() {
        return campusMap;
    }

    public void setCampusMap(Map<String, String> campusMap) {
        this.campusMap = campusMap;
    }

    public UserTransactionController getUsercontroller() {
        return usercontroller;
    }

    public void setUsercontroller(UserTransactionController usercontroller) {
        this.usercontroller = usercontroller;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
    
    public void updatePreferredCampus() {
        Users_Entity userEntity = this.usercontroller.getUserEntity(this.user.getUser());
        userEntity.setPreferredCampus(this.preferredCampus);
        this.usercontroller.updateUser(userEntity);
        this.user.setPreferredCampus(this.preferredCampus);
    }
    
    public void updatePassword() {
        try {
            Users_Entity userEntity = this.usercontroller.ValidateUser(this.user.getUserEmail(), oldPassword);
            byte[] password = AuthenticationUtils.hash(this.newPassword.toCharArray(), userEntity.getPasswordSalt());
            userEntity.setPassword(password);
            this.usercontroller.updateUser(userEntity);
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Failed to change password");
        }
    }
    
}
