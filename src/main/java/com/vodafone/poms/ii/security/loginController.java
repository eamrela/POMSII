/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.security;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.WebAttributes;



/**
 *
 * @author eamrela
 */
@ManagedBean(name="loginController")
@RequestScoped
public class loginController implements PhaseListener{
 

    
    protected final Log logger = LogFactory.getLog(getClass());
    
    public String doLogin(){
        try { 
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            
            RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
                    .getRequestDispatcher("/login");
            System.out.println("Dispatcher is OK!");
            
            ServletRequest request = (ServletRequest) context.getRequest();
            if(request==null){
                System.out.println("Request is NULL");
            }
           
            ServletResponse response = (ServletResponse) context.getResponse();
            dispatcher.forward(request,
                    response);
            
            FacesContext.getCurrentInstance().responseComplete();
            
        } catch (ServletException | IOException ex) {
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Caught an error while doing Authentication!");
        }
            return null;
    }
    
    @Override
    public void afterPhase(PhaseEvent event) {
    }
    
    @Override
    public void beforePhase(PhaseEvent event) {
        Exception e = (Exception) FacesContext.getCurrentInstance().
          getExternalContext().getSessionMap().get(WebAttributes.AUTHENTICATION_EXCEPTION);
 
        if (e instanceof BadCredentialsException) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                    WebAttributes.AUTHENTICATION_EXCEPTION, null);
            FacesContext.getCurrentInstance().addMessage(null, 
              new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Username or password not valid.", "Username or password not valid"));
        }
    }
    
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
