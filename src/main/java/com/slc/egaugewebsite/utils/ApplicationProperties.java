/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *Utilities class used to retrieve the application's properties file 
 * 
 * @author Steven Kritikos
 */
@Singleton
@Startup
public class ApplicationProperties {
    private static Properties properties;
    private static String fileName;

    public ApplicationProperties() {
        ApplicationProperties.fileName = "application.properties";
        ApplicationProperties.properties = new Properties();
    }
    
    public static Properties getApplicationProperties() throws IOException {
        InputStream input;
        input = ApplicationProperties.class.getClassLoader().getResourceAsStream(ApplicationProperties.fileName);
        
        if (input != null) {
            ApplicationProperties.properties.load(input);
            input.close();
        } else {
            throw new FileNotFoundException("Application properties files not found");
        }
        return ApplicationProperties.properties; 
    }
    
}
