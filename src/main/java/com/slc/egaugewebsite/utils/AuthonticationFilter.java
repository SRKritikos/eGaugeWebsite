/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.utils;

import com.slc.egaugewebsite.controller.beans.UserBean;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Steven Kritikos
 */
@WebFilter(filterName="AuthFilter", urlPatterns = ("*.xhtml"))
public class AuthonticationFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // empty init method
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpSession session = httpRequest.getSession(true);

            String requestURI = httpRequest.getRequestURI();
            String user = (String)session.getAttribute("userId");
            String userRole = (String) session.getAttribute("userRole");
            if ((requestURI.indexOf("/user.xhtml") >= 0 || requestURI.indexOf("/admin.xhtml")  >= 0) && (session == null || user == null)) {
                httpResponse.sendRedirect("/eGaugeWebsite/views/index.xhtml");
            } else if (requestURI.indexOf("/admin.xhtml") >= 0 && userRole.equals(UserRole.defaultuser.name())) {
                httpResponse.sendRedirect("/eGaugeWebsite/views/index.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    @Override
    public void destroy() {
        //Empty destroy method
    }

}
