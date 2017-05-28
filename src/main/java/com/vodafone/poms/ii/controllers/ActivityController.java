package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.ActivityFacade;
import com.vodafone.poms.ii.entities.ActivityAttachments;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.ConfigurationsPK;
import com.vodafone.poms.ii.entities.Sites;
import com.vodafone.poms.ii.entities.VendorPo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.io.Serializable;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

@Named("activityController")
@SessionScoped
public class ActivityController implements Serializable {
    
    private AspPo selectedASPPo;
    private VendorPo selectedVendorPo;
    private String activityIds;
    private Float totalPriceASP;
    private Float totalPriceVendor;
    private Float itemsUncorrelatedActivityValue;
    
    @EJB
    private com.vodafone.poms.ii.beans.ActivityFacade ejbFacade;
    private List<Activity> items = null;
    private List<Activity> itemsUncorrelated = null;
    private List<Activity> itemsCorrelated = null;
    private Activity selected;
    private List<Activity> selectedItems;
    
    @Inject
    private TaxesController taxesController;
    @Inject
    private UsersController usersController;
    @Inject
    private AspPoController aspPOController;
    @Inject
    private VendorPoController vendorPOController;
    @Inject
    private ActivityAttachmentsController attachmentController;
    @Inject
    private ConfigurationsController configurationController;
   

    public ActivityController() {
    }

    public Activity getSelected() {
        return selected;
    }

    public void setSelected(Activity selected) {
        this.selected = selected;
        selectedASPPo = null;
        selectedVendorPo = null;
    }

    public List<Activity> getSelectedItems() {
        return selectedItems;
    }

    public String getActivityIds() {
        if(selectedItems!=null){
            activityIds = "";
            for (int i = 0; i < selectedItems.size(); i++) {
                activityIds+= " "+selectedItems.get(i).getActivityId();
            }
        }
        return activityIds;
    }

    public Float getTotalPriceASP() {
        if(selectedItems!=null){
            totalPriceASP = 0f;
            for (int i = 0; i < selectedItems.size(); i++) {
                totalPriceASP += selectedItems.get(i).getTotalPriceAsp();
            }
        }
        return totalPriceASP;
    }

    public Float getTotalPriceVendor() {
        if(selectedItems!=null){
            totalPriceVendor = 0f;
            for (int i = 0; i < selectedItems.size(); i++) {
                totalPriceVendor += selectedItems.get(i).getTotalPriceVendor();
            }
        }
        return totalPriceVendor;
    }
    
    

    public void setActivityIds(String activityIds) {
        this.activityIds = activityIds;
    }

    
    public void setSelectedItems(List<Activity> selectedItems) {
        this.selectedItems = selectedItems;
        selectedASPPo = null;
        selectedVendorPo = null;
        if(selectedItems.size()==1){
            selected = selectedItems.get(0);
        }
        else if(selectedItems.size()>0){
            // Check ASP,Type,CorrelateTo
            String domain = selectedItems.get(0).getActivityType().getDomainName();
            String asp = selectedItems.get(0).getAsp().getSubcontractorName();
            String correlateTo = selectedItems.get(0).getCorrelateTo();
            if(correlateTo.equals("ASP")){
            for (int i = 1; i < selectedItems.size(); i++) {
                if(!selectedItems.get(i).getActivityType().getDomainName().equals(domain)
                        ||
                    !selectedItems.get(i).getAsp().getSubcontractorName().equals(asp)
                        || !selectedItems.get(i).getCorrelateTo().equals(correlateTo)){
                    selectedItems.remove(selectedItems.get(i));
                    JsfUtil.addErrorMessage("Selected Activity doesn't have a matching ASP/Type/CorrelationType");
                }
            }
            } else if(correlateTo.equals("CUSTOMER")){
             for (int i = 1; i < selectedItems.size(); i++) {
                if(!selectedItems.get(i).getActivityType().getDomainName().equals(domain)
                        || !selectedItems.get(i).getCorrelateTo().equals(correlateTo)){
                    selectedItems.remove(selectedItems.get(i));
                    JsfUtil.addErrorMessage("Selected Activity doesn't have a matching Type/CorrelationType");
                }
            }   
            }
            selected = null;
        }
    }
    
    

    public AspPo getSelectedASPPo() {
        return selectedASPPo;
    }

    public void setSelectedASPPo(AspPo selectedASPPo) {
        this.selectedASPPo = selectedASPPo;
    }

