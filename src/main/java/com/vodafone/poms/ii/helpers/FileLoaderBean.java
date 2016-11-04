/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.helpers;


import com.vodafone.poms.ii.controllers.ActivityCodeController;
import com.vodafone.poms.ii.controllers.SitesController;
import com.vodafone.poms.ii.entities.ActivityCode;
import com.vodafone.poms.ii.entities.Sites;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Amr
 */
@Named("fileUploader")
@SessionScoped
public class FileLoaderBean implements Serializable{
  
    @Inject
    private ActivityCodeController activityController;
    @Inject
    private SitesController siteController;

    public void upload(FileUploadEvent event) {
        String source = FacesContext.getCurrentInstance().
		getExternalContext().getRequestParameterMap().get("source");
        if(event.getFile() != null) {
            if(source.equals("SITES")){
                try {
                    List<Sites> sites = SiteLoader.readFile(event.getFile().getInputstream());
                    for (Sites site : sites) {
                        siteController.setSelected(site);
                        siteController.create();
                    }
                    siteController.setSelected(null);
                     
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("File Uploaded"));
                } catch (IOException ex) {
                    Logger.getLogger(FileLoaderBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(source.equals("ACTIVITY")){
                try { 
                    List<ActivityCode> activities = ActivityCodeLoader.readFile(event.getFile().getInputstream());
                    for (ActivityCode activity : activities) {
                        activityController.setSelected(activity);
                        activityController.create();
                    }
                    activityController.setSelected(null);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("File Uploaded"));
                } catch (IOException ex) {
                    Logger.getLogger(FileLoaderBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
