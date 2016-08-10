/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.model;

import com.slc.egaugewebsite.data.entities.Users_Entity;


/**
 *
 * @author Steven Kritikos
 */
public class QueueTableRowModel {
    String userName;
    String currentStatus;
    
    public QueueTableRowModel(Users_Entity user) {
        this.userName = user.getFirstName() + "  " + user.getLastName().charAt(0) + ".";
        this.currentStatus = user.getIsActive() ? "Charging" : "Waiting";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
    
    
    
}