    public VendorPo getSelectedVendorPo() {
        return selectedVendorPo;
    }

    public void setSelectedVendorPo(VendorPo selectedVendorPo) {
        this.selectedVendorPo = selectedVendorPo;
    }
    
    
    public void onItemSelectSetSite(SelectEvent event){
    if(selected!=null){
        selected.setSite((Sites) event.getObject());
    }
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
        selected.setCreator(usersController.getLoggedInUser());
        selected.setSysDate(new Date());
        initializeEmbeddableKey();
        System.out.println("Prepare Create for Activity");
        return selected;
    }

    public void createMultiple(List<Activity> activities){
        for (Activity activitie : activities) {
            try{
            getFacade().create(activitie);
            }catch(Exception e){
                System.out.println(activitie.getSite().getSitePhysical());
                
            }
        }
        
    }
    
    public void create() {
        System.out.println("Creating Activity");
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ActivityCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            itemsCorrelated = null;
            itemsUncorrelated = null;
        }
        prepareCreate();
        try {
            //http://localhost:8080/POMS-II/app/business_provider/crud_activity.xhtml
            FacesContext.getCurrentInstance().getExternalContext().redirect("/POMS-II/app/business_provider/crud_activity.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ActivityController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ActivityUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ActivityDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            selectedItems = null;
            items = null;    // Invalidate list of items to trigger re-query.
            itemsCorrelated = null;
            itemsUncorrelated = null;
        }
    }

    public List<Activity> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<Activity> getItemsCorrelated() {
        if(itemsCorrelated == null){
            itemsCorrelated = getFacade().findCorrelatedItems(usersController.getLoggedInUser());
        }
        return itemsCorrelated;
    }

    public List<Activity> getItemsUncorrelated() {
        itemsUncorrelated = getFacade().findUncorrelatedItems(usersController.getLoggedInUser());
        itemsUncorrelatedActivityValue = 0f;
        for (Activity itemsUncorrelated1 : itemsUncorrelated) {
            itemsUncorrelatedActivityValue = itemsUncorrelatedActivityValue + itemsUncorrelated1.getTotalPriceAsp();
        }
        return itemsUncorrelated;
    }

    public Float getItemsUncorrelatedActivityValue() {
        return itemsUncorrelatedActivityValue;
    }

