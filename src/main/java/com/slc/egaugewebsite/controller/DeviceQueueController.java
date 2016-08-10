/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller;

import com.slc.egaugewebsite.data.dao.DeviceDAO;
import com.slc.egaugewebsite.data.dao.UsersDAO;
import com.slc.egaugewebsite.data.dao.exceptions.RollbackFailureException;
import com.slc.egaugewebsite.data.entities.Device_Entity;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.model.InstDevice;
import com.slc.egaugewebsite.model.InstDeviceList;
import com.slc.egaugewebsite.utils.DBDeviceNames;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.UserTransaction;

/**
 *
 * @author Steven Kritikos
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class DeviceQueueController {
    @Resource
    private UserTransaction utx;
    @PersistenceContext(unitName = "com.slc_eGaugeWebsite_war_1.2-SNAPSHOTPU", type = PersistenceContextType.TRANSACTION)
    private EntityManager em;
    private UsersDAO usersdao;
    private DeviceDAO devicedao;
    @EJB
    private DeviceDataClient ddc;
    public DeviceQueueController() {
    }
    
    
    @PostConstruct
    public void init() {
        this.devicedao = new DeviceDAO(utx, em);
        this.usersdao = new UsersDAO(utx, em);
    }
    
    public void addToQueue(String campus, String userId) {
        // TODO make sure the user is not in queue -  if they are queueued for another campus ask if they want to queue for a different station.
        String deviceName = DBDeviceNames.getDBName(campus);
        Device_Entity deviceEntity = devicedao.getDeviceByName(deviceName);
        Users_Entity usersEntity = usersdao.findUsers_Entity(userId);
            
        try {
            // Update the user entity with queue info
            usersEntity.setDeviceId(deviceEntity);
            usersEntity.setTimeEnteredQueue(new Date());
            usersEntity.setIsActive(false);
            this.usersdao.edit(usersEntity);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public List<Users_Entity> getQueueByStation(String campus) {
        String deviceName = DBDeviceNames.getDBName(campus);
        Device_Entity device = devicedao.getDeviceByName(deviceName);
        // Get the queue for the given station then build a list of table row models for the table;
        return device.getUsersList();
    }
    
    public void updateQueue() {
        try { 
            InstDeviceList devices = ddc.getInstData(new String[0]);
            // Extract individual device dat then update their queue
            if (devices != null) {
                InstDevice kingston1 = devices.getDevices().stream()
                       .filter(device-> device.getDeviceName().equals(DBDeviceNames.KINGSTON_1.getEntityName())).findFirst().get();
               InstDevice kingston2 = devices.getDevices().stream()
                       .filter(device-> device.getDeviceName().equals(DBDeviceNames.KINGSTON_2.getEntityName())).findFirst().get();
               updateKingstonQueue(kingston1, kingston2);
            }
        } catch (Exception ex) {
            Logger.getLogger(InstantaneousReadingJob.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    private void updateKingstonQueue(InstDevice device1, InstDevice device2 ) {

    }
    
    private void updateDeviceQueue(InstDevice device) {
        Device_Entity deviceEntity = devicedao.getDeviceByName(device.getDeviceName());
        List<Users_Entity> queue = deviceEntity.getUsersList();
        try {
            if (queue != null) { 

                if(queue.size() == 1) {
                    if (device.getInstPower().compareTo(BigDecimal.valueOf(100)) == 1
                            && !queue.get(0).getIsActive()) {   
                        userStartedCharging(queue.get(0));
                    } else {
                        updateNextInQueue(queue.get(0));
                    }
                } else {
                    Users_Entity topOfQueue = queue.get(0);
                    Users_Entity nextInLine = queue.get(1);
                    // Check if current queued user is done charging
                    if (device.getInstPower().compareTo(BigDecimal.valueOf(100)) == -1 
                            && topOfQueue.getIsActive()) {
                        //Email telling them they are done/ready for next person,
                        removeUserFromQueue(topOfQueue);
                        updateNextInQueue(nextInLine);
                    // else check if next in line is charging
                    } else if (device.getInstPower().compareTo(BigDecimal.valueOf(100)) == 1
                            && !topOfQueue.getIsActive()) {   
                        userStartedCharging(topOfQueue);
                    }
                }
            } 
        }catch (Exception e) {
            System.out.println(e.toString());   
        }
        
    }

    public void removeUserFromQueue(Users_Entity user) {
        try {
            // Set all queue related fields to null
            user.setAvailableEndTime(null);
            user.setAvailableEndTime(null);
            user.setAvailaleStartTime(null);
            user.setExtendIimeTries(0);
            user.setDeviceId(null);
            user.setIsActive(false);
            user.setTimeEnteredQueue(null);
            usersdao.edit(user);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateNextInQueue(Users_Entity user) {   
        try {
            boolean edit = true;
            if (user.getAvailaleStartTime() == null) {
                // Set the available date times
                Calendar cal = Calendar.getInstance();
                user.setAvailaleStartTime(new Date());
                cal.setTime(new Date());
                cal.add(Calendar.HOUR, 1);
                user.setAvailableEndTime(cal.getTime());
              // If the user is not active remove them from queue if they have pass their given time
            } else if (!user.getIsActive()) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                start.setTime(user.getAvailaleStartTime());
                end.setTime(user.getAvailableEndTime());
                if (start.after(end)){
                    edit = false;
                }
            }
            
            if (edit) {
                usersdao.edit(user);
            } else {
                removeUserFromQueue(user);
            }

        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void userStartedCharging(Users_Entity user) {
        user.setIsActive(true);
        updateNextInQueue(user);
    }
    

}
