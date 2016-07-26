/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.beans;

import com.slc.egaugewebsite.data.dao.UserrolesDAO;
import com.slc.egaugewebsite.data.dao.UsersDAO;
import com.slc.egaugewebsite.data.entities.Userroles_Entity;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.utils.AuthenticationUtils;
import com.slc.egaugewebsite.utils.DatabaseUtils;
import com.slc.egaugewebsite.utils.SessionUtils;
import com.slc.egaugewebsite.utils.UserRole;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;

/**
 *
 * @author Steven Kritikos
 */
@ManagedBean(name = "signupbean")
@RequestScoped
public class SignUpBean implements Serializable {
    @ManagedProperty("#{user}")
    private UserBean user;
    
    private final EntityManagerFactory emf;    
    private final UsersDAO usersdao;
    private final UserrolesDAO roledao;

    public SignUpBean() {
       this.emf = DatabaseUtils.getEntityManager();
       this.usersdao = new UsersDAO(emf);
       this.roledao = new UserrolesDAO(emf);
    }
    
    private String email;
    private String password;
    private String preferredCampus;

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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
    
    
    
    public String signUpUser() {
        try {
            Users_Entity userEntity = usersdao.getUserByEmail(email);
            if (userEntity == null) {
                // Generate salt and password to insert into database
                byte[] salt = AuthenticationUtils.getNextSalt();
                byte[] hashedPassword = AuthenticationUtils.hash(password.toCharArray(), salt);

                //insert
                Userroles_Entity roleEntity = roledao.getUserRoleByName(UserRole.defaultuser);
                usersdao.insertUser(email, hashedPassword, salt, preferredCampus, roleEntity);
                
                //login the user
                userEntity = usersdao.getUserByEmail(email);
                if (userEntity != null) {
                    this.user.setPreferredCampus(userEntity.getPreferredCampus());
                    this.user.setUserRole(userEntity.getRoleId().getRoleName());
                    this.user.setUser(userEntity.getUserId());
                    
                    HttpSession session = SessionUtils.getSession();
                    session.setAttribute("userId", this.user.getUser());
                    session.setAttribute("userRol", this.user.getUserRole());
                } else {
                    throw new ValidationException("Failed to create user");
                }
            } else {
                throw new ValidationException("E-mail Exists");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return "signup";
            
        }
        return "index";
    }
}
