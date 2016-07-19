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
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


/**
 * //TODO: make this class request from application bean for the model it should display
 * @author Steven Kritikos
 */
@RequestScoped
@ManagedBean(name = "devicedatabean", eager = true)
public class DeviceDataRequestBean implements Serializable {
                       
private final DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    private String startDate;
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        System.out.println("########WE ARE SETTING THE STARTDATE#######");
        if(startDate == null || startDate.isEmpty() ) {
            this.startDate = df.format(new Date());
        }  else {
        this.startDate = startDate;
        }
        System.out.println("start Date is: " + this.startDate );
    }

    public String getEndDate() {

        return endDate;
    }

    public void setEndDate(String endDate) {
        if (endDate == null || endDate.isEmpty()) {
            // Get a day back
            Calendar tempCal = Calendar.getInstance();
            tempCal.setTime(new Date());
            tempCal.add(Calendar.DAY_OF_MONTH, -1);
            this.endDate = df.format(tempCal.getTime());
            
        }   else {
            this.endDate = endDate;
        }
        System.out.println("end Date is: " + this.endDate );
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
            //Get the 2 request dates from the user
            String requestedStartDate  = FacesContext.getCurrentInstance().
		getExternalContext().getRequestParameterMap().get("requestStartDate");
            String requestedEndDate = FacesContext.getCurrentInstance().
		getExternalContext().getRequestParameterMap().get("requestEndDate");
            //validate and set the start and end dates
            this.setStartDate(requestedStartDate);
            this.setEndDate(requestedEndDate);
            //Get client controller to send request for data.
            DeviceDataClient client = new DeviceDataClient();
            Date startDateObj = df.parse(this.startDate);
            Date endDateObj = df.parse(this.endDate); 
            System.out.println(startDateObj + "    " + endDateObj);
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
