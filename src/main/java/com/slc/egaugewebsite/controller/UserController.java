/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller;

import com.slc.egaugewebsite.data.dao.UserrolesDAO;
import com.slc.egaugewebsite.data.dao.UsersDAO;
import com.slc.egaugewebsite.data.entities.Userroles_Entity;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.utils.AuthenticationUtils;
import com.slc.egaugewebsite.utils.UserRole;
import com.slc.egaugewebsite.utils.DatabaseUtils;
import java.util.UUID;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Steven Kritikos
 */
public class UserController {
    private EntityManagerFactory emf = DatabaseUtils.getEntityManager();
    @Resource
    private UserTransaction utx;
    
    UsersDAO usersdao = new UsersDAO(utx, emf);
    UserrolesDAO roledao = new UserrolesDAO(utx, emf);
    
    public boolean createUser(String email, String password, String preferredCampus, UserRole roleId) throws Exception {
        boolean rtVl = false;
        //TODO: validate that user doesnt exist
        try {
            // Generate salt and password to insert into database
            byte[] salt = AuthenticationUtils.getNextSalt();
            byte[] hashedPassword = AuthenticationUtils.hash(password.toCharArray(), salt);
            
            //build user entity object to insert to db
            Userroles_Entity roleEntity = roledao.getUserRoleByName(roleId);
            Users_Entity userEntity = new Users_Entity();
            userEntity.setUserId(UUID.randomUUID().toString());
            userEntity.setEmail(email);
            userEntity.setPassword(hashedPassword);
            userEntity.setPasswordSalt(salt);
            userEntity.setPrefferedCampus(preferredCampus);
            userEntity.setRoleId(roleEntity);
            //insert
            usersdao.create(userEntity);
            rtVl = true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return rtVl;
    }
    
    //TODO: Get user - takes e-mail and password
    //- uses auth utils to check if the passwords match then returns true or false;
    
   
}
