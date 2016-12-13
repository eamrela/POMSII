package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.Sites;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.SitesFacade;
import com.vodafone.poms.ii.helpers.SiteLoader;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.event.FileUploadEvent;

@Named("sitesController")
@SessionScoped
public class SitesController implements Serializable {

    @EJB
    private com.vodafone.poms.ii.beans.SitesFacade ejbFacade;
    private List<Sites> items = null;
    private Sites selected;

    public SitesController() {
    }
    public void resetSelection(){
        selected= null;
    }

    public Sites getSelected() {
        return selected;
    }

    public void setSelected(Sites selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SitesFacade getFacade() {
        return ejbFacade;
    }

    public Sites prepareCreate() {
        selected = new Sites();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SitesCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SitesUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SitesDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Sites> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Sites getSites(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Sites> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Sites> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter("SitesControllerConverter")
    public static class SitesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SitesController controller = (SitesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sitesController");
            return controller.getSites(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Sites) {
                Sites o = (Sites) object;
                return getStringKey(o.getSitePhysical());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Sites.class.getName()});
                return null;
            }
        }

    }

    public List<Sites> autoCompleteSite(String query) {
        List<Sites> allSites = getItems();
        List<Sites> filteredSites = new ArrayList<>();
         
        for (int i = 0; i < allSites.size(); i++) {
            Sites code = allSites.get(i);
            if(code.getSitePhysical().toLowerCase().contains(query)) {
                filteredSites.add(code);
            }
        }      
        return filteredSites;
    }
    
    public void uploadSites(FileUploadEvent event){
        try {
            List<Sites> uploadedSites=SiteLoader.readFile(event.getFile().getInputstream());
            if(uploadedSites!=null?uploadedSites.size()>0:false){
                persistAll(uploadedSites);
                JsfUtil.addSuccessMessage(uploadedSites.size()+" Sites Successfully Uploaded.");
            }else{
                JsfUtil.addErrorMessage("No Site Items Found");
                
            }
        } catch (IOException ex) {
            Logger.getLogger(ActivityCodeController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, "File Upload Failed");
        }
        
    }
    
    private void persistAll(List<Sites> list){
        if (list!=null?list.size()>0:false) {
            try {
                for (int i = 0; i < list.size(); i++) {
                    getFacade().edit(list.get(i));
                }
                items=null;
                JsfUtil.addSuccessMessage("Bulk upload succeeded");
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }else{
            JsfUtil.addErrorMessage("No Sites found to update!");
            
        }
    }
}
