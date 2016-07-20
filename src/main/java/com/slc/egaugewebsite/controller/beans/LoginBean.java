/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.data.dao.UserrolesDAO;
import com.slc.egaugewebsite.data.dao.UsersDAO;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.utils.AuthenticationUtils;
import com.slc.egaugewebsite.utils.DatabaseUtils;
import com.slc.egaugewebsite.utils.SessionUtils;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name = "loginbean", eager = true)
@RequestScoped
public class LoginBean {
    @ManagedProperty("#{user}")
    private UserBean user;
    private final EntityManagerFactory emf;    
    private final UsersDAO usersdao;
    private final UserrolesDAO roledao;
    private String email;
    private String password;

    public LoginBean() {
       this.emf = DatabaseUtils.getEntityManager();
       this.usersdao = new UsersDAO(emf);
       this.roledao = new UserrolesDAO(emf);
    }
   

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
    
    
    public String loginUser() {
        try {
            Users_Entity userEntity = usersdao.getUserByEmail(this.email);
            if (userEntity == null) {
                throw new Exception("User does not exist");
            }
            System.out.println("CHECK IF USER EXISTS");
            boolean validPassword = AuthenticationUtils.isExpectedPassword(this.password.toCharArray(), userEntity.getPasswordSalt(), userEntity.getPassword());
            if (!validPassword) {
                throw new Exception("Password doesn't match");
            }
            System.out.println("VALIDATED PASSWORD");
            // build user session
            this.user.setPreferredCampus(userEntity.getPreferredCampus());
            this.user.setUserRole(userEntity.getRoleId().getRoleName());
            this.user.setUser(userEntity.getUserId());
            
            //add user to session
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("userRole", this.user.getUserRole());
            session.setAttribute("userId", this.user.getUser());
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "signup";
        }
        
        return "index";
    }
    
    
    
}
