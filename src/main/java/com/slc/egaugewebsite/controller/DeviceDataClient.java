/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slc.egaugewebsite.controller;


import com.google.gson.Gson;
import com.slc.egaugewebsite.data.entities.Device_Entity;
import com.slc.egaugewebsite.model.InstDeviceList;
import com.slc.egaugewebsite.utils.ApplicationProperties;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;


/**
 * Jersey REST client generated for REST resource:DeviceDataController
 * [data]<br>
 * USAGE:
 * <pre>
 *        DeviceDataBean client = new DeviceDataBean();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Steven
 */
@Singleton
@Startup
public class DeviceDataClient implements Serializable{;
    private WebTarget webTarget;
    private Client client;
    private String BASE_URI;
    private DateFormat requestdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
    private Properties appProperties;

    public DeviceDataClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
      try {
        this.appProperties = ApplicationProperties.getApplicationProperties();
        this.BASE_URI = appProperties.getProperty("baseurl") + "slcegaugeapi/api";
      } catch (IOException ex) {
        ex.printStackTrace();
      }
        webTarget = client.target(BASE_URI).path("devices");
    }

    public void putJson(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public String getData(String startDate, String endDate) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }
        resource = resource.path("data");
        
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }
    
    /**
     * Get devices' instantaneous reading
     * 
     * @param campus
     * @return 
     */
    public InstDeviceList getInstData(String[] campus) {
        Gson gson = new Gson();
        WebTarget resource = webTarget;
        InstDeviceList devices = null;
        if (campus != null) {
            resource = resource.queryParam("campus", campus);
        }
        try {
        resource = resource.path("instdata");
        String jsonResponse = resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        devices = gson.fromJson(jsonResponse, InstDeviceList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devices;
    }
    
    /**
     * Get the device information for given campus name
     * 
     * @param campusName
     * @return 
     */
    public Device_Entity getDeviceByName(String campusName) {
        
        Gson gson = new Gson();
        WebTarget resource = webTarget;
        
        if (campusName == null){
            campusName = "Kingston";
        } 
        
        resource = resource.queryParam("campus", campusName).path("device");
        String jsonResponse = resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        Device_Entity device = gson.fromJson(jsonResponse, Device_Entity.class);
        
        return  device;
        
    }
    
    public void close() {
        client.close();
    }

   
    
}