    public void setItemsUncorrelatedActivityValue(Float itemsUncorrelatedActivityValue) {
        this.itemsUncorrelatedActivityValue = itemsUncorrelatedActivityValue;
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

    public List<Activity> getExportItems(Date fromDate, Date toDate) {
        return getFacade().findExportItems(fromDate,toDate);
    }

    public List<Activity> getUserItems() {
        return getFacade().findUserItems(usersController.getLoggedInUser());
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
        if(selectedItems!=null && selectedASPPo!=null){
            Float totalPrice = 0f;
            for (int i = 0; i < selectedItems.size(); i++) {
            selectedItems.get(i).setAspPoCollection(new ArrayList<AspPo>(){{add(selectedASPPo);}});
            totalPrice += selectedItems.get(0).getTotalPriceAsp();
            selected = selectedItems.get(i);
            update(); 
            selected = null;
            }
            selectedASPPo.setActivityCollection(new ArrayList<Activity>(){{addAll(selectedItems);}});
            selectedASPPo.setWorkDone(selectedASPPo.getWorkDone()+
                (totalPrice/selectedASPPo.getServiceValue().floatValue())
            );
//            selectedASPPo.setRemainingInPo(
//                    selectedASPPo.getRemainingInPo().subtract(
//                            BigInteger.valueOf(totalPrice.intValue())));
            aspPOController.setSelected(selectedASPPo);
            aspPOController.update();
            aspPOController.setSelected(null);
            itemsUncorrelated = null;
            selectedItems = null;
            JsfUtil.addSuccessMessage("Activity "+activityIds+" is now correlated to ASP PO "+selectedASPPo.getPoNumber());
        }
    }
    
    public void uncorrelate(){
        if(selected!=null){
            selected.getAspPoCollection().remove(aspPOController.getSelected());
            aspPOController.getSelected().getActivityCollection().remove(selected);
            aspPOController.getSelected().setWorkDone(aspPOController.getSelected().getWorkDone()-
                    (selected.getTotalPriceAsp()/aspPOController.getSelected().getServiceValue().floatValue()));
            update();
            aspPOController.update();
            selected = null;
        }
    }
    
    public void correlateVendorPo(){
         if(selectedItems!=null && selectedVendorPo!=null){
            Float totalPrice = 0f;
            for (int i = 0; i < selectedItems.size(); i++) {
            selectedItems.get(i).setVendorPoCollection(new ArrayList<VendorPo>(){{add(selectedVendorPo);}});
            totalPrice += selectedItems.get(0).getTotalPriceVendor();
            selected = selectedItems.get(i);
            update(); 
            selected = null;
            }
            selectedVendorPo.getActivityCollection().addAll(selectedItems);
            selectedVendorPo.setWorkDone(selectedVendorPo.getWorkDone()+
                    (totalPrice/selectedVendorPo.getServiceValue().floatValue()));
            vendorPOController.setSelected(selectedVendorPo);
            vendorPOController.update();
            vendorPOController.setSelected(null);
            itemsUncorrelated = null;
            selectedItems = null;
            JsfUtil.addSuccessMessage("Activity "+activityIds+" is now correlated to Vendor PO "+selectedVendorPo.getPoNumber());
        }
    }
    
    public void uncorrelateVendor(){
        if(selected!=null){
            selected.getVendorPoCollection().remove(vendorPOController.getSelected());
            vendorPOController.getSelected().getActivityCollection().remove(selected);
            vendorPOController.getSelected().setWorkDone(vendorPOController.getSelected().getWorkDone()-
                    (selected.getTotalPriceVendor()/vendorPOController.getSelected().getServiceValue().floatValue()));
            update();
            vendorPOController.update();
            selected = null;
        }
    }
    
    public void clearSelected(){
        selected=null;
        items = null;
        itemsUncorrelated = null;
        itemsUncorrelated = null;
        selectedASPPo = null;
        selectedVendorPo = null;
    }
    
    public void validateUMPercent(){
        if(selected!=null && !usersController.getRole().equals("ROLE_SYSADMIN")){
            if(selected.getAcUmPercent().compareTo(0.12f)==-1){
                selected.setActivityCode(selected.getActivityCode());
                JsfUtil.addErrorMessage("UM% Can't be less than 12%, Please contact your manager");
            }
        }
    }

    public void uploadAttachment(FileUploadEvent event){
        if(selected!=null){
            if(event.getFile() != null) {
                String path = configurationController.getConfigurations(new ConfigurationsPK("ACTIVITY_ATTACHMENT", "PROD")).getConfValue();
                new File(path+"\\"+selected.getActivityId()).mkdir();
                File file = new File(path+"\\"+selected.getActivityId()+"\\"+event.getFile().getFileName());
                OutputStream outputStream;
                try {
                    outputStream = new FileOutputStream(file);
                    IOUtils.copy(event.getFile().getInputstream(), outputStream);
                    outputStream.close();
                    attachmentController.prepareCreate();
                    attachmentController.getSelected().setActivityId(selected);
                    attachmentController.getSelected().setAttachmentLocation(file.getAbsolutePath());
                    attachmentController.getSelected().setAttachmentName(file.getName());
                    attachmentController.getSelected().setUploadedBy(usersController.getLoggedInUser());
                    attachmentController.getSelected().setUploadedOn(new Date());
                    
                    if(selected.getActivityAttachmentsCollection()!=null){
                        selected.getActivityAttachmentsCollection().add(attachmentController.create());
                        update();
                    }else{
                        selected.setActivityAttachmentsCollection(new ArrayList<>());
                        selected.getActivityAttachmentsCollection().add(attachmentController.create());
                        update();
                    }
                    
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ActivityController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ActivityController.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }
        }
    }
    
    public void downloadAttachment(ActivityAttachments item){
        if(selected!=null){
            try {

                System.out.println("Downloading Attachment");
                File FB = new File(item.getAttachmentLocation());
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext =  facesContext.getExternalContext();
                externalContext.responseReset();
                
                externalContext.setResponseContentType("application/csv");
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\""+FB.getName()+""+"\"");
                
                FileInputStream fin = new FileInputStream(FB);
                OutputStream output = externalContext.getResponseOutputStream();
                byte[] data;
                data = new byte[fin.available()];
                fin.read(data);
                output.write(data);
                output.flush();
                output.close();
                facesContext.responseComplete();
            } catch (IOException ex) {
                Logger.getLogger(ActivityController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
