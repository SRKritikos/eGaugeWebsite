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
                return "index";              
            case "SIGNUP":
                return "signup";
        }
        return "index";
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
    
    
}
