/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller;

import com.slc.egaugewebsite.utils.ApplicationProperties;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

/**
 *
 * @author Steven Kritikos
 */
@Stateful
public class ValidateCaptchaClient {
    
    private WebTarget webTarget;
    private Client client;
    private String BASE_URI;
    private String SECRET_KEY;
    
    public ValidateCaptchaClient() {
        this.BASE_URI = "https://www.google.com/recaptcha/";
        
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(BASE_URI);
    }
    
    @PostConstruct
    public void init() {
        try {
            this.SECRET_KEY = ApplicationProperties.getApplicationProperties().getProperty("captchasecretkey");
        } catch (IOException ex) {
            Logger.getLogger(ValidateCaptchaClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean validateCaptcha(String captchaResponse) {
        this.webTarget = this.webTarget.path("api").path("siteverify");
        Form form = new Form();
        form.param("secret", this.SECRET_KEY);
        form.param("response", captchaResponse);
        JsonObject jsonObject =  null;
         boolean rtVl = false;
        try {
            jsonObject = webTarget.request().post(Entity.form(form), JsonObject.class);
            rtVl = jsonObject.getBoolean("success");
        } catch (Exception  e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return rtVl;
    }
}
