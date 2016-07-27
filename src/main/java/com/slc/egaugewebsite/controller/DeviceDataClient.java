/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slc.egaugewebsite.controller;

import com.slc.egaugewebsite.controller.beans.DeviceDataModelBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
@Stateless
public class DeviceDataClient implements Job {

    @EJB
    private DeviceDataModelBean modelBean;
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/eGaugeWebService/web";
    private DateFormat requestdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    public DeviceDataClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("data");
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
    
    public String getInstData(String[] campus) {
        WebTarget resource = webTarget;
        if (campus != null) {
            resource = resource.queryParam("campus", campus);
        }
        
        resource = resource.path("instdata");
        
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }
    
    public void close() {
        client.close();
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Updating Model Bean");
        this.updateBean();
    }
    
    public void updateBean() {
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(new Date());
        Calendar yesterdayCal = Calendar.getInstance();
        yesterdayCal.setTime(new Date());
        yesterdayCal.add(Calendar.DAY_OF_MONTH, -1);
        
        String now = requestdf.format(nowCal.getTime());
        String yesterday = requestdf.format(yesterdayCal.getTime());
        modelBean.setDeviceData(this.getData(now, yesterday));
    }

    
}
