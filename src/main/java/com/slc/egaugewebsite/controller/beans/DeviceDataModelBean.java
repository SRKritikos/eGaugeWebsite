/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;


import java.io.Serializable;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;


/**
 * An application scoped bean to hold a days worth of device data. 
 * 
 * @author Steven Kritikos
 */
@Singleton
@Startup
public class DeviceDataModelBean implements Serializable {
    
    private String deviceData;
    
    public String getDeviceData() {
        return deviceData;
    }

    public void setDeviceData(String deviceData) {
        System.out.println("Updating Device Data Model");
        this.deviceData = deviceData;
    }

    public DeviceDataModelBean() {
        System.out.println("APP BEAN STARTED");
    }
    
    
}
