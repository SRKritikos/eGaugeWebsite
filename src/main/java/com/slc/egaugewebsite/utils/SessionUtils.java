/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Steven Kritikos

 */
public class SessionUtils {
    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
            .getExternalContext().getSession(false);
     }

     public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
            .getExternalContext().getRequest();
     }

     public static String getEmail() {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
            return session.getAttribute("email").toString();
     }

     public static String getUserId() {
             HttpSession session = getSession();
             if (session != null)
                     return (String) session.getAttribute("userid");
             else
                     return null;
     }
     
      public static String getUserRole() {
             HttpSession session = getSession();
             if (session != null)
                     return (String) session.getAttribute("userRole");
             else
                     return null;
     }
}
