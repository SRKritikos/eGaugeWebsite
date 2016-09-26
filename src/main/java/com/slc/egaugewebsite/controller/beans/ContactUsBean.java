/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.controller.UserTransactionController;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name = "contactusbean")
@RequestScoped
public class ContactUsBean {
    @EJB
    private UserTransactionController usercontroller;
    @ManagedProperty("#{user}")
    private UserBean user;
    @ManagedProperty("#{contactinfobean}")
    private ContactInformationBean contactinfo;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public ContactInformationBean getContactinfo() {
        return contactinfo;
    }

    public void setContactinfo(ContactInformationBean contactinfo) {
        this.contactinfo = contactinfo;
    }
    
    public String contactUs() {
        try {
            System.out.println(this.contactinfo.getAdminEmailAddress());
            Users_Entity admin = this.usercontroller.getUserByEmail(this.contactinfo.getAdminEmailAddress());
            System.out.println("Now Redirecting to email msg to " + admin.getEmail() + " from " + this.user.getUserEmail());
            return "/emailmessage.xhtml?toId="+ admin.getUserId()+ "&fromId="+ this.user.getUser() +"&faces-redirect=true";
        } catch (Exception e) {
            System.out.println(e.toString());
            return "/index.xhtml?faces-redirect=true";
                   
        }
    }
}
