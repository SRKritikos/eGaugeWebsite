/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller;

import com.slc.egaugewebsite.data.dao.UserrolesDAO;
import com.slc.egaugewebsite.data.dao.UsersDAO;
import com.slc.egaugewebsite.data.dao.exceptions.RollbackFailureException;
import com.slc.egaugewebsite.data.entities.Userroles_Entity;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.utils.AuthenticationUtils;
import com.slc.egaugewebsite.utils.UserRole;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.UserTransaction;
/**
 * Controller class to handle user interactions with the database.
 * provides user transaction and entity manager to DAO class.
 * 
 * Business logic for user interactions with the database goes here
 * @author Steven Kritikos
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UserTransactionController {
    @Resource
    private UserTransaction utx;
    @PersistenceContext(unitName = "com.slc_eGaugeWebsite_war_1.2-SNAPSHOTPU", type = PersistenceContextType.TRANSACTION)
    private EntityManager em;
    private UsersDAO usersdao;
    private UserrolesDAO rolesdao;

    public UserTransactionController() {
    }
    
    @PostConstruct
    public void init() {
        this.usersdao = new UsersDAO(utx, em);
        this.rolesdao = new UserrolesDAO(utx, em);
    }
    
    public Users_Entity ValidateUser(String email, String password) throws Exception {
        Users_Entity rtVl = null;
        rtVl = usersdao.getUserByEmail(email);
        if (rtVl == null) {
            throw new Exception("invalid");
        }
        boolean validPassword = AuthenticationUtils.isExpectedPassword(password.toCharArray(), rtVl.getPasswordSalt(), rtVl.getPassword());
        if (!validPassword) {
            throw new Exception("invalid");
        }
        
        return rtVl;
    }
    
    public Users_Entity signUpUser(String email, String password, String prefferedCampus,
                                    String firstName, String lastName) throws Exception {
        Users_Entity userEntity = usersdao.getUserByEmail(email);
        Users_Entity rtVl = null;
        if (userEntity == null) {
            try {
                // Generate salt and password to insert into database
                byte[] salt = AuthenticationUtils.getNextSalt();
                byte[] hashedPassword = AuthenticationUtils.hash(password.toCharArray(), salt);
                
                //build user entity object to unsert into db
                Userroles_Entity userRole = rolesdao.getUserRoleByName(UserRole.defaultuser);
                Users_Entity newUser  = new Users_Entity();
                newUser.setUserId(UUID.randomUUID().toString());
                newUser.setEmail(email);
                newUser.setFirstName(firstName);
                newUser.setLastName(lastName);
                newUser.setPassword(hashedPassword);
                newUser.setPasswordSalt(salt);
                newUser.setRoleId(userRole);
                newUser.setPreferredCampus(prefferedCampus);
                newUser.setAvailableEndTime(null);
                newUser.setAvailableStartTime(null);
                newUser.setTimeStartedCharging(null);
                newUser.setTimeEndedCharging(null);
                newUser.setExtendTimeTries(0);
                newUser.setIsActive(null);
                newUser.setTimeEnteredQueue(null);
                newUser.setDeviceId(null);
                
                usersdao.create(newUser);
                rtVl = usersdao.getUserByEmail(newUser.getEmail());
            } catch (RollbackFailureException ex) {
                System.out.println(ex.getMessage());
               throw new Exception("server error");
            } catch (Exception ex) {
                System.out.println(ex.toString());
                throw new Exception("server error");
            }
            
        } else {
           throw new Exception("e-mail already exists");
        }
        return rtVl;
    }
    
    /**
     * Function to change the user's role 
     */
    public void updateUserRole(String email) {
        try {
            Users_Entity userEntity = this.usersdao.getUserByEmail(email);
            int roleId = userEntity.getRoleId().getRoleId();
            //If user make admin else make user;
            if (roleId == 2) {
                userEntity.setRoleId(this.rolesdao.getUserRoleByName(UserRole.admin));
            } else {
                userEntity.setRoleId(this.rolesdao.getUserRoleByName(UserRole.defaultuser));
            }
            this.usersdao.edit(userEntity);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(UserTransactionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UserTransactionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateUser(Users_Entity user) {
        try {
            this.usersdao.edit(user);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(UserTransactionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UserTransactionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Users_Entity getUserByEmail(String email) {
        return this.usersdao.getUserByEmail(email);
    }
    
    public void deleteUser(String email) {
        try {
            Users_Entity user = this.usersdao.getUserByEmail(email);
            this.usersdao.destroy(user.getUserId());
        } catch (RollbackFailureException ex) {
            Logger.getLogger(UserTransactionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UserTransactionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Users_Entity> getUsers() {
        return usersdao.findUsers_EntityEntities();
    }
    
    public List<Users_Entity> getAdminUsers() {
        return rolesdao.getUserRoleByName(UserRole.admin).getUsersList();
    }
    
    public List<Users_Entity> getRegularUsers() {
        return rolesdao.getUserRoleByName(UserRole.defaultuser).getUsersList();
    }
    
    public Users_Entity getUserEntity(String userid) {
        return usersdao.findUsers_Entity(userid);
    }
    
    
}
