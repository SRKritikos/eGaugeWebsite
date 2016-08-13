/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Quatz job to handle updating the device queue 
 * 
 * @author Steven Kritikos
 */
public class InstantaneousReadingJob implements Job {
  
    
    public InstantaneousReadingJob() {
        
    }    
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        DeviceQueueController dqc = null;
        try {
            // Use JNDI namespace to get the queue EJB in the applicaton's context.
            dqc = (DeviceQueueController) InitialContext.doLookup("java:global/eGaugeWebsite/DeviceQueueController");
            dqc.updateQueue();
        } catch (NamingException ex) {
            Logger.getLogger(InstantaneousReadingJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    
