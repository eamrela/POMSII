package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.AspPoFacade;
import com.vodafone.poms.ii.entities.AspGrn;
import com.vodafone.poms.ii.entities.VendorPo;
import com.vodafone.poms.ii.entities.VendorPoJAspPoValue;
import com.vodafone.poms.ii.entities.VendorPoJAspPoValuePK;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.joda.time.DateTime;

@Named("aspPoController")
@SessionScoped
public class AspPoController implements Serializable {

    

    private List<VendorPo> selectedVendorPoList;
    private List<VendorPoJAspPoValue> selectedItemsToBeCorrelated;
    private VendorPo selectVendorPo = null;
    private VendorPoJAspPoValue itemToCorrelate;
    private BigDecimal valueToBeTaken;
    private String poIds;
    private BigDecimal totalPOASPPrice;
    private BigDecimal itemsUncorrelatedPoValue;
    private BigDecimal selectedGrnDeserved;
    private boolean disabled = false;
    private Date startMonth;
    private Integer numberOfMonths;
    
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
    @Inject 
    private PoTypesController poTypesController;
    @Inject
    private VendorPoJAspPoValueController vendorPoAspPoValueController;

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

    public VendorPoJAspPoValue getItemToCorrelate() {
        return itemToCorrelate;
    }

    public void setItemToCorrelate(VendorPoJAspPoValue itemToCorrelate) {
        this.itemToCorrelate = itemToCorrelate;
    }

    public void updateSelectedItem(VendorPoJAspPoValue po){
        itemToCorrelate = po;
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
        selected.setPoType(poTypesController.getPoTypes("Extra Work"));
        initializeEmbeddableKey();
        return selected;
    }
    
