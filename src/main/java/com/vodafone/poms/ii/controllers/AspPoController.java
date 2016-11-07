package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.AspPoFacade;
import com.vodafone.poms.ii.entities.VendorPo;

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

@Named("aspPoController")
@SessionScoped
public class AspPoController implements Serializable {

    
    private VendorPo selectedVendorPo;
    @EJB
    private com.vodafone.poms.ii.beans.AspPoFacade ejbFacade;
    private List<AspPo> items = null;
    private AspPo selected;
    
    @Inject
    private ActivityController activityController;
    @Inject
    private TaxesController taxesController;
    @Inject
    private UsersController usersController;
    @Inject
    private PoStatusController poStatusController;
    @Inject 
    private AspGrnController aspGrnController;
    @Inject
    private VendorPoController vendorPoController;

    public AspPoController() {
    }

    public AspPo getSelected() {
        return selected;
    }

    public void setSelected(AspPo selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AspPoFacade getFacade() {
        return ejbFacade;
    }

    public AspPo prepareCreate() {
        selected = new AspPo();
        selected.setTaxes(taxesController.getCurrentTaxes());
        selected.setCreator(usersController.getSelected());
        selected.setSysDate(new Date());
        selected.setPoStatus(poStatusController.getInitialStatus());
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        if(selected.getRemainingInPo()==null){
            selected.setRemainingInPo(selected.getPoValue());
        }
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AspPoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        prepareCreate();
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AspPoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AspPoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AspPo> getItems() {
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

    public AspPo getAspPo(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<AspPo> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AspPo> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AspPo.class)
    public static class AspPoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AspPoController controller = (AspPoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "aspPoController");
            return controller.getAspPo(getKey(value));
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
            if (object instanceof AspPo) {
                AspPo o = (AspPo) object;
                return getStringKey(o.getPoNumber());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AspPo.class.getName()});
                return null;
            }
        }

    }

    public List<AspPo> findMatchingPOForActivity(){
        List<AspPo> suggestedPOs = new ArrayList<>();
        if(activityController.getSelected()!=null){
            suggestedPOs = getFacade().findPOforActivity(activityController.getSelected());
        }
        return suggestedPOs;
    }

    public void createGRN(){
        if(selected!=null){
            if(selected.getGrnDeserved().compareTo(BigInteger.ZERO)==1){
                aspGrnController.prepareCreate();
                aspGrnController.getSelected().setAspPoId(selected);
                aspGrnController.getSelected().setCreator(usersController.getSelected());
                aspGrnController.getSelected().setGrnDeserved(selected.getGrnDeserved());
                aspGrnController.getSelected().setSysDate(new Date());
                selected.getAspGrnCollection().add(aspGrnController.create());
                selected.setGrnDeserved(BigInteger.ZERO);
                update();
                
            }
        }
    }

    public VendorPo getSelectedVendorPo() {
        return selectedVendorPo;
    }

    public void setSelectedVendorPo(VendorPo selectedVendorPo) {
        this.selectedVendorPo = selectedVendorPo;
    }
    
    public void correlate(){
        if(selectedVendorPo!=null && selected!=null){
           selected.setVendorPoCollection(new ArrayList<VendorPo>(){{add(selectedVendorPo);}});
            update();
            selectedVendorPo.setAspPoCollection(new ArrayList<AspPo>(){{add(selected);}});
            selectedVendorPo.setWorkDone(selectedVendorPo.getWorkDone().add(BigInteger.valueOf(selected.getFactor().intValue())));
//            selectedVendorPo.setRemainingInPo(
//            selectedVendorPo.getRemainingInPo().subtract(
//                            BigInteger.valueOf((((Float)(selected.getPoValue().intValue()
//                                                +selected.getPoValue().intValue()*(Float.valueOf(String.valueOf((selected.getPoMargin()/100))))))
//                                                .intValue()))));
            vendorPoController.setSelected(selectedVendorPo);
            vendorPoController.update();
            vendorPoController.setSelected(null);
            JsfUtil.addSuccessMessage("ASP PO "+selected.getPoNumber()+" is now correlated to Vendor PO "+selectedVendorPo.getPoNumber());
        }
    }
    
}
