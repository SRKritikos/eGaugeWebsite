/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.EmailController;
import com.slc.egaugewebsite.controller.UserTransactionController;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.utils.AuthenticationUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@ManagedBean(name = "forgotpasswordbean")
@RequestScoped
public class ForgotPasswordBean {
    @ManagedProperty("#{user}")
    private UserBean user;
    @EJB
    private UserTransactionController usercontroller;
    @EJB
    private EmailController emailcontroller;
    private String email;
    private UIComponent errorMsg;
    private String msgColor;
    private String oldPassword;
    private String newPassword;
    
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UIComponent getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(UIComponent errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getMsgColor() {
        return msgColor;
    }

    public void setMsgColor(String msgColor) {
        this.msgColor = msgColor;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
    
    public String changePassword() {
        FacesMessage msg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Users_Entity userEntity = this.usercontroller.ValidateUser(this.email, oldPassword);
            if (userEntity != null) {
                byte[] password = AuthenticationUtils.hash(this.newPassword.toCharArray(), userEntity.getPasswordSalt());
                userEntity.setPassword(password);
                this.usercontroller.updateUser(userEntity); 
                // Log user into session
                this.user.setPreferredCampus(userEntity.getPreferredCampus());
                this.user.setUserRole(userEntity.getRoleId().getRoleName());
                this.user.setUser(userEntity.getUserId());
                this.user.setFirstName(userEntity.getFirstName());
                this.user.setLastName(userEntity.getLastName());
                this.user.setUserEmail(email);
                this.user.setInQueue(false);
                this.user.setCharging(false);
            } else {
                this.msgColor = "red";
                msg.setSummary("Invalid email.");
                context.addMessage(this.errorMsg.getClientId(context), msg);
                return null;
            }
        } catch (Exception e) {
            this.msgColor = "red";
            if (e.getMessage().equals("invalid")) {
                msg.setSummary("Invalid password.");
            } else {
                msg.setSummary("Failed to change password.");
            }
            System.out.println(e.getMessage());
            context.addMessage(this.errorMsg.getClientId(context), msg);
            return null;
        }
        return "/index.xhtml?faces-redirect=true";
    }
    
     public void forgotPassword() {
         FacesMessage msg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        Users_Entity userEntity = this.usercontroller.getUserByEmail(this.email);
        if (userEntity != null) {
             try {
                 this.emailcontroller.sendPasswordResetEmail(userEntity);
                 msg.setSummary("Email with instructions to change password has been sent.");
                 msgColor = "green";
             } catch (Exception ex) {
                 msg.setSummary("Failed to send email");
                 msgColor = "red";
             }
        } else {
            msg.setSummary("Failed to send email");
            msgColor = "red";
        } 
        context.addMessage(this.errorMsg.getClientId(context), msg);
    }
}
