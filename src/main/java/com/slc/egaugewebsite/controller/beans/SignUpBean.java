/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.UserTransactionController;
import com.slc.egaugewebsite.controller.ValidateCaptchaClient;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name = "signupbean")
@RequestScoped
public class SignUpBean implements Serializable {
    @ManagedProperty("#{user}")
    private UserBean user;
    @EJB
    private UserTransactionController usercontroller;
    @EJB
    private ValidateCaptchaClient captchaclient;
    private UIComponent errorMsg;
    private String email;
    private String password;
    private String preferredCampus;
    private String firstName;
    private String lastName;

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

    public String getPreferredCampus() {
        return preferredCampus;
    }

    public void setPreferredCampus(String preferredCampus) {
        this.preferredCampus = preferredCampus;
    }

    public UserTransactionController getUsercontroller() {
        return usercontroller;
    }

    public void setUsercontroller(UserTransactionController usercontroller) {
        this.usercontroller = usercontroller;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UIComponent getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(UIComponent errorMsg) {
        this.errorMsg = errorMsg;
    }
    
    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
    
    
    
    public String signUpUser() {
        FacesMessage msg = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            //Validate Captcha
            String captchaResponse = req.getParameter("g-recaptcha-response");
            boolean validCaptcha = this.captchaclient.validateCaptcha(captchaResponse);
            if (validCaptcha) {
                Users_Entity userEntity = usercontroller.signUpUser(email, password, preferredCampus, firstName, lastName);

                this.user.setPreferredCampus(userEntity.getPreferredCampus());
                this.user.setUserRole(userEntity.getRoleId().getRoleName());
                this.user.setUser(userEntity.getUserId());
                this.user.setFirstName(firstName);
                this.user.setLastName(lastName);
                this.user.setUserEmail(email);
                this.user.setInQueue(false);
                this.user.setCharging(false);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            msg.setSummary("Failed to create account");
            context.addMessage(this.errorMsg.getClientId(context), msg);
            System.out.println(e.toString());
            return null;    
        }
        return "index";
    }
}
