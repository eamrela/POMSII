package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.VendorPoWorkDone;
import com.vodafone.poms.ii.beans.VendorPoWorkDoneFacade;
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
import javax.inject.Inject;

@Named("vendorPoWorkDoneController")
@SessionScoped
public class VendorPoWorkDoneController implements Serializable {

    @EJB
    private com.vodafone.poms.ii.beans.VendorPoWorkDoneFacade ejbFacade;
    private List<VendorPoWorkDone> items = null;
    private List<VendorPoWorkDone> selectedPoItems = null;
    private VendorPoWorkDone selected;

    @Inject
    private VendorPoController vendorPoController;
    
    public VendorPoWorkDoneController() {
    }

    public VendorPoWorkDone getSelected() {
        return selected;
    }

    public void setSelected(VendorPoWorkDone selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VendorPoWorkDoneFacade getFacade() {
        return ejbFacade;
    }

    public VendorPoWorkDone prepareCreate() {
        selected = new VendorPoWorkDone();
        selected.setVPoId(vendorPoController.getSelected());
        initializeEmbeddableKey();
        return selected;
    }

    public VendorPoWorkDone create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VendorPoWorkDoneCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            selectedPoItems = null;
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VendorPoWorkDoneUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VendorPoWorkDoneDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            selectedPoItems = null;
        }
    }

    public List<VendorPoWorkDone> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<VendorPoWorkDone> getSelectedPoItems() {
        if(vendorPoController.getSelected()!=null){
            selectedPoItems = getFacade().findRelatedItems(vendorPoController.getSelected());
        }
        return selectedPoItems;
    }

    
    private VendorPoWorkDone persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                   selected =  getFacade().merge(selected);
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
        return selected;
    }

    public VendorPoWorkDone getVendorPoWorkDone(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<VendorPoWorkDone> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<VendorPoWorkDone> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = VendorPoWorkDone.class)
    public static class VendorPoWorkDoneControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VendorPoWorkDoneController controller = (VendorPoWorkDoneController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vendorPoWorkDoneController");
            return controller.getVendorPoWorkDone(getKey(value));
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
            if (object instanceof VendorPoWorkDone) {
                VendorPoWorkDone o = (VendorPoWorkDone) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), VendorPoWorkDone.class.getName()});
                return null;
            }
        }

    }

    public void createWD(){
        if(selected!=null){
            vendorPoController.getSelected().setWorkDone(vendorPoController.getSelected().getWorkDone()+selected.getWorkDone());
            vendorPoController.update();
            create();
        }
    }
}
