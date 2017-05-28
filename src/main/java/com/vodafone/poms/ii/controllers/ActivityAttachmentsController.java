package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.ActivityAttachments;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.ActivityAttachmentsFacade;

import java.io.Serializable;
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

@Named("activityAttachmentsController")
@SessionScoped
public class ActivityAttachmentsController implements Serializable {

    @EJB
    private com.vodafone.poms.ii.beans.ActivityAttachmentsFacade ejbFacade;
    private List<ActivityAttachments> items = null;
    private ActivityAttachments selected;

    public ActivityAttachmentsController() {
    }

    public ActivityAttachments getSelected() {
        return selected;
    }

    public void setSelected(ActivityAttachments selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ActivityAttachmentsFacade getFacade() {
        return ejbFacade;
    }

    public ActivityAttachments prepareCreate() {
        selected = new ActivityAttachments();
        initializeEmbeddableKey();
        return selected;
    }

    public ActivityAttachments create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle10").getString("ActivityAttachmentsCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle10").getString("ActivityAttachmentsUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle10").getString("ActivityAttachmentsDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ActivityAttachments> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private ActivityAttachments persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    selected = getFacade().merge(selected);
                    
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
                return selected;
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle10").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle10").getString("PersistenceErrorOccured"));
            }
        }
            return null;
    }

    public ActivityAttachments getActivityAttachments(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ActivityAttachments> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ActivityAttachments> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ActivityAttachments.class)
    public static class ActivityAttachmentsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ActivityAttachmentsController controller = (ActivityAttachmentsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "activityAttachmentsController");
            return controller.getActivityAttachments(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ActivityAttachments) {
                ActivityAttachments o = (ActivityAttachments) object;
                return getStringKey(o.getAttachmentId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ActivityAttachments.class.getName()});
                return null;
            }
        }

    }

}
