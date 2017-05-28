package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.Configurations;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.ConfigurationsFacade;

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

@Named("configurationsController")
@SessionScoped
public class ConfigurationsController implements Serializable {

    @EJB
    private com.vodafone.poms.ii.beans.ConfigurationsFacade ejbFacade;
    private List<Configurations> items = null;
    private Configurations selected;

    public ConfigurationsController() {
    }

    public Configurations getSelected() {
        return selected;
    }

    public void setSelected(Configurations selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
        selected.setConfigurationsPK(new com.vodafone.poms.ii.entities.ConfigurationsPK());
    }

    private ConfigurationsFacade getFacade() {
        return ejbFacade;
    }

    public Configurations prepareCreate() {
        selected = new Configurations();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle6").getString("ConfigurationsCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle6").getString("ConfigurationsUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle6").getString("ConfigurationsDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Configurations> getItems() {
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle6").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle6").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Configurations getConfigurations(com.vodafone.poms.ii.entities.ConfigurationsPK id) {
        return getFacade().find(id);
    }

    public List<Configurations> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Configurations> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Configurations.class)
    public static class ConfigurationsControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConfigurationsController controller = (ConfigurationsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "configurationsController");
            return controller.getConfigurations(getKey(value));
        }

        com.vodafone.poms.ii.entities.ConfigurationsPK getKey(String value) {
            com.vodafone.poms.ii.entities.ConfigurationsPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.vodafone.poms.ii.entities.ConfigurationsPK();
            key.setConfName(values[0]);
            key.setConfEnviroment(values[1]);
            return key;
        }

        String getStringKey(com.vodafone.poms.ii.entities.ConfigurationsPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getConfName());
            sb.append(SEPARATOR);
            sb.append(value.getConfEnviroment());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Configurations) {
                Configurations o = (Configurations) object;
                return getStringKey(o.getConfigurationsPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Configurations.class.getName()});
                return null;
            }
        }

    }

}
