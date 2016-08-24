/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.DeviceQueueController;
import com.slc.egaugewebsite.controller.UserTransactionController;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;


/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name = "removefromqueuebean")
@RequestScoped
public class RemoveFromQueueBean {
    private String userId;
    @EJB
    private UserTransactionController usercontroller;
    @EJB
    private DeviceQueueController queuecontroller;
    @ManagedProperty("#{user}")
    private UserBean user;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
    
    public void removeUserFromQueue() {
        try {
            System.out.println("USERID : " + this.userId);
            Users_Entity userEntity = this.usercontroller.getUserEntity(userId);
            System.out.println("removing user " + userEntity.getEmail() + " from queue");
            this.queuecontroller.updateNextUserInQueue(userEntity);
            this.queuecontroller.removeUserFromQueue(userEntity);
            this.user.removeFromQueue();
    
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    
}
