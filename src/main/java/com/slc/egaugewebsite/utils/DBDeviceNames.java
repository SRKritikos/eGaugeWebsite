/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slc.egaugewebsite.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum to handle returning database stored device names for queries.
 * 
 * 
 * @author Steven
 */
public enum DBDeviceNames {
    KINGSTON_1("Kingston_Wand1_Power"),
    KINGSTON_2("Kingston_Wand2_Power"),
    KINGSTON_TOTAL("Kingston_TotalPower"),
    CORNWALL("Cornwall_Power"),
    BROCKVILLE("Brockville_Power");
    
    private String entityName;
    private DBDeviceNames(String name) {
        this.entityName = name;
    }
    
    public String getEntityName() {
        return this.entityName;
    }
    
    /**
     * Get the database stored name of given device name
     * 
     * @param name
     * @return 
     */
    public static String getDBName(String name) {
        String rtVl = null;
        if (name != null) {
            switch (name.toUpperCase()) {
                case "KINGSTON1" : 
                    rtVl = KINGSTON_1.entityName;
                    break;
                case "KINGSTON2" : 
                    rtVl = KINGSTON_2.entityName;
                    break;
                case "KINGSTON":
                    rtVl = KINGSTON_TOTAL.entityName;
                    break;
                case "CORNWALL":
                    rtVl =  CORNWALL.entityName;
                    break;
                case "BROCKVILLE":
                    rtVl =  BROCKVILLE.entityName;
                    break;
            }   
        }
        return rtVl;
    }
    
    /**
     * Given list of campus names return list of DB device names or null for all DB device names
     * @param campus
     * @return 
     */
    public static List<String> getCampusDBNames(List<String> campus) {
        List<String> rtVl = new ArrayList<>();
        if (campus == null) {
            for (DBDeviceNames device : DBDeviceNames.values()) {
                rtVl.add(device.entityName);
            }
        } else {
            for (String c : campus) {
                rtVl.add(DBDeviceNames.getDBName(c));
            }
        }                     
        return rtVl;
    }
}
