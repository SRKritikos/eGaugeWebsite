/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.DeviceQueueController;
import com.slc.egaugewebsite.controller.UserTransactionController;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.model.QueueTableRowModel;
import com.slc.egaugewebsite.utils.DBDeviceNames;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;


/**
 *
 * @author Steven Kritikos
 */
@RequestScoped
@ManagedBean(name="queuebean")
public class QueueBean implements Serializable{
    private String campus;
    private String extendTimeStyle;
    private String addToQueueStyle;
    private List<QueueTableRowModel> tableData;
    private Map<String,String> campusMap;
    @EJB
    private DeviceQueueController queuecontroller;
    @EJB
    private UserTransactionController usercontroller;
    @ManagedProperty("#{user}")
    private UserBean user;

    public QueueBean() {
        this.campusMap = new HashMap<>();
        this.campusMap.put("Kingston", "Kingston");
        this.campusMap.put("Brockville", "Brockville");
        this.campusMap.put("Cornwall", "Cornwall");
    }

    @PostConstruct
    public void init() {
        System.out.println(this.user.getInQueue() + "  " + this.user.getCharging() + "  " +  this.user.isFinishedCharging());
        Users_Entity userEntity = usercontroller.getUserEntity(this.user.getUser());
        if (userEntity.getTimeEnteredQueue() != null) {
            System.out.println("User in queue");
            // get the device the user is queued too by looking them up in the database
            
            this.user.setCharging(userEntity.getIsActive());
            if (userEntity.getTimeEndedCharging()!= null) { 
                this.user.setFinishedCharging(true);
            }
            String deviceName = userEntity.getDeviceId().getDeviceName();
            switch (deviceName) {
                case "Kingston_Wand1_Power":
                case "Kingston_Wand2_Power":
                case "Kingston_TotalPower":
                    System.out.println("Kingston");
                    this.campus = "Kingston";
                    break;
                case "Brockville_Power":
                    System.out.println("Brock");
                    this.campus = "Brockville";
                    break;
                case "Cornwall_Power":
                    System.out.println("Cornwall");
                    this.campus = "Cornwall";   
                    break;
            }
        } else {
            this.campus = this.user.getPreferredCampus();
            this.user.removeFromQueue();
        }
        this.updateTable();
    }
    
    public Map<String, String> getCampusMap() {
        return campusMap;
    }

    public void setCampusMap(Map<String, String> campusMap) {
        this.campusMap = campusMap;
    }
    
    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public List<QueueTableRowModel> getTableData() {
        return tableData;
    }

    public void setTableData(List<QueueTableRowModel> tableData) {
        this.tableData = tableData;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getExtendTimeStyle() {
        return extendTimeStyle;
    }

    public void setExtendTimeStyle(String extendTimeStyle) {
        this.extendTimeStyle = extendTimeStyle;
    }
    
    public void updateTable() {
        
        // Check if user can extended their time.
        if (this.user.getExtendedTimeTries() > 2) {
            this.extendTimeStyle = "disabled";
        } else {
            this.extendTimeStyle = "active";
        }
        //Check if the device is is offline.
        
        this.tableData = this.queuecontroller.getQueueByStation(this.campus).stream()
                .map(user_entity -> new QueueTableRowModel(user_entity))
                .collect(Collectors.toList());   
    }
    
    
    /**
     * Add the user to the queue for the selected campus
     */
    public void addToQueue() {
        try {
            this.queuecontroller.addToQueue(campus, user.getUser());
            this.updateTable();
            this.user.setInQueue(true);
            System.out.println(this.user.getCharging().toString() + this.user.getInQueue().toString());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /**
     * Remove user from the queue
     */
    public void removeFromQueue() {
        try {
            Users_Entity userEntity = usercontroller.getUserEntity(this.user.getUser());
            this.queuecontroller.removeUserFromQueue(userEntity);
            this.user.removeFromQueue();
            this.updateTable();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /**
     * Update the device the user is queued for to new selected queue
     */
    public void updateCampusForUser() {
        this.removeFromQueue();
        this.addToQueue();
    }
    
    /**
     * Set the row class to indicate the current user in the table
     */
    public String getRowClasses() {
        StringBuilder sb = new StringBuilder();
        for (QueueTableRowModel data : this.tableData) {
            if (data.getUserEmail().equals(this.user.getUserEmail())){
                sb.append("info,");
            } else {
                sb.append(",");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Extend period for user once the this is not me button or extend time is clicked
     */
    public void extendPeriod() {
        try {
            Users_Entity userEntity = this.usercontroller.getUserByEmail(this.user.getUserEmail());
            this.queuecontroller.extendPeriodForUser(userEntity);
            //update session with extended period
            this.user.setExtendedTimeTries(userEntity.getExtendIimeTries()+1);
            this.user.setCharging(false);
            this.user.setFinishedCharging(false);
            System.out.println("USER STUFF " + this.user.getExtendedTimeTries() + " " + user.getUserEmail());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /**
     * Function called when user removes themselves from queue.
     * @return 
     */
    public String notifyNextInqueue() {
        try {
            Users_Entity userEntity = this.usercontroller.getUserEntity(this.user.getUser());
            System.out.println("removing user " + userEntity.getEmail() + " from queue");
            this.queuecontroller.notifyNextUserInQueue(userEntity);
            this.queuecontroller.removeUserFromQueue(userEntity);
            this.user.removeFromQueue();
            return "/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return null;
        }
        
       
    }
}
