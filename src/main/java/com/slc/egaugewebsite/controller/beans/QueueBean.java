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
        System.out.println(this.user.getInQueue() + " " + this.user.getCharging());
        if (this.user.getInQueue()) {
            // get the device the user is queued too by looking them up in the database then get
            String deviceName = usercontroller.getUserEntity(this.user.getUser()).getDeviceId().getDeviceName();
            System.out.println(deviceName);
            switch (deviceName) {
                case "Kingston1_Power":
                case "Kingsotn2_Power":
                    System.out.println("Kingston");
                    this.campus = "Kingston";
                    break;
                case "Brockville_Power":
                    System.out.println("Brock");
                    this.campus = "Brockville";
                    break;
                case "Cornwall_Power":
                    System.out.println("Corn");
                    this.campus = "Cownwall";
                    break;
            }
        } else {
            this.campus = this.user.getPreferredCampus();
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
    
    
    
    public void updateTable() {
        this.tableData = this.queuecontroller.getQueueByStation(campus).stream()
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
            this.user.setInQueue(false);
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
    
}
