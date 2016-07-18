/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller;

import com.slc.egaugewebsite.controller.beans.UserBean;
import com.slc.egaugewebsite.data.dao.UserrolesDAO;
import com.slc.egaugewebsite.data.dao.UsersDAO;
import com.slc.egaugewebsite.data.entities.Userroles_Entity;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.utils.AuthenticationUtils;
import com.slc.egaugewebsite.utils.UserRole;
import com.slc.egaugewebsite.utils.DatabaseUtils;
import com.slc.egaugewebsite.utils.SessionUtils;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedProperty;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;

/**
 *
 * @author Steven Kritikos
 */
public class UserController {
    @ManagedProperty("#user")
    private UserBean user;
    private final EntityManagerFactory emf;    
    private final UsersDAO usersdao;
    private final UserrolesDAO roledao;

    public UserController() {
       this.emf = DatabaseUtils.getEntityManager();
       this.usersdao = new UsersDAO(emf);
       this.roledao = new UserrolesDAO(emf);
    }
    
    
    /**
     * Validate and hash password then insert new user into db
     * @param email
     * @param password
     * @param preferredCampus
     * @param roleId
     * @return
     * @throws Exception 
     */
    public boolean createUser(String email, String password, String preferredCampus, UserRole roleId) throws Exception {
        boolean rtVl = false;
        
        try {
            Users_Entity userEntity = usersdao.getUserByEmail(email);
            if (userEntity == null) {
                // Generate salt and password to insert into database
                byte[] salt = AuthenticationUtils.getNextSalt();
                byte[] hashedPassword = AuthenticationUtils.hash(password.toCharArray(), salt);

                //insert
                Userroles_Entity roleEntity = roledao.getUserRoleByName(roleId);
                usersdao.insertUser(email, hashedPassword, salt, preferredCampus, roleEntity);
                
                //login the user
                userEntity = usersdao.getUserByEmail(email);
                if (userEntity != null) {
                    this.user.setPreferredCampus(userEntity.getPreferredCampus());
                    this.user.setUserRole(userEntity.getRoleId().getRoleName());
                    this.user.setUsername(userEntity.getEmail());
                    
                    HttpSession session = SessionUtils.getSession();
                    session.setAttribute("user", this.user);
                } else {
                    throw new ValidationException("Failed to create user");
                }
                rtVl = true;
            } else {
                throw new ValidationException("E-mail Exists");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            
        }
        return rtVl;
    }
    

    
    

   
}