    public AspPo prepareCreateService() {
        selected = new AspPo();
        startMonth = null;
        numberOfMonths = null;
        selected.setTaxes(taxesController.getCurrentTaxes());
        selected.setCreator(usersController.getLoggedInUser());
        selected.setSysDate(new Date());
        selected.setPoStatus(poStatusController.getInitialStatus());
        selected.setPoType(poTypesController.getPoTypes("Service"));
        selected.setPoMargin(0.0);
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
        try {   
            FacesContext.getCurrentInstance().getExternalContext().redirect("/POMS-II/app/finance_admin/crud_asp_po.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ActivityController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createService(){
        DateTime start = new DateTime(startMonth);
         if(selected.getRemainingInPo()==null){
                selected.setRemainingInPo(selected.getPoValue());
        }
         String description = selected.getPoDescription();
         String poNumber = selected.getPoNumber();
         
        for (int i = 0; i < numberOfMonths; i++) {
            selected.setPoDate(start.plusMonths(i).toDate());
            selected.setPoNumber(i+"-"+poNumber);
            selected.setPoDescription("["+start.plusMonths(i).toString("MMM")+"] "+description);
            persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AspPoCreated"));
            if (!JsfUtil.isValidationFailed()) {
                items = null;    // Invalidate list of items to trigger re-query.
                itemsUncorrelated = null;
                selectedItems = null;
            }
        } 
        
        prepareCreate();
        prepareCreateService();
        try {   
            FacesContext.getCurrentInstance().getExternalContext().redirect("/POMS-II/app/finance_admin/crud_asp_po.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ActivityController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            selectedGrnDeserved = null;
        }
    }

     public void closePO(){
        if(selected!=null){
            selected.setPoStatus(poStatusController.getFinalStatus());
            update();
            items = null;
        }
    }

     
    public List<AspPo> getItems() {
        if (items == null) {
            items = getFacade().findAllOpen();
        }
        return items;
    }

    public List<AspPo> getItemsUncorrelated() {
        itemsUncorrelated=getFacade().findUncorrelatedItems();
        itemsUncorrelatedPoValue = BigDecimal.ZERO;
        for (AspPo itemsUncorrelated1 : itemsUncorrelated) {
            itemsUncorrelatedPoValue = itemsUncorrelatedPoValue.add(BigDecimal.valueOf(
                                        itemsUncorrelated1.getPoValue().doubleValue()*(1+(itemsUncorrelated1.getPoMargin()/100.0))
                                ));
        }
        return itemsUncorrelated;
    }

    
    public List<AspPo> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<AspPo> selectedItems) {
        this.selectedItems = selectedItems;
        selectedVendorPoList = null;
        valueToBeTaken = BigDecimal.ZERO;
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
            selectedGrnDeserved = null;
        }
    }

    public void setPoIds(String poIds) {
        this.poIds = poIds;
    }

    public void setTotalPOASPPrice(BigDecimal totalPOASPPrice) {
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

    public BigDecimal getTotalPOASPPrice() {
        BigDecimal totalCorrelatedPrice = BigDecimal.ZERO;
        totalPOASPPrice = BigDecimal.ZERO;
        if(selectedItems!=null){
            for (int i = 0; i < selectedItems.size(); i++) {
                totalPOASPPrice = 
                        totalPOASPPrice.add(
                                BigDecimal.valueOf(
                                        selectedItems.get(i).getPoValue().doubleValue()*(1+(selectedItems.get(i).getPoMargin()/100.0))
                                ));
                totalCorrelatedPrice = totalCorrelatedPrice.add(vendorPoAspPoValueController.getValue(selectedItems.get(i)));
            }
        }
        totalPOASPPrice = totalPOASPPrice.subtract(totalCorrelatedPrice);
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

    public List<AspPo> findAll(){
        return getFacade().findAll();
    }
    
    public List<AspPo> getDashboardCommittedCost(String domains) {
        return getFacade().findCommittedCostItems(domains);
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
            if(selected.getGrnDeserved().compareTo(BigDecimal.ZERO)==1){
                aspGrnController.prepareCreate();
                aspGrnController.getSelected().setAspPoId(selected);
                aspGrnController.getSelected().setCreator(usersController.getLoggedInUser());
                aspGrnController.getSelected().setGrnDeserved(selected.getGrnDeserved());
                aspGrnController.getSelected().setSysDate(new Date());
                aspGrnController.create();
                update();
                
            }
        }
    }

   

    public List<VendorPo> getSelectedVendorPoList() {
        return selectedVendorPoList;
    }

    public void setSelectedVendorPoList(List<VendorPo> selectedVendorPoList) {
        this.selectedVendorPoList = selectedVendorPoList;
    }
    
    

    public BigDecimal getValueToBeTaken() {
        return valueToBeTaken;
    }

    public void setValueToBeTaken(BigDecimal valueToBeTaken) {
        this.valueToBeTaken = valueToBeTaken;
    }
    
    public void prepareCorrelate(){
        selectedItemsToBeCorrelated = new ArrayList<>();
        for (int i = 0; i < selectedVendorPoList.size(); i++) {
            List<VendorPoJAspPoValue> correlatedItems = vendorPoAspPoValueController.getSelectedItems(selectedVendorPoList.get(i));
            if(correlatedItems.isEmpty()){
            VendorPoJAspPoValue item = new VendorPoJAspPoValue();
            item.setAspPo(selected);
            item.setValueToTake(BigDecimal.ZERO);
            item.setVendorPo(selectedVendorPoList.get(i));
            VendorPoJAspPoValuePK pk = new VendorPoJAspPoValuePK();
            pk.setAspPoId(item.getAspPo().getPoNumber());
            pk.setVendorPoId(item.getVendorPo().getPoNumber());
            item.setVendorPoJAspPoValuePK(pk);
            selectedItemsToBeCorrelated.add(item);
            }else{
                selectedItemsToBeCorrelated.addAll(correlatedItems);
            }
        }
    }

    public List<VendorPoJAspPoValue> getSelectedItemsToBeCorrelated() {
        return selectedItemsToBeCorrelated;
    }

    public void setSelectedItemsToBeCorrelated(List<VendorPoJAspPoValue> selectedItemsToBeCorrelated) {
        this.selectedItemsToBeCorrelated = selectedItemsToBeCorrelated;
    }
    
    public void correlateItem(){
        if(itemToCorrelate!=null){
            selectVendorPo = itemToCorrelate.getVendorPo();
            
            vendorPoAspPoValueController.setSelected(itemToCorrelate);
            vendorPoAspPoValueController.createOrIncrease();
            
            selected.setVendorPoCollection(new ArrayList<VendorPo>(){{add(selectVendorPo);}});
            update();
            selectVendorPo.getAspPoCollection().add(selected);
            selectVendorPo.setWorkDone(selectVendorPo.getWorkDone()+
                (itemToCorrelate.getValueToTake().floatValue()/selectVendorPo.getServiceValue().floatValue())
            );
            vendorPoController.setSelected(selectVendorPo);
            vendorPoController.update();
            vendorPoController.setSelected(null);
            items = null;
            //selectedItems = null;
            JsfUtil.addSuccessMessage("ASP PO "+poIds+" is now correlated to Customer PO "+selectVendorPo.getPoNumber());
            selectVendorPo=null;
            selectedItemsToBeCorrelated.remove(itemToCorrelate);
            if(selectedItemsToBeCorrelated.size()==1){
                selectedItemsToBeCorrelated.clear();
            }
        }else{
            // report error
            JsfUtil.addSuccessMessage("You need to select and item");
        }
    }
    
    public void correlateSingle(){
        // if it exists incease the value
        if(selectedVendorPoList.size()==1){
            selectVendorPo = selectedVendorPoList.get(0);
            VendorPoJAspPoValue correlationValue = new VendorPoJAspPoValue();
            correlationValue.setAspPo(selected);
            correlationValue.setVendorPo(selectVendorPo);
            correlationValue.setValueToTake(totalPOASPPrice);
            VendorPoJAspPoValuePK pk = new VendorPoJAspPoValuePK();
            pk.setAspPoId(correlationValue.getAspPo().getPoNumber());
            pk.setVendorPoId(correlationValue.getVendorPo().getPoNumber());
            correlationValue.setVendorPoJAspPoValuePK(pk);
            vendorPoAspPoValueController.setSelected(correlationValue);
            vendorPoAspPoValueController.createOrIncrease();
            selected.setVendorPoCollection(new ArrayList<VendorPo>(){{add(selectVendorPo);}});
            update();
            selectVendorPo.setAspPoCollection(new ArrayList<AspPo>(){{add(selected);}});
            selectVendorPo.setWorkDone(selectVendorPo.getWorkDone()+
                (totalPOASPPrice.floatValue()/selectVendorPo.getServiceValue().floatValue())
            );
            vendorPoController.setSelected(selectVendorPo);
            vendorPoController.update();
            vendorPoController.setSelected(null);
            items = null;
            selectedItems = null;
            JsfUtil.addSuccessMessage("ASP PO "+poIds+" is now correlated to Customer PO "+selectVendorPo.getPoNumber());
            selectVendorPo=null;
        }else{
            // report error
            JsfUtil.addSuccessMessage("You can't correlate Single for multiple POs");
        }
    }
    
    // needs to be modified
    public void uncorrelate(){
        if(selected!=null){
            VendorPoJAspPoValue valueTaken = vendorPoAspPoValueController.findById(vendorPoController.getSelected().getPoNumber(),selected.getPoNumber());
            if(valueTaken!=null){
            selected.getVendorPoCollection().remove(vendorPoController.getSelected());
            vendorPoController.getSelected().getAspPoCollection().remove(selected);
            vendorPoController.getSelected().setWorkDone(vendorPoController.getSelected().getWorkDone()-
                    (itemToCorrelate.getValueToTake().floatValue()/vendorPoController.getSelected().getServiceValue().floatValue()));
            update();
            vendorPoController.update();
            selected = null;
            vendorPoAspPoValueController.setSelected(valueTaken);
            vendorPoAspPoValueController.destroy();
            }else{
               JsfUtil.addSuccessMessage("Something went wrong, please contact system admin"); 
            }
        }
    }
    
    public BigDecimal getSelectedGrnDeserved() {
        if(selected!=null){
        BigDecimal totalGrnValue = BigDecimal.ZERO;
        BigDecimal totalGrnDeserved = BigDecimal.valueOf(selected.getServiceValue().floatValue()*selected.getWorkDone());
        List<AspGrn> grns = aspGrnController.getSelectedPoItems();
        for (int i = 0; i < grns.size(); i++) {
            totalGrnValue  = totalGrnValue.add(grns.get(i).getGrnValue()!=null?
                                    grns.get(i).getGrnValue():BigDecimal.ZERO);
        }
        selected.setGrnDeserved(totalGrnDeserved.subtract(totalGrnValue));
        selectedGrnDeserved = selected.getGrnDeserved();
        return selectedGrnDeserved;
        
        }
        selectedGrnDeserved = BigDecimal.ZERO;
        return selectedGrnDeserved;
    }
    
    public boolean correlationStatus(AspPo po){
        if(po!=null){
            
            BigDecimal selectedCorrelationValue = vendorPoAspPoValueController.getValue(po);
            if(po.getPoValue().equals(selectedCorrelationValue) || po.getPoValue().compareTo(selectedCorrelationValue)==-1){
                return true;
            }
        }
        return false;
    }
    
    public void clearSelected(){
        selected=null;
        items = null;
        selectedGrnDeserved = null;
        itemsUncorrelated = null;
        selectedVendorPoList = null;
        selectedItems=null;
        selectedItemsToBeCorrelated = null;
        itemToCorrelate = null;
    }

    public BigDecimal getItemsUncorrelatedPoValue() {
        return itemsUncorrelatedPoValue;
    }

    public void setItemsUncorrelatedPoValue(BigDecimal itemsUncorrelatedPoValue) {
        this.itemsUncorrelatedPoValue = itemsUncorrelatedPoValue;
    }
    
    public void adjustMargin(){
        if(selected!=null){
            if(selected.getPoType().getTypeName().equals("Service")){
                selected.setPoMargin(13.0);
            }else{
                selected.setPoMargin(30.0);
            }
        }
    }

    public Date getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Date startMonth) {
        this.startMonth = startMonth;
    }

    public Integer getNumberOfMonths() {
        return numberOfMonths;
    }

    public void setNumberOfMonths(Integer numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }
    
    
}
