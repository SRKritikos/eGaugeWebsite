/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name= "admindeviceinfobean")
@RequestScoped
public class AdminUpdateInfromationBean {
    private String kingston1Info;
    private String kingston2Info;
    private String brockvilleInfo;
    private String cornwallInfo;
    private String mainInfoDiv;
    
    @ManagedProperty(value = "#{deviceinfobean}")
    private DeviceInformationBean deviceinfo;

    public String getKingston1Info() {
        return kingston1Info;
    }

    public void setKingston1Info(String kingston1Info) {
        this.kingston1Info = kingston1Info;
    }

    public String getKingston2Info() {
        return kingston2Info;
    }

    public void setKingston2Info(String kingston2Info) {
        this.kingston2Info = kingston2Info;
    }

    public String getBrockvilleInfo() {
        return brockvilleInfo;
    }

    public void setBrockvilleInfo(String brockvilleInfo) {
        this.brockvilleInfo = brockvilleInfo;
    }

    public String getCornwallInfo() {
        return cornwallInfo;
    }

    public void setCornwallInfo(String cornwallInfo) {
        this.cornwallInfo = cornwallInfo;
    }

    public String getMainInfoDiv() {
        return mainInfoDiv;
    }

    public void setMainInfoDiv(String mainInfoDiv) {
        this.mainInfoDiv = mainInfoDiv;
    }

    
    public DeviceInformationBean getDeviceinfo() {
        return deviceinfo;
    }

    public void setDeviceinfo(DeviceInformationBean deviceinfo) {
        this.deviceinfo = deviceinfo;
    }
    
    
    public void updateInformation() {
        this.deviceinfo.setBrockvilleInfo(brockvilleInfo);
        this.deviceinfo.setCornwallInfo(cornwallInfo);
        this.deviceinfo.setKingston1Info(kingston1Info);
        this.deviceinfo.setKingston2Info(kingston2Info);
        this.deviceinfo.setMainInfoDiv(mainInfoDiv);
    }
}
