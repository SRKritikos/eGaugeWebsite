/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.DeviceDataClient;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.faces.bean.ManagedBean;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * An application scoped bean to hold a days worth of device data. 
 * 
 * @author Steven Kritikos
 */
@Singleton
@ManagedBean(name="devicedatamodel",  eager=true)
public class DeviceDataModelBean implements Serializable {
    
    private String deviceData;

    public String getDeviceData() {
        return deviceData;
    }

    public void setDeviceData(String deviceData) {
        this.deviceData = deviceData;
    }

    public DeviceDataModelBean() {
        System.out.println("APP BEAN STARTED");
    }
    
    
}
