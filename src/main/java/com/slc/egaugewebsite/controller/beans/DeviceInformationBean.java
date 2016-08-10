/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Steven Kritikos
 */

@ManagedBean(name = "deviceinfobean")
@ApplicationScoped
public class DeviceInformationBean {
    private String kingston1Info;    
    private String kingston2Info;
    private String cornwallInfo;
    private String brockvilleInfo;
    private String mainInfoDiv;
    

    public DeviceInformationBean() {
        this.kingston1Info = "";
        this.brockvilleInfo = "";
        this.cornwallInfo = "";
        this.kingston2Info = "";
        this.mainInfoDiv = "";  
    }
    
    public String getkingston1Info() {
        return kingston1Info;
    }
    public void setkingston1Info(String testString) {
        this.kingston1Info = testString;
    }

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

    public String getCornwallInfo() {
        return cornwallInfo;
    }

    public void setCornwallInfo(String cornwallInfo) {
        this.cornwallInfo = cornwallInfo;
    }

    public String getBrockvilleInfo() {
        return brockvilleInfo;
    }

    public void setBrockvilleInfo(String brockvilleInfo) {
        this.brockvilleInfo = brockvilleInfo;
    }

    public String getMainInfoDiv() {
        return mainInfoDiv;
    }

    public void setMainInfoDiv(String mainInfoDiv) {
        this.mainInfoDiv = mainInfoDiv;
    }
    
    
    
    
    
    

    
}
