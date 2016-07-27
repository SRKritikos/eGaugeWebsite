/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.quartz;

import com.slc.egaugewebsite.controller.DeviceDataClient;
import com.slc.egaugewebsite.controller.beans.DeviceDataModelBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.quartz.DateBuilder;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.newJob;

/**
 *
 * @author Steven Kritikos
 */
@WebListener
public class DeviceDataScheduler implements ServletContextListener{
private Scheduler scheduler = null;
    
    /**
     * Schedule job to update the device data model
     * 
     * @param sce 
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
       try {
                System.out.println("Sheduler started");
                SchedulerFactory sf = new StdSchedulerFactory();
                scheduler = sf.getScheduler();
                scheduler.start();
                
                JobDetail job =  newJob(DeviceDataClient.class).withIdentity("instJob").build();
                
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1")
                        .startAt(DateBuilder.futureDate(10, DateBuilder.IntervalUnit.SECOND))
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1).repeatForever()).build();
              
               
               //scheduler.scheduleJob(job, trigger);

               
            } catch (SchedulerException ex) {
            Logger.getLogger(DeviceDataScheduler.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception thrown in scheduler function");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       if (scheduler !=  null) {
           try {
               scheduler.shutdown();
           } catch (SchedulerException ex) {
               Logger.getLogger(DeviceDataScheduler.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
    }  
            
}
