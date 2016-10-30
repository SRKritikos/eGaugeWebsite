/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller;

import com.slc.egaugewebsite.data.dao.UsersDAO;
import com.slc.egaugewebsite.data.dao.exceptions.RollbackFailureException;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.utils.ApplicationProperties;
import com.slc.egaugewebsite.utils.AuthenticationUtils;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
public class EmailController {
    private String HOST_EMAIL;
    private String HOST_PASSWORD;
    private Message emailMessage;
    private Properties props;
    @Resource
    private UserTransaction utx;
    @PersistenceContext(unitName = "com.slc_eGaugeWebsite_war_1.2-SNAPSHOTPU", type = PersistenceContextType.TRANSACTION)
    private EntityManager em;
    private UsersDAO usersdao;
    private Properties appproperties;
    private final String BASE_URL = "http://localhost:8080/egauge/views/";
    
    public EmailController() {
    }
    
    @PostConstruct
    public void init() {
        this.usersdao = new UsersDAO(utx, em);
        try {
            this.appproperties = ApplicationProperties.getApplicationProperties();
            this.HOST_EMAIL = this.appproperties.getProperty("hostemail");
            this.HOST_PASSWORD = this.appproperties.getProperty("hostpassword");
            this.props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.trust", this.appproperties.getProperty("smtphost"));
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", this.appproperties.getProperty("smtphost"));
            props.put("mail.smtp.port", this.appproperties.getProperty("smtpport"));
            
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(HOST_EMAIL, HOST_PASSWORD);
                        }
                    });
            this.emailMessage = new MimeMessage(session);
        } catch (IOException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
       
    }
    
    
    /**
     * Function to be called when e-mail needs to be sent through the application
     * 
     * @param from
     * @param to
     * @param subject
     * @param bodyText 
     */
    public void sendEmail(String from, String to, String subject, String bodyText) {
        try {
            this.emailMessage.setFrom(new InternetAddress(from));
            this.emailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            this.emailMessage.setSubject(subject);
            this.emailMessage.setContent(bodyText, "text/html; charset=utf-8");
            Transport.send(this.emailMessage);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void sendPasswordResetEmail(Users_Entity user) throws Exception{
        //TODO get address of the email that will send auttomated messages
        String newPassword = AuthenticationUtils.generateRandomPassword(12);
        byte[] newHashedPassword = AuthenticationUtils.hash(newPassword.toCharArray(), user.getPasswordSalt());
        String subject = "Password Reset";
        String body = "Dear " + user.getFirstName() + " " + user.getLastName() + ",<br/><br/><br/>"
                + "Your new password is " + newPassword + ".<br/>"
                + "<a href=" + this.BASE_URL + "'passwordreset.xhtml'>Click Here</a>"
                + " to reset your password";
        
        try {
            // Update the user's password if nothing thrown send email to user about their password change
            user.setPassword(newHashedPassword);
            this.usersdao.edit(user);
            this.sendEmail(this.HOST_EMAIL, user.getEmail(), subject, body);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("failure");
        } catch (Exception ex) {
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("failure");
        }  
    }
    
    public void sendFinishedChargingEmail(Users_Entity topOfQueue, Users_Entity nextInQueue) {
        String subject = "Vehicle finished charging";
        String body = "Dear " + topOfQueue.getFirstName() + " " + topOfQueue.getLastName() + ",<br/><br/><br/>"
                + "Your car has finished charging and is ready to be picked up.<br/>"
                + "Please <a href='" + this.BASE_URL + "remove.xhtml?id=" + topOfQueue.getUserId() + "'>click here</a> "
                + " to remove yourself from the queue and to notify the next person in queue that the station is now available.";
                if (nextInQueue != null) {
                    body += "<br/><a href='" + this.BASE_URL + "views/emailmessage.xhtml?toId=" + 
                    nextInQueue.getUserId() + "&fromId=" + topOfQueue.getUserId() + "'>Click here</a>"
                    + " if you would like to send the user that's next in queue a message.";
                }
        try {
            this.sendEmail(this.HOST_EMAIL, topOfQueue.getEmail(), subject, body);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
                
    }
    
    public void notifyNextInQueueEmail(Users_Entity user) {
        String subject = "Charging station now available";
        String body = "Dear " + user.getFirstName() + " " + user.getLastName() + ",<br/><br/><br/>"
                + "The electric vehicle station you queued for is now avaible.<br/>"
                + "You have one hour to start charging or you will forfeit your spot.";
        
        try {
            this.sendEmail(this.HOST_EMAIL, user.getEmail(), subject, body);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void notifyNextInQueueStationIsDoneCharging(Users_Entity topOfQueue, Users_Entity nextInQueue) {
        String subject = "Charging station finished charging";
        String body = "Dear " + nextInQueue.getFirstName() + " " + nextInQueue.getLastName() + ",<br/><br/><br/>"
                + "The user ahead of you has finished charging their vehicle.<br/>"
                + "You will be notified when the station becomes available.<br/>"
                + "<a href='" + this.BASE_URL + "emailmessage.xhtml?toId=" + 
                topOfQueue.getUserId() + "&fromId=" + nextInQueue.getUserId() + "'>click here</a>"
                + " if you would like to send the user ahead of you a message.";
        try {
            this.sendEmail(this.HOST_EMAIL, nextInQueue.getEmail(), subject, body);
        } catch (Exception e) {
            System.out.println(e.toString());
        } 
    }
}
