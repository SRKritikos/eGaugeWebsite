/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller;

import com.slc.egaugewebsite.data.dao.UsersDAO;
import com.slc.egaugewebsite.data.dao.exceptions.RollbackFailureException;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.utils.AuthenticationUtils;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
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
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class EmailController {
    private final String HOST_EMAIL;
    private final String HOST_PASSWORD;
    private Message emailMessage;
    private Properties props;
    @Resource
    private UserTransaction utx;
    @PersistenceContext(unitName = "com.slc_eGaugeWebsite_war_1.2-SNAPSHOTPU", type = PersistenceContextType.TRANSACTION)
    private EntityManager em;
    private UsersDAO usersdao;
    
    public EmailController() {
        this.HOST_EMAIL = "slcegauge@gmail.com";
        this.HOST_PASSWORD = "egaugeemail123";
        this.props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(HOST_EMAIL, HOST_PASSWORD);
            }
        });
        this.emailMessage = new MimeMessage(session);
    }
    
    @PostConstruct
    public void init() {
        this.usersdao = new UsersDAO(utx, em);
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
            this.emailMessage.setText(bodyText);
            
            Transport.send(this.emailMessage);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void sendPasswordResetEmail(Users_Entity user){
        //TODO get address of the email that will send auttomated messages
        String newPassword = AuthenticationUtils.generateRandomPassword(12);
        byte[] newHashedPassword = AuthenticationUtils.hash(newPassword.toCharArray(), user.getPasswordSalt());
        String subject = "Password Reset";
        String body = "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n\n\n"
                + "Your new password is " + newPassword + ".\n"
                + "Follow the link and login with your new password " 
                + "localhost:8080/eGaugeWebService/faces/index.xhtml";
        
        try {
            // Update the user's password if nothing thrown send email to user about their password change
            user.setPassword(newHashedPassword);
            this.usersdao.edit(user);
            this.sendEmail(this.HOST_EMAIL, user.getEmail(), subject, body);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
