/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.DeviceDataClient;
import com.slc.egaugewebsite.model.InstDeviceList;
import com.slc.egaugewebsite.utils.DBDeviceNames;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name = "instbean")
@RequestScoped
public class InstantaneousDisplayBean implements Serializable {
    private BigDecimal kingston1Power;
    private BigDecimal kingston2Power;
    private BigDecimal cornwallPower;
    private BigDecimal brockvillePower;
    @EJB
    private DeviceDataClient ddc;
    
    public BigDecimal getKingston1Power() {
        return kingston1Power;
    }

    public void setKingston1Power(BigDecimal kingston1Power) {
        this.kingston1Power = kingston1Power;
    }

    public BigDecimal getKingston2Power() {
        return kingston2Power;
    }

    public void setKingston2Power(BigDecimal kingston2Power) {
        this.kingston2Power = kingston2Power;
    }

    public BigDecimal getCornwallPower() {
        return cornwallPower;
    }

    public void setCornwallPower(BigDecimal cornwallPower) {
        this.cornwallPower = cornwallPower;
    }

    public BigDecimal getBrockvillePower() {
        return brockvillePower;
    }

    public void setBrockvillePower(BigDecimal brockvillePower) {
        this.brockvillePower = brockvillePower;
    }

    public DeviceDataClient getDdc() {
        return ddc;
    }

    public void setDdc(DeviceDataClient ddc) {
        this.ddc = ddc;
    }
    
    /**
     * update the fields of the class with the most recent instantaneous data reading
     */
    public void updateReadings() {
        InstDeviceList devices = ddc.getInstData(new String[0]);
        try {
          BigDecimal tempPower = devices.getDevices().stream()
                  .filter(device -> device.getDeviceName().equals(DBDeviceNames.KINGSTON_1.getEntityName()))
                  .findFirst()
                  .get()
                  .getInstPower();
          this.kingston1Power = tempPower.compareTo(BigDecimal.valueOf(100)) == 1 ? tempPower : BigDecimal.ZERO;
          tempPower = devices.getDevices().stream()
                  .filter(device -> device.getDeviceName().equals(DBDeviceNames.KINGSTON_2.getEntityName()))
                  .findFirst()
                  .get()
                  .getInstPower();
          this.kingston2Power = tempPower.compareTo(BigDecimal.valueOf(100)) == 1 ? tempPower : BigDecimal.ZERO;
          tempPower = devices.getDevices().stream()
                  .filter(device -> device.getDeviceName().equals(DBDeviceNames.BROCKVILLE.getEntityName()))
                  .findFirst()
                  .get()
                  .getInstPower();
          this.brockvillePower = tempPower.compareTo(BigDecimal.valueOf(100)) == 1 ? tempPower : BigDecimal.ZERO;
          tempPower = devices.getDevices().stream()
                  .filter(device -> device.getDeviceName().equals(DBDeviceNames.CORNWALL.getEntityName()))
                  .findFirst()
                  .get()
                  .getInstPower();
          this.cornwallPower = tempPower.compareTo(BigDecimal.valueOf(100)) == 1 ? tempPower : BigDecimal.ZERO;
        } catch (Exception e) {
          e.printStackTrace();
        }
    }
}
