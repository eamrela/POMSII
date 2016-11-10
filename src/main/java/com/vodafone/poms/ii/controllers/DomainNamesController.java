package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.DomainNames;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.DomainNamesFacade;

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

@Named("domainNamesController")
@SessionScoped
public class DomainNamesController implements Serializable {

    @EJB
    private com.vodafone.poms.ii.beans.DomainNamesFacade ejbFacade;
    private List<DomainNames> items = null;
    private DomainNames selected;

    public DomainNamesController() {
    }

    public DomainNames getSelected() {
        return selected;
    }

    public void setSelected(DomainNames selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DomainNamesFacade getFacade() {
        return ejbFacade;
    }

    public DomainNames prepareCreate() {
        selected = new DomainNames();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DomainNamesCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DomainNamesUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DomainNamesDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<DomainNames> getItems() {
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

    public DomainNames getDomainNames(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<DomainNames> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<DomainNames> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter("DomainNamesControllerConverter")
    public static class DomainNamesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DomainNamesController controller = (DomainNamesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "domainNamesController");
            return controller.getDomainNames(getKey(value));
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
            if (object instanceof DomainNames) {
                DomainNames o = (DomainNames) object;
                return getStringKey(o.getDomainName());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DomainNames.class.getName()});
                return null;
            }
        }

    }

}
