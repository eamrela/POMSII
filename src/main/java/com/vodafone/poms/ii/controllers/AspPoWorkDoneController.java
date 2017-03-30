package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.AspPoWorkDone;
import com.vodafone.poms.ii.beans.AspPoWorkDoneFacade;
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

@Named("aspPoWorkDoneController")
@SessionScoped
public class AspPoWorkDoneController implements Serializable {

    @EJB
    private com.vodafone.poms.ii.beans.AspPoWorkDoneFacade ejbFacade;
    private List<AspPoWorkDone> items = null;
    private List<AspPoWorkDone> selectedPoItems = null;
    private AspPoWorkDone selected;
    private AspPoWorkDone selectedWD;
    
    @Inject 
    private AspPoController aspPoController;

    public AspPoWorkDoneController() {
    }

    public AspPoWorkDone getSelected() {
        return selected;
    }

    public void setSelected(AspPoWorkDone selected) {
        this.selected = selected;
    }

    public AspPoWorkDone getSelectedWD() {
        return selectedWD;
    }

    public void setSelectedWD(AspPoWorkDone selectedWD) {
        this.selectedWD = selectedWD;
    }

    
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AspPoWorkDoneFacade getFacade() {
        return ejbFacade;
    }

    public AspPoWorkDone prepareCreate() {
        selected = new AspPoWorkDone();
        selected.setAspPoId(aspPoController.getSelected());
        initializeEmbeddableKey();
        return selected;
    }

    public AspPoWorkDone create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AspPoWorkDoneCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            selectedPoItems = null;
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AspPoWorkDoneUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AspPoWorkDoneDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            selectedPoItems = null;
            selectedWD = null;
        }
    }

    public List<AspPoWorkDone> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<AspPoWorkDone> getSelectedPoItems() {
        if(aspPoController.getSelected()!=null){
            selectedPoItems = getFacade().findRelatedItems(aspPoController.getSelected());
        }
        return selectedPoItems;
    }

    private AspPoWorkDone persistWD(PersistAction persistAction, String successMessage) {
        if (selectedWD != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    selectedWD = getFacade().merge(selectedWD);
                } else {
                    getFacade().remove(selectedWD);
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
        return selectedWD;
    }
    
    private AspPoWorkDone persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    selected = getFacade().merge(selected);
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

    public AspPoWorkDone getAspPoWorkDone(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AspPoWorkDone> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AspPoWorkDone> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AspPoWorkDone.class)
    public static class AspPoWorkDoneControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AspPoWorkDoneController controller = (AspPoWorkDoneController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "aspPoWorkDoneController");
            return controller.getAspPoWorkDone(getKey(value));
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
            if (object instanceof AspPoWorkDone) {
                AspPoWorkDone o = (AspPoWorkDone) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AspPoWorkDone.class.getName()});
                return null;
            }
        }

    }

    public void createWD(){
        if(selected!=null){
             if(selected.getWorkDone().compareTo(aspPoController.getSelected().getFactor())<=0){
                 if(selected.getWorkValue().compareTo(aspPoController.getSelected().getRemainingInPo())<=0){
            aspPoController.getSelected().setWorkDone(aspPoController.getSelected().getWorkDone()+selected.getWorkDone());
            aspPoController.update();
            create();
                 }else{
                     JsfUtil.addErrorMessage("Work done value can't exceed the remaining from PO");
                 }
             }else{
                 JsfUtil.addErrorMessage("Work done factor can't be more than PO Factor");
             }
        }
    }
    
     public void deleteWD(){
        if(selectedWD!=null && aspPoController.getSelected()!=null){
            aspPoController.getSelected().setWorkDone(aspPoController.getSelected().getWorkDone()-selectedWD.getWorkDone());
            aspPoController.update();
            persistWD(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AspPoWorkDoneDeleted"));
        }
    }
}
