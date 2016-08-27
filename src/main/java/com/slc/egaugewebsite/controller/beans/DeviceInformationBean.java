/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name = "deviceinfobean")
@ApplicationScoped
public class DeviceInformationBean implements Serializable{
    private String kingston1Info;    
    private String kingston2Info;
    private String cornwallInfo;
    private String brockvilleInfo;
    private String mainInfoDiv;
    private String kingston1BorderColor;
    private String kingston2BorderColor;
    private String brockvilleBorderColor;
    private String cornwallBorderColor;
    

    public DeviceInformationBean() {
        this.kingston1Info = "";
        this.brockvilleInfo = "";
        this.cornwallInfo = "";
        this.kingston2Info = "";
        this.mainInfoDiv = "";  
        this.kingston1BorderColor = "green";
        this.kingston2BorderColor = "green";
        this.cornwallBorderColor = "green";
        this.brockvilleBorderColor = "green";
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

    public String getKingston1BorderColor() {
        return kingston1BorderColor;
    }

    public void setKingston1BorderColor(String kingston1BorderColor) {
        this.kingston1BorderColor = kingston1BorderColor;
    }

    public String getKingston2BorderColor() {
        return kingston2BorderColor;
    }

    public void setKingston2BorderColor(String kingston2BorderColor) {
        this.kingston2BorderColor = kingston2BorderColor;
    }

    public String getBrockvilleBorderColor() {
        return brockvilleBorderColor;
    }

    public void setBrockvilleBorderColor(String brockvilleBorderColor) {
        this.brockvilleBorderColor = brockvilleBorderColor;
    }

    public String getCornwallBorderColor() {
        return cornwallBorderColor;
    }

    public void setCornwallBorderColor(String cornwallBorderColor) {
        this.cornwallBorderColor = cornwallBorderColor;
    }
    
}
