/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.controller.validators;

import javax.faces.application.FacesMessage;
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

@FacesValidator("matchingpasswordvalidator")
public class MatchingPasswordValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String newPassword =  value.toString();
        if (newPassword.length() < 6) {
            throw new ValidatorException(new FacesMessage("Password must be 6 or more characters"));
        } 
        String confirmPassword = (String) ((UIInput) component.getAttributes().get("confirmPassword")).getSubmittedValue();
       
        if (!newPassword.equals(confirmPassword)) {
            throw new ValidatorException(new FacesMessage("Password fields don't match"));
        }
    }
    
}
