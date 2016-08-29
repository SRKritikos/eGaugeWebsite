/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.model;

import com.slc.egaugewebsite.data.entities.Users_Entity;


/**
 * Class used to represent a row of user information
 * for the front end table representing the queue of a station.
 * 
 * @author Steven Kritikos
 */
public class QueueTableRowModel {
    private String userName;
    private String statusColor;
    private String currentStatus;
    
    public QueueTableRowModel(Users_Entity user) {
        // check if user is charging
        if (user.getIsActive()) {
            this.currentStatus = "Charging";
        // check if user is done charging but still in queue 
        } else if (user.getTimeEndedCharging() != null ) {
            this.currentStatus = "Finished Charging";
        // check if user is has a start time but is not active
        } else if (user.getAvailableStartTime() != null && !user.getIsActive()) {
            this.currentStatus = "Up Next";
        } else {
            this.currentStatus = "Waiting";
        }
        // See if the user is anonymous
        if (user.getExtendIimeTries() < 0 && user.getFirstName().equals("Anonymous")) {
            this.userName = "Anonymous User";
        } else {
            this.userName = user.getFirstName() + "  " + user.getLastName().charAt(0) + ".";
        }
        // TODO SET COLOR
        this.statusColor = user.getEmail();
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

    public String getUserEmail() {
        return statusColor;
    }

    public void setUserEmail(String userEmail) {
        this.statusColor = userEmail;
    }
    
}
