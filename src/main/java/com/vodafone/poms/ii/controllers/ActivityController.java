package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.ActivityFacade;
import com.vodafone.poms.ii.entities.AspPo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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

@Named("activityController")
@SessionScoped
public class ActivityController implements Serializable {
    
    private AspPo selectedASPPo;
    
    @EJB
    private com.vodafone.poms.ii.beans.ActivityFacade ejbFacade;
    private List<Activity> items = null;
    private Activity selected;
    
    @Inject
    private TaxesController taxesController;
    @Inject
    private UsersController usersController;
    @Inject
    private AspPoController aspPOController;

    public ActivityController() {
    }

    public Activity getSelected() {
        return selected;
    }

    public void setSelected(Activity selected) {
        this.selected = selected;
        selectedASPPo = null;
    }

    public AspPo getSelectedASPPo() {
        return selectedASPPo;
    }

    public void setSelectedASPPo(AspPo selectedASPPo) {
        this.selectedASPPo = selectedASPPo;
    }
    
    

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ActivityFacade getFacade() {
        return ejbFacade;
    }

    public Activity prepareCreate() {
        selected = new Activity();
        selected.setTaxes(taxesController.getCurrentTaxes());
        selected.setCreator(usersController.getSelected());
        selected.setSysDate(new Date());
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ActivityCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        prepareCreate();
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ActivityUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ActivityDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Activity> getItems() {
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

    public Activity getActivity(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Activity> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Activity> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Activity.class)
    public static class ActivityControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ActivityController controller = (ActivityController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "activityController");
            return controller.getActivity(getKey(value));
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
            if (object instanceof Activity) {
                Activity o = (Activity) object;
                return getStringKey(o.getActivityId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Activity.class.getName()});
                return null;
            }
        }

    }

    public void correlate(){
        if(selected!=null && selectedASPPo!=null){
            selected.setAspPoCollection(new ArrayList<AspPo>(){{add(selectedASPPo);}});
            update();
            selectedASPPo.setActivityCollection(new ArrayList<Activity>(){{add(selected);}});
            selectedASPPo.setWorkDone(selectedASPPo.getWorkDone().add(BigInteger.ONE));
            selectedASPPo.setRemainingInPo(
                    selectedASPPo.getRemainingInPo().subtract(
                            BigInteger.valueOf(selected.getTotalPriceAsp().intValue())));
            aspPOController.setSelected(selectedASPPo);
            aspPOController.update();
            aspPOController.setSelected(null);
            JsfUtil.addSuccessMessage("Activity "+selected.getActivityId()+" is now correlated to ASP PO "+selectedASPPo.getPoNumber());
        }
    }
    
    public void uncorrelate(){
        if(selected!=null && selectedASPPo!=null){
            selected.setAspPoCollection(null);
            update();
            selectedASPPo.getActivityCollection().remove(selected);
            selectedASPPo.setWorkDone(selectedASPPo.getWorkDone().subtract(BigInteger.ONE));
            selectedASPPo.setRemainingInPo(
                    selectedASPPo.getRemainingInPo().add(
                            BigInteger.valueOf(selected.getTotalPriceAsp().intValue())));
            aspPOController.setSelected(selectedASPPo);
            aspPOController.update();
            aspPOController.setSelected(null);
            JsfUtil.addSuccessMessage("Activity "+selected.getActivityId()+" is now uncorrelated from ASP PO "+selectedASPPo.getPoNumber());
        }
    }
}
