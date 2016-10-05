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
    private String adminEmail;
    private String adminName;
    public ContactInformationBean() {

       

    }
    
    @PostConstruct
    public void init() {
        this.adminEmail = "slcegauge@gmail.com";
        this.adminName = "Admin Name";
    }
    
    
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
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
