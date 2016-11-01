/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.EmailController;
import com.slc.egaugewebsite.controller.UserTransactionController;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Steven Kritikos
 */
@RequestScoped
@ManagedBean(name="emailmsgbean")
public class EmailMessageBean {
    private String message; 
    private String toId;
    private String fromId;
    private String toName;
    @EJB
    private EmailController emailcontroller;
    @EJB
    UserTransactionController usercontroller;
    public EmailMessageBean() {
        
    }
    
    @PostConstruct
    private void init() {
    }
    
    public String getMessage() {
        return message;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    public String getToName() {
        Users_Entity toUser = this.usercontroller.getUserEntity(toId);
        this.toName = "  " + toUser.getFirstName();
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }
        
    public String sendMessage() {
        Users_Entity from = this.usercontroller.getUserEntity(this.fromId);
        Users_Entity to = this.usercontroller.getUserEntity(this.toId);
        this.emailcontroller.sendEmail(from.getEmail(), to.getEmail(), "Message regarding slc E.V. station" + from.getFirstName(), this.message);
        return "/index.xhtml?faces-redirect=true";
    }
}
