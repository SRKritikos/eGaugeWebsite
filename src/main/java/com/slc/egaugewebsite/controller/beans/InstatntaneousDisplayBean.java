/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Steven Kritikos
 */
@ViewScoped
@ManagedBean(name = "instantaneousdisplaybean", eager = true)
    public class InstatntaneousDisplayBean implements Serializable {

    private String reading;

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        System.out.println("########WE ARE SETTING THE READING#######");
        this.reading = reading;
    }
    
    
    
}
