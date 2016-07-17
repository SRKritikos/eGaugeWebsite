/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.DeviceDataClient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Steven Kritikos
 */
@ViewScoped
@ManagedBean(name = "devicedatabean", eager = true)
public class DeviceDataRequestBean {
    
private final DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    private String startDate;
    private String endDate;

    public String getStartDate() {
        if(this.startDate == null) {
            this.startDate = df.format(new Date());
        }
        return startDate;
    }

    public void setStartDate(String startDate) {
            this.startDate = startDate;
    }

    public String getEndDate() {
        if(this.endDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            this.endDate = df.format(cal.getTime());
        }
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
        /**
     * submits a form request to the client object then returns the data from the
     * request.
     * 
     * @return 
     */
    public String submitRequest() {
        DateFormat requestdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String rtVl = null;
        try {
            DeviceDataClient client = new DeviceDataClient();
            Date startDateObj = df.parse(this.startDate);
            Date endDateObj = df.parse(this.startDate); 
            
            String requestStartDate = requestdf.format(startDateObj);
            String requestEndDate = requestdf.format(endDateObj);
            
            rtVl = client.getData(requestStartDate, requestEndDate);
        } catch(Exception  e) {
            //TODO :  makes this return usefull errors - THIS IS TOP OF CHAIN RIGHT?
            System.out.println(e.toString());
        }
        return rtVl;
    }
    
}
