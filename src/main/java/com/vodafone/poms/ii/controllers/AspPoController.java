package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.AspPoFacade;
import com.vodafone.poms.ii.entities.VendorPo;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Named("aspPoController")
@SessionScoped
public class AspPoController implements Serializable {

    
    private VendorPo selectedVendorPo;
    private String poIds;
    private BigInteger totalPOASPPrice;
    private BigInteger itemsUncorrelatedPoValue;
    private boolean disabled = false;
    @EJB
    private com.vodafone.poms.ii.beans.AspPoFacade ejbFacade;
    private List<AspPo> items = null;
    private List<AspPo> itemsUncorrelated = null;
    private List<AspPo> selectedItems = null;
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

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        if(selectedItems!=null){
            for (int i = 0; i < selectedItems.size(); i++) {
                if(selectedItems.get(i).getVendorPoCollection().size()>0)
                {
                    disabled=true;
                    break;
                }
            }
        }
        return disabled;
    }

    
    
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
        selected.setCreator(usersController.getLoggedInUser());
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
            itemsUncorrelated = null;
            selectedItems = null;
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
            itemsUncorrelated = null;
            selectedItems = null;
        }
    }

    public List<AspPo> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<AspPo> getItemsUncorrelated() {
        itemsUncorrelated=getFacade().findUncorrelatedItems();
        itemsUncorrelatedPoValue = BigInteger.ZERO;
        for (AspPo itemsUncorrelated1 : itemsUncorrelated) {
            itemsUncorrelatedPoValue = itemsUncorrelatedPoValue.add(itemsUncorrelated1.getPoValue());
        }
        return itemsUncorrelated;
    }

    
    public List<AspPo> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<AspPo> selectedItems) {
        this.selectedItems = selectedItems;
        selectedVendorPo = null;
        if(selectedItems.size()==1){
            selected = selectedItems.get(0);
        }
        else{
            // Check ASP,Type,
            String poType = selectedItems.get(0).getPoType().getTypeName();
            String domainName = selectedItems.get(0).getDomainName().getDomainName();
            for (int i = 1; i < selectedItems.size(); i++) {
                if(!selectedItems.get(i).getPoType().getTypeName().equals(poType)
                        ||
                    !selectedItems.get(i).getDomainName().getDomainName().equals(domainName)){
                    selectedItems.remove(selectedItems.get(i));
                    JsfUtil.addErrorMessage("Selected POs doesn't have a matching Type/Domain");
                }
            }
            selected = null;
        }
    }

    public void setPoIds(String poIds) {
        this.poIds = poIds;
    }

    public void setTotalPOASPPrice(BigInteger totalPOASPPrice) {
        this.totalPOASPPrice = totalPOASPPrice;
    }

    public String getPoIds() {
         if(selectedItems!=null){
            poIds = "";
            for (int i = 0; i < selectedItems.size(); i++) {
                poIds+= " "+selectedItems.get(i).getPoNumber();
            }
        }
        return poIds;
    }

    public BigInteger getTotalPOASPPrice() {
        if(selectedItems!=null){
            totalPOASPPrice = BigInteger.ZERO;
            for (int i = 0; i < selectedItems.size(); i++) {
                totalPOASPPrice = 
                        totalPOASPPrice.add(
                                BigDecimal.valueOf(
                                        selectedItems.get(i).getPoValue().doubleValue()*(1+(selectedItems.get(i).getPoMargin()/100.0))
                                ).toBigInteger());
            }
        }
        return totalPOASPPrice;
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

    public List<AspPo> getExportItems(Date fromDate, Date toDate) {
        return getFacade().findExportItems(fromDate,toDate);
    }

    public List<AspPo> getDashboardCommittedCost(Date start, Date end) {
        return getFacade().findCommittedCostItems(start,end);
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
        if(activityController.getSelectedItems()!=null){
            suggestedPOs = getFacade().findPOforActivity(activityController.getSelectedItems());
        }
        return suggestedPOs;
    }

    public void createGRN(){
        if(selected!=null){
            if(selected.getGrnDeserved().compareTo(BigInteger.ZERO)==1){
                aspGrnController.prepareCreate();
                aspGrnController.getSelected().setAspPoId(selected);
                aspGrnController.getSelected().setCreator(usersController.getLoggedInUser());
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
        if(selectedVendorPo!=null && selectedItems!=null){
           disabled=true;
           BigInteger totalPrice = BigInteger.ZERO;
            for (int i = 0; i < selectedItems.size(); i++) {
            selectedItems.get(i).setVendorPoCollection(new ArrayList<VendorPo>(){{add(selectedVendorPo);}});
            totalPrice = 
                        totalPrice.add(
                                BigDecimal.valueOf(
                                        selectedItems.get(i).getPoValue().doubleValue()*(1+(selectedItems.get(i).getPoMargin()/100.0))
                                ).toBigInteger());
            selected = selectedItems.get(i);
            update(); 
            selected = null;
            }
            selectedVendorPo.setAspPoCollection(new ArrayList<AspPo>(){{addAll(selectedItems);}});
            selectedVendorPo.setWorkDone(selectedVendorPo.getWorkDone()+
                (totalPrice.floatValue()/selectedVendorPo.getServiceValue().floatValue())
            );
            selectedVendorPo.setRemainingInPo(
                    selectedVendorPo.getRemainingInPo().subtract(
                            BigInteger.valueOf(totalPrice.intValue())));
            vendorPoController.setSelected(selectedVendorPo);
            vendorPoController.update();
            vendorPoController.setSelected(null);
            items = null;
            selectedItems = null;
            JsfUtil.addSuccessMessage("ASP PO "+poIds+" is now correlated to Customer PO "+selectedVendorPo.getPoNumber());
            selectedVendorPo=null;
        }
    }
    
    public void clearSelected(){
        selected=null;
        items = null;
        itemsUncorrelated = null;
    }

    public BigInteger getItemsUncorrelatedPoValue() {
        return itemsUncorrelatedPoValue;
    }

    public void setItemsUncorrelatedPoValue(BigInteger itemsUncorrelatedPoValue) {
        this.itemsUncorrelatedPoValue = itemsUncorrelatedPoValue;
    }
    
    public void adjustMargin(){
        if(selected!=null){
            if(selected.getPoType().getTypeName().equals("Service")){
                selected.setPoMargin(13.0);
            }
        }
    }
}
