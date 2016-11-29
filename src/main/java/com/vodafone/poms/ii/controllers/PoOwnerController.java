package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.PoOwner;
import com.vodafone.poms.ii.beans.PoOwnerFacade;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;


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

@Named("poOwnerController")
@SessionScoped
public class PoOwnerController implements Serializable {

    @EJB
    private com.vodafone.poms.ii.beans.PoOwnerFacade ejbFacade;
    private List<PoOwner> items = null;
    private PoOwner selected;

    public PoOwnerController() {
    }

    public PoOwner getSelected() {
        return selected;
    }

    public void setSelected(PoOwner selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PoOwnerFacade getFacade() {
        return ejbFacade;
    }

    public PoOwner prepareCreate() {
        selected = new PoOwner();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PoOwnerCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PoOwnerUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PoOwnerDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PoOwner> getItems() {
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

    public PoOwner getPoOwner(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<PoOwner> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PoOwner> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter("PoOwnerControllerConverter")
    public static class PoOwnerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PoOwnerController controller = (PoOwnerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "poOwnerController");
            return controller.getPoOwner(getKey(value));
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
            if (object instanceof PoOwner) {
                PoOwner o = (PoOwner) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PoOwner.class.getName()});
                return null;
            }
        }

    }

}
