/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.validators;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Steven Kritikos
 */
@FacesValidator("loginvalidator")
public class LoginValidator implements Validator{
    /**
     * Validates the users login credentials - if no errors are thrown login the user 
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String enteredEmail = value.toString();
        // Get the UIInput of enteredPassword to then get the entered password
        String enteredPassword = (String) ((UIInput) component.getAttributes().get("enteredPassword")).getSubmittedValue();
        System.out.println(enteredEmail + "  " + enteredPassword);
        
        if (enteredEmail == null || enteredEmail.isEmpty() || enteredPassword == null || enteredPassword.isEmpty() ) {
            return;
        }
    }
}
