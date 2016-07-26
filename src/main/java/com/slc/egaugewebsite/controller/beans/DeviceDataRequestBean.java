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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * //TODO: make this class request from application bean for the model it should display
 * @author Steven Kritikos
 */
@ViewScoped
@ManagedBean(name = "devicedatabean")
public class DeviceDataRequestBean implements Serializable {
    @EJB
    private DeviceDataModelBean dataModel;
    
    private final DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    private String startDate;
    private String endDate;
    private String data; 

    public DeviceDataRequestBean() {
        System.out.println("Device data bean ready");
    }

    @PostConstruct
    public void init(){
        System.out.println("bean injected " + dataModel.getDeviceData());
    }
    public String getData() {
        data = dataModel.getDeviceData();
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
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

    public DeviceDataModelBean getDataModel() {
        return dataModel;
    }

    public void setDataModel(DeviceDataModelBean dataModel) {
        this.dataModel = dataModel;
    }

    
    /**
     * submits a form request to the client object then returns the data from the
     * request.
     * 
     * @return 
     */
    public void submitRequest() {
        DateFormat requestdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String rtVl = null;
        try {
            if (this.startDate != null  && this.endDate != null) {
                //Get client controller to send request for data.
                DeviceDataClient client = new DeviceDataClient();
                Date startDateObj = df.parse(this.startDate);
                Date endDateObj = df.parse(this.endDate); 
                String requestStartDate = requestdf.format(startDateObj);
                String requestEndDate = requestdf.format(endDateObj);
                rtVl = client.getData(requestStartDate, requestEndDate);
                dataModel.setDeviceData(rtVl);
            }
        } catch(Exception  e) {
            //TODO :  makes this return usefull errors - THIS IS TOP OF CHAIN RIGHT?
            System.out.println(e.toString());
            e.printStackTrace();
            
        }
        
       
    }

}
