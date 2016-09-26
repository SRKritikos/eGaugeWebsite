/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.UserTransactionController;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.utils.ApplicationProperties;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Steven Kritikos
 */


@ManagedBean(name = "contactinfobean")
@ApplicationScoped
public class ContactInformationBean {
    private Properties properties;
    private String address;
    private String emailaddress;
    private String city;
    private String province;
    private String postalcode;
    private String phone;
    
    private String adminEmailAddress;
    public ContactInformationBean() {
        this.adminEmailAddress ="stevenrkritikos@outlook.com";
        try {
            this.properties = ApplicationProperties.getApplicationProperties();
        } catch (IOException ex) {
            System.out.println("Failed to initialize Contact Information");
            System.out.println(ex.toString());
        }
        System.out.println("INFO BEAN: " + this.properties);
        if (this.properties != null) {
            System.out.println("Updating properties");
            this.address = this.properties.getProperty("address");
            this.emailaddress = this.properties.getProperty("emailaddress");
            this.city = this.properties.getProperty("city");
            this.postalcode = this.properties.getProperty("postalcode");
            this.province =  this.properties.getProperty("province");
            this.phone = this.getProperties().getProperty("phone");
        } else {
            System.out.println("#######INFO BEAN STARTING###########");
            this.address = "";
            this.city = "";
            this.emailaddress = "";
            this.phone = ""; 
            this.postalcode = "";
            this.province = "";
        }
    }
    
    @PostConstruct
    public void init() {
        
    }
    
    
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdminEmailAddress() {
        return adminEmailAddress;
    }    
    
    public void updatePropertiesFile() {
        //TODO - not sure how you get access to save the file - all I can find is how to create - does this mean that recreating the file will just over ride old values? 
        System.out.println("UPDATING THE PROP FILE");
//        try {
//            ApplicationProperties.updateProperty("address", this.address);
//            ApplicationProperties.updateProperty("emailaddress", this.emailaddress);
//            ApplicationProperties.updateProperty("city", this.city);
//            ApplicationProperties.updateProperty("province", this.province);
//            ApplicationProperties.updateProperty("postalcode", this.postalcode);
//            ApplicationProperties.updateProperty("phone", this.phone);
//        } catch (IOException ex) {
//            System.out.println(ex.toString());
//            Logger.getLogger(ContactInformationBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    
}
