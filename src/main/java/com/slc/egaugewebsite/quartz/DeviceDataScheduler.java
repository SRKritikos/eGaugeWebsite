/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.quartz;

import com.slc.egaugewebsite.controller.InstantaneousReadingJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.quartz.DateBuilder;
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
 * @author Steven Kritikos
 */
@Singleton
@Startup
@DependsOn("DeviceDataClient")
public class DeviceDataScheduler{
    private Scheduler scheduler = null;
    
    
    /**
     * Schedule job to update the device data model
     * 
     * @param sce 
     */
    @PostConstruct
    public void contextInitialized() {
       try {
                SchedulerFactory sf = new StdSchedulerFactory();
                scheduler = sf.getScheduler();
                scheduler.start();
                
                JobDetail job =  newJob(InstantaneousReadingJob.class).withIdentity("instJob").build();
                
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1")
                        .startAt(DateBuilder.futureDate(10, DateBuilder.IntervalUnit.SECOND))
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1).repeatForever()).build();
              
               
               scheduler.scheduleJob(job, trigger);

               
            } catch (SchedulerException ex) {
            Logger.getLogger(InstantaneousReadingJob.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception thrown in scheduler function");
        }
    }
            
}
