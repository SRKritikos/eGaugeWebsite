/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.utils;

import com.slc.egaugewebsite.data.entities.Users_Entity;
import java.util.Calendar;
import java.util.UUID;

/**
 * Class to help generate random users
 *  Used in the application to generate temporary unknown charging users
 * @author Steven Kritikos
 */
public class RandomUserGenerator {
    private Users_Entity randUser;

    public RandomUserGenerator(Users_Entity user) {
        this.randUser = new Users_Entity();
        
        String passwordString = AuthenticationUtils.generateRandomPassword(5);
        byte[] salt = AuthenticationUtils.getNextSalt();
        byte[] password = AuthenticationUtils.hash(passwordString.toCharArray(), salt);
        
        
        
        randUser.setEmail(AuthenticationUtils.generateRandomPassword(32));
        randUser.setUserId(UUID.randomUUID().toString());
        randUser.setPassword(password);
        randUser.setPasswordSalt(salt);
        randUser.setAvailableEndTime(user.getAvailableEndTime());
        randUser.setAvailableStartTime(user.getAvailableStartTime());
        randUser.setDeviceId(user.getDeviceId());
        randUser.setIsActive(user.getIsActive());
        // Set exteded tris to -1 -  this is used to check if the user is Unonymous
        randUser.setExtendTimeTries(-1);
        randUser.setFirstName("Anonymous");
        randUser.setLastName("User");
        randUser.setPreferredCampus(user.getPreferredCampus());
        randUser.setRoleId(user.getRoleId());
        randUser.setTimeEndedCharging(null);
        randUser.setTimeStartedCharging(null);
        
        //Add 1 second before this user entered queue
        Calendar cal = Calendar.getInstance();
        cal.setTime(user.getTimeEnteredQueue());
        cal.add(Calendar.SECOND, -1);
        System.out.println("OLD USER TIME" + user.getTimeEnteredQueue() + "  NEW USER TIME : " + cal.getTime());
        randUser.setTimeEnteredQueue(cal.getTime());
        
    }

    public Users_Entity getRandUser() {
        return randUser;
    }

    public void setRandUser(Users_Entity randUser) {
        this.randUser = randUser;
    }
    
    
}
