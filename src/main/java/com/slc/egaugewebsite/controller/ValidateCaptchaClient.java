/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller;

import javax.ejb.Singleton;
import javax.ejb.Startup;
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
@Singleton
@Startup
public class ValidateCaptchaClient {
    
    private WebTarget webTarget;
    private Client client;
    private final String BASE_URI = "https://www.google.com/recaptcha/";
    private final String SECRET_KEY = "6LfKAycTAAAAAE72gXt4hJsmJZPyadep2roeRFlx";
    
    public ValidateCaptchaClient() {
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(BASE_URI);
    }
    
    
    public boolean validateCaptcha(String captchaResponse) {
        this.webTarget = this.webTarget.path("api").path("siteverify");
        Form form = new Form();
        form.param("secret", this.SECRET_KEY);
        form.param("response", captchaResponse);
        JsonObject jsonObject =  null;
        try {
            jsonObject = webTarget.request().post(Entity.form(form), JsonObject.class);
        } catch (Exception  e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        boolean rtVl = jsonObject.getBoolean("success");
        return rtVl;
    }
}
