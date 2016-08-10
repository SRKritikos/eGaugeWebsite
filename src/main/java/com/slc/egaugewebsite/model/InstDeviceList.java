/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Steven Kritikos
 */
public class InstDeviceList implements Serializable{
    private List<InstDevice> devices;

    public List<InstDevice> getDevices() {
        return devices;
    }

    public void setDevices(List<InstDevice> devices) {
        this.devices = devices;
    }
}
