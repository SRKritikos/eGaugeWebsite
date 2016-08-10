/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Steven Kritikos
 */
public class InstDevice implements Serializable{
    private String deviceName;
    private BigDecimal instPower;
    private String message;
    
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public BigDecimal getInstPower() {
        return instPower;
    }

    public void setInstPower(BigDecimal instPower) {
        this.instPower = instPower;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
}
