/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.UserTransactionController;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name = "adminusercontrolbean")
@RequestScoped
public class AdminUserControlBean {
    @EJB
    private UserTransactionController usercontroller;
    private Map<String, String> adminMap;
    private Map<String, String> userMap;
    private List<String> selectedAdmin;
    private List<String> selectedUser;
    
    public AdminUserControlBean() {
        this.adminMap = new HashMap<>();
        this.userMap = new HashMap<>();
    }

    public UserTransactionController getUsercontroller() {
        return usercontroller;
    }

    public void setUsercontroller(UserTransactionController usercontroller) {
        this.usercontroller = usercontroller;
    }

    public Map<String, String> getAdminMap() {
        return adminMap;
    }

    public void setAdminMap(Map<String, String> adminMap) {
        this.adminMap = adminMap;
    }

    public Map<String, String> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, String> userMap) {
        this.userMap = userMap;
    }

    public List<String> getSelectedAdmin() {
        return selectedAdmin;
    }

    public void setSelectedAdmin(List<String> selectedAdmin) {
        this.selectedAdmin = selectedAdmin;
    }

    public List<String> getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(List<String> selectedUser) {
        this.selectedUser = selectedUser;
    }

    
    
    
    @PostConstruct
    public void init() {
       updateControlGroups();
    }

    public void updateControlGroups() {
        this.adminMap.clear();
        for(Users_Entity userEntity : usercontroller.getAdminUsers()) {
            this.adminMap.put(userEntity.getEmail(), userEntity.getEmail());
        }
        this.userMap.clear();
        for(Users_Entity userEntity : usercontroller.getRegularUsers()) {
            this.userMap.put(userEntity.getEmail(), userEntity.getEmail());
        }
    }
    
    public void updateUserRole() {
        this.selectedAdmin.stream()
                .forEach(selectAdmin -> this.usercontroller.updateUserRole(selectAdmin));
        this.selectedUser.stream()
                .forEach(selectUser -> this.usercontroller.updateUserRole(selectUser));
        
        this.updateControlGroups();
    }
    
    public void deleteUser() {
        
    }
}
