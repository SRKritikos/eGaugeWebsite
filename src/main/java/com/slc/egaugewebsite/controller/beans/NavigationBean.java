/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Steven Kritikos
 */

@ManagedBean(name = "navigationbean")
@RequestScoped
public class NavigationBean implements Serializable{
    @ManagedProperty(value="#{param.pageName}")
    private String pageName;
    
    public String directToPage() {
        switch (pageName.toUpperCase()) {
            case "INDEX": 
                return "/index.xhtml?faces-redirect=true";              
            case "SIGNUP":
                return "/signup.xhtml?faces-redirect=true";
            case "QUEUE":
                return "/queue.xhtml?faces-redirect=true";
            case "PROFILE":
                return "/profile.xhtml?faces-redirect=true";
            case "ADMIN": 
                return "/admin.xhtml?faces-redirect=true";
            case "MAP":
                return "/map.xhtml?faces-redirect=true";
            case "SPONSORS":
                return "sponsors.xhtml?faces-redirect=true";
        }
        return "/index.xhtml?faces-redirect=true";
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
    
    
}
