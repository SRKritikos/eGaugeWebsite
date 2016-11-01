/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.EmailController;
import com.slc.egaugewebsite.controller.UserTransactionController;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name = "loginbean")
@RequestScoped
public class LoginBean implements Serializable {
    @ManagedProperty("#{user}")
    private UserBean user;
    @EJB
    private UserTransactionController usercontroller;
    @EJB
    private EmailController emailcontroller;
    private String email;
    private String password;
    private UIComponent errorMsg;
    boolean errorThrown;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public UserTransactionController getUsercontroller() {
        return usercontroller;
    }

    public void setUsercontroller(UserTransactionController usercontroller) {
        this.usercontroller = usercontroller;
    }

    public UIComponent getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(UIComponent errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isErrorThrown() {
        return errorThrown;
    }

    public void setErrorThrown(boolean errorThrown) {
        this.errorThrown = errorThrown;
    }

    public EmailController getEmailcontroller() {
        return emailcontroller;
    }

    public void setEmailcontroller(EmailController emailcontroller) {
        this.emailcontroller = emailcontroller;
    }
    
    
    
    public String loginUser() {
        this.email = this.email.trim();
        this.password = this.password.trim();
        try {
            Users_Entity userEntity = usercontroller.ValidateUser(this.email, this.password);
            // build user session
            this.user.setPreferredCampus(userEntity.getPreferredCampus());
            this.user.setUserRole(userEntity.getRoleId().getRoleName());
            this.user.setUser(userEntity.getUserId());
            this.user.setFirstName(userEntity.getFirstName());
            this.user.setLastName(userEntity.getLastName());
            this.user.setUserEmail(userEntity.getEmail());
            this.user.setExtendedTimeTries(userEntity.getExtendTimeTries());          
            if (userEntity.getTimeEnteredQueue() != null) {
                // check if they are in queue depending on if the time they entered queue is not null 
                this.user.setInQueue(true);
                this.user.setCharging(userEntity.getIsActive());
                // make sure user 
                if (userEntity.getTimeEndedCharging() != null) {
                    this.user.setFinishedCharging(true);
                }
            } else {
                this.user.setInQueue(false);
                this.user.setCharging(false);
                this.user.setFinishedCharging(false);
            }
        } catch (Exception e) {
            this.errorThrown = true;
            FacesMessage msg = new FacesMessage();
            FacesContext context = FacesContext.getCurrentInstance();
            
            if (e.getMessage().equals("invalid")) {
                msg.setSummary("Username and password don't match");
            } else {
                msg.setSummary("Failed to log in. Internal server error");
            }
            System.out.println(e.getMessage());
            context.addMessage(this.errorMsg.getClientId(context), msg);
            return null;
        }
        return "index?faces-redirect=true";
    }
}
