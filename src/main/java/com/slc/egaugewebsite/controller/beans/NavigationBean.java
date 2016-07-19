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

@ManagedBean(name = "navigationbean", eager = true)
@RequestScoped
public class NavigationBean {
    @ManagedProperty(value="#{param.pageName}")
    private String pageName;
    
    public String directToPage() {
        switch (pageName.toUpperCase()) {
            case "INDEX": 
                return "/index.xhtml?faces-redirect=true";              
            case "SIGNUP":
                return "/signup.xhtml?faces-redirect=true";
            case "USER":
                return "/user.xhtml?faces-redirect=true";
            case "ADMIN": 
                return "/admin.xhtml?faces-redirect=true";
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
