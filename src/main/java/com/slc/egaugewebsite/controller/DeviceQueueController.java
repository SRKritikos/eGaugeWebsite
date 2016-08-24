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
import com.slc.egaugewebsite.utils.RandomUserGenerator;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateful;
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
@Stateful
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
    @EJB
    private EmailController ec;
    public DeviceQueueController() {
    }
    
    
    @PostConstruct
    public void init() {
        this.devicedao = new DeviceDAO(utx, em);
        this.usersdao = new UsersDAO(utx, em);
    }
    
    public void addToQueue(String campus, String userId) {
        String deviceName = DBDeviceNames.getDBName(campus);
        Device_Entity deviceEntity = devicedao.getDeviceByName(deviceName);
        Users_Entity usersEntity = usersdao.findUsers_Entity(userId);
            
        try {
            //Update the user entity with queue info
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
        System.out.println(deviceName);
        Device_Entity device = devicedao.getDeviceByName(deviceName);
        System.out.println(device.getUsersList().size());
        // Get the queue for the given station sorted by the time users entered queue
        return device.getUsersList().stream()
                .sorted((prevUser, curUser) -> prevUser.getTimeEnteredQueue().compareTo(curUser.getTimeEnteredQueue()))
                .collect(Collectors.toList());
    }
    /**
     * Function that gets called periodically to update the queue model; 
     */
    public void updateQueue() {
        try { 
            System.out.println("Updating the queue");
            InstDeviceList devices = ddc.getInstData(new String[0]);
            // Extract individual device dat then update their queue
            if (devices != null) {
                InstDevice kingston1 = devices.getDevices().stream()
                       .filter(device-> device.getDeviceName().equals(DBDeviceNames.KINGSTON_1.getEntityName())).findFirst().get();
               InstDevice kingston2 = devices.getDevices().stream()
                       .filter(device-> device.getDeviceName().equals(DBDeviceNames.KINGSTON_2.getEntityName())).findFirst().get();
               updateTopOfKingstonQueue(kingston1, kingston2);
               InstDevice cornwall = devices.getDevices().stream()
                       .filter(device -> device.getDeviceName().equals(DBDeviceNames.CORNWALL.getEntityName())).findFirst().get();
                updateTopOfQueue(cornwall);
                InstDevice brockville = devices.getDevices().stream()
                       .filter(device -> device.getDeviceName().equals(DBDeviceNames.BROCKVILLE.getEntityName())).findFirst().get();
                updateTopOfQueue(brockville);
            }
        } catch (Exception ex) {
            Logger.getLogger(InstantaneousReadingJob.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    private void updateTopOfKingstonQueue(InstDevice device1, InstDevice device2 ) {

    }
    
    /**
     * Update the status of the user at the top of the queue for given device
     * @param device 
     */
    private void updateTopOfQueue(InstDevice device) {
        try {
            Device_Entity deviceEntity = devicedao.getDeviceByName(device.getDeviceName());
            System.out.println(device.getDeviceName() + "  " + device.getInstPower().toString());
            List<Users_Entity> queue = deviceEntity.getUsersList();
            if (!queue.isEmpty()) { 
                Users_Entity topOfQueue = queue.get(0);
                //  Check if user is finished charging but is still in queue 
                if (!topOfQueue.getIsActive() && topOfQueue.getTimeEndedCharging() != null) {
                    //Means the user is done charging - see when they finished -- and deal approprietly
                } else {
                    //Make sure top queue available end time starts
                    this.updateUserAvailableTime(topOfQueue); 
                    this.checkIfTimeExpired(topOfQueue);
                }
                
                // Check if user has started charging
                if (device.getInstPower().compareTo(BigDecimal.valueOf(100)) == 1
                            && !topOfQueue.getIsActive()) { 
                    this.userStartedCharging(topOfQueue);
                // Check if current user is done charging
                } else if (device.getInstPower().compareTo(BigDecimal.valueOf(100)) == -1 
                            && topOfQueue.getIsActive()) {
                    this.userFinishedCharing(topOfQueue);
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
            user.setAvailableStartTime(null);
            user.setTimeStartedCharging(null);
            user.setTimeEndedCharging(null);
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

    /**
     * Set users available start and end times
     * @param user 
     */
    public void updateUserAvailableTime(Users_Entity user) {   
        try {
            if (user.getAvailableStartTime() == null) {
                // Set the available date times
                Calendar cal = Calendar.getInstance();
                user.setAvailableStartTime(new Date());
                cal.setTime(new Date());
                cal.add(Calendar.HOUR, 1);
                user.setAvailableEndTime(cal.getTime());
                usersdao.edit(user);
            }
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *  Remove user from queue if their time has expired.
     * @param user 
     */
    public void checkIfTimeExpired(Users_Entity user) {
        if (!user.getIsActive()) {
               Calendar start = Calendar.getInstance();
               Calendar end = Calendar.getInstance();
               start.setTime(user.getAvailableStartTime());
               end.setTime(user.getAvailableEndTime());
               if (start.after(end)){
                   removeUserFromQueue(user);
            }
        }
    }

    private void userStartedCharging(Users_Entity user) {
        try {
            System.out.println("Top of q started charging");
            user.setIsActive(true);
            user.setTimeStartedCharging(new Date());
            this.usersdao.edit(user);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void userFinishedCharing(Users_Entity user) {
        try {
            System.out.println("Top of q done emailed them letting them know");
            user.setIsActive(false);
            user.setTimeEndedCharging(new Date());
            this.usersdao.edit(user);
            ec.sendFinishedChargingEmail(user);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public void updateNextUserInQueue(Users_Entity userEntity) {
        try {
            Device_Entity deviceEntity = userEntity.getDeviceId();
            if (deviceEntity.getUsersList().size() > 1) {
                Users_Entity nextInQueue = deviceEntity.getUsersList().get(1);
                this.updateUserAvailableTime(nextInQueue);
                System.out.println("NEXT IN QUEUE: " + nextInQueue.getEmail() );
                ec.notifyNextInQueueEmail(nextInQueue);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
         
        
    }

    public void extendPeriodForUser(Users_Entity userEntity) {
        try {
            System.out.println("EXTENDING PERIOD FOR USER");
            if (userEntity.getExtendIimeTries() < 3) {
                Users_Entity randUser = new RandomUserGenerator(userEntity).getRandUser();
                this.usersdao.create(randUser);
                // Update new user
                userEntity.setAvailableEndTime(null);
                userEntity.setAvailableStartTime(null);
                userEntity.setExtendIimeTries(userEntity.getExtendIimeTries() + 1);
                userEntity.setIsActive(false);
                userEntity.setTimeStartedCharging(null);
                this.usersdao.edit(userEntity);
            }
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
