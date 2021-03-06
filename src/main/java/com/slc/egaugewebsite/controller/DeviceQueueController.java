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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        List<Users_Entity> rtVl;
        String deviceName = DBDeviceNames.getDBName(campus);
        try {
            Device_Entity device = devicedao.getDeviceByName(deviceName);
            // If the campus is Kingston get the kingston queue then add the actual users of the queue for the top;
            if (campus.startsWith("Kingston")) {
                Device_Entity kingston1 = devicedao.getDeviceByName(DBDeviceNames.getDBName("Kingston1"));
                Device_Entity kingston2 = devicedao.getDeviceByName(DBDeviceNames.getDBName("Kingston2"));
                List<Users_Entity> kingstonQueue = usersdao.getQueueByDevice(device);
                if (!kingston1.getUsersList().isEmpty()) {
                    kingstonQueue.add(0, kingston1.getUsersList().get(0));
                }
                if (!kingston2.getUsersList().isEmpty()) {
                    kingstonQueue.add(0, kingston2.getUsersList().get(0));
                }
                for (Users_Entity user : kingstonQueue) {
                    System.out.println(user.getEmail());
                }
                rtVl = kingstonQueue;
            } else {
                rtVl = usersdao.getQueueByDevice(device);  
            }
        } catch (Exception e ) {
            System.out.println(e.toString());
            rtVl = new ArrayList<>();
        }
        return rtVl;
    }
    
    /**
     * Function that gets called periodically to update the queue model; 
     */
    public void updateQueue() {
        try { 
            InstDeviceList devices = ddc.getInstData(new String[0]);
            System.out.println("UPDATING QUEUE");
            if (devices != null) {
                // Get kingston devices to update the Kingston queue   
                InstDevice kingston = devices.getDevices().stream()
                       .filter(device-> device.getDeviceName().equals(DBDeviceNames.KINGSTON_TOTAL.getEntityName())).findFirst().get();
                InstDevice kingston1 = devices.getDevices().stream()
                       .filter(device-> device.getDeviceName().equals(DBDeviceNames.KINGSTON_1.getEntityName())).findFirst().get();
                InstDevice kingston2 = devices.getDevices().stream()
                        .filter(device-> device.getDeviceName().equals(DBDeviceNames.KINGSTON_2.getEntityName())).findFirst().get();
                updateTopOfKingstonQueue(kingston, kingston1);
                updateTopOfKingstonQueue(kingston, kingston2);
                
                //Get cornwall device and update the Cornwall queue
                InstDevice cornwall = devices.getDevices().stream()
                       .filter(device -> device.getDeviceName().equals(DBDeviceNames.CORNWALL.getEntityName())).findFirst().get();
                Device_Entity cornwallEntity = devicedao.getDeviceByName(cornwall.getDeviceName());
                List<Users_Entity> cornwallQueue = usersdao.getQueueByDevice(cornwallEntity);
               if (!cornwallQueue.isEmpty() && cornwallEntity.getIsOnline()) {
                  updateTopOfQueue(cornwall.getInstPower(), cornwallQueue.get(0));
               }
                
                //Get brockville device and update the Brockville queue 
                InstDevice brockville = devices.getDevices().stream()
                       .filter(device -> device.getDeviceName().equals(DBDeviceNames.BROCKVILLE.getEntityName())).findFirst().get();
                Device_Entity brockvilleEntity = devicedao.getDeviceByName(brockville.getDeviceName());
                List<Users_Entity> brockvilleQueue = usersdao.getQueueByDevice(brockvilleEntity);
                if (!brockvilleQueue.isEmpty() && brockvilleEntity.getIsOnline()) {
                    updateTopOfQueue(brockville.getInstPower(), brockvilleQueue.get(0));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(InstantaneousReadingJob.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    /**
     * Function called to assign a user to a Kingston device
     * then updating that users information.
     * @param kingstonTotal
     * @param kingstonDevice 
     */
    private void updateTopOfKingstonQueue(InstDevice kingstonTotal, InstDevice kingstonDevice) {
        Device_Entity kingstonTotalEntity = devicedao.getDeviceByName(kingstonTotal.getDeviceName());
        Device_Entity kingstonEntity = devicedao.getDeviceByName(kingstonDevice.getDeviceName());
        
        if (kingstonEntity.getIsOnline()) {
            // Make sure people exist in kingston queue or are assigned a station
            if (!kingstonTotalEntity.getUsersList().isEmpty() || !kingstonEntity.getUsersList().isEmpty()) {
                // if the device does have an assigned user - assign it a user
                if (kingstonEntity.getUsersList().isEmpty()) {
                    Users_Entity topOfQueue = usersdao.getQueueByDevice(kingstonTotalEntity).get(0);
                    topOfQueue.setDeviceId(kingstonEntity);
                    try {
                        usersdao.edit(topOfQueue);
                        this.updateTopOfQueue(kingstonDevice.getInstPower(), topOfQueue);
                        kingstonEntity.getUsersList().add(topOfQueue);
                    } catch (RollbackFailureException ex) {
                        System.out.println(ex.toString());
                        System.out.println("Failed add user to a Kingston device");
                    } catch (Exception ex) {
                        System.out.println(ex.toString());
                        System.out.println("Failed add user to a Kingston device");
                    } 
                }

                this.updateTopOfQueue(kingstonDevice.getInstPower(), kingstonEntity.getUsersList().get(0));
            }
        }
        
    }
    
    /**
     * Update the status of the user at the top of the queue for given device
     * @param device 
     */
    private void updateTopOfQueue(BigDecimal reading, Users_Entity topOfQueue) {
        try {
            //Check if user is finished charging but is still in queue 
            if (!topOfQueue.getIsActive() && topOfQueue.getTimeEndedCharging() != null) {
                //Means the user is done charging - see when they finished -- and deal approprietly
            } else {
                //Make sure top queue available end time starts
                this.updateUserAvailableTime(topOfQueue); 
                this.checkIfTimeExpired(topOfQueue);
            }
            //Check if user has started charging
            if (reading.compareTo(BigDecimal.valueOf(100)) == 1
                        && !topOfQueue.getIsActive()) { 
                this.userStartedCharging(topOfQueue);
            //Check if current user is done charging
            } else if (reading.compareTo(BigDecimal.valueOf(100)) == -1 
                        && topOfQueue.getIsActive()) {
                //If top of q is anonymous
                if(topOfQueue.getExtendTimeTries() < 0 &&
                        topOfQueue.getFirstName().equals("Anonymous")) {
                    usersdao.destroy(topOfQueue.getUserId());
                    this.notifyNextUserInQueue(topOfQueue);
                } else {                    
                    this.userFinishedCharging(topOfQueue);
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
            user.setExtendTimeTries(0);
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

    /**
     * Update user to charging status
     * @param user 
     */
    private void userStartedCharging(Users_Entity user) {
        try {
            user.setIsActive(true);
            user.setTimeStartedCharging(new Date());
            this.usersdao.edit(user);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * For given user update their status in database and notify them.
     * Also notify the next in queue for the station.
     * @param user 
     */
    public void userFinishedCharging(Users_Entity user) {
        try {
            System.out.println("Top of queue done, emailed them letting them know");
            user.setIsActive(false);
            user.setTimeEndedCharging(new Date());
            this.usersdao.edit(user);
            Users_Entity nextInQueue = this.getNextInQueue(user.getDeviceId());
            ec.sendFinishedChargingEmail(user, nextInQueue);
            ec.notifyNextInQueueStationIsDoneCharging(user, nextInQueue);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * Determine who the next user that needs to be notified is then send them an email
     * @param userEntity 
     */
    public void notifyNextUserInQueue(Users_Entity userEntity) {
        try {
            Device_Entity deviceEntity = userEntity.getDeviceId();
            Users_Entity nextInQueue = this.getNextInQueue(deviceEntity);
            if (nextInQueue != null) {
                this.updateUserAvailableTime(nextInQueue);
                ec.notifyNextInQueueEmail(nextInQueue);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        } 
    }

    public void extendPeriodForUser(Users_Entity userEntity) {
        try {
            if (userEntity.getExtendTimeTries() < 3) {
                Users_Entity randUser = new RandomUserGenerator(userEntity).getRandUser();
                this.usersdao.create(randUser);
                // Update user
                userEntity.setAvailableEndTime(null);
                userEntity.setAvailableStartTime(null);
                userEntity.setExtendTimeTries(userEntity.getExtendTimeTries() + 1);
                userEntity.setIsActive(false);
                userEntity.setTimeStartedCharging(null);
                userEntity.setTimeEndedCharging(null);
                // if the user is queued for kingston place them in the kingston queue
                if (userEntity.getDeviceId().getDeviceName().startsWith("Kingston")) {
                    userEntity.setDeviceId(devicedao.getDeviceByName("Kingston_TotalPower"));
                }
                this.usersdao.edit(userEntity);
            }
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    /**
     * Remove each user from the queue for given campus
     * @param campus 
     */
    public void clearQueueForCampus(String campus) {
        Device_Entity device = devicedao.getDeviceByName(campus);
        List<Users_Entity> queue = usersdao.getQueueByDevice(device);
        queue.stream().forEach(user -> this.removeUserFromQueue(user));
    }
    
    public void setStationOffline(String deviceName) {
        try {
            Device_Entity device = this.devicedao.getDeviceByName(deviceName);
            device.setIsOnline(false);
            this.devicedao.edit(device);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setStationOnline(String deviceName) {
        try {
            Device_Entity device = this.devicedao.getDeviceByName(deviceName);
            device.setIsOnline(true);
            this.devicedao.edit(device);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DeviceQueueController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Users_Entity getNextInQueue(Device_Entity device) {
        Users_Entity nextInQueue = null;
        if (device.getUsersList().size() > 1) {
            List<Users_Entity> queue = usersdao.getQueueByDevice(device);
            nextInQueue = queue.get(1);
        }
        return nextInQueue;
    }
}
