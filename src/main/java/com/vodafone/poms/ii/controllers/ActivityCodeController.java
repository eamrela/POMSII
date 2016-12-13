package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.ActivityCode;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.ActivityCodeFacade;
import com.vodafone.poms.ii.helpers.ActivityCodeLoader;
import java.io.File;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.event.FileUploadEvent;

@Named("activityCodeController")
@SessionScoped
public class ActivityCodeController implements Serializable {

    @EJB
    private com.vodafone.poms.ii.beans.ActivityCodeFacade ejbFacade;
    private List<ActivityCode> items = null;
    private ActivityCode selected;
    private List<ActivityCode> uploadedItems = null;
    private Float umPercent;
    private Float um;
    private String subcontractorPrice;
    private String vendorPrice;
    private File uploadedFile;

    public void initUpload(){
        uploadedItems=new ArrayList<>();
    }
    
    public void resetSelection(){
        selected= null;
    }
    
    public ActivityCodeController() {
    }

    public ActivityCode getSelected() {
        return selected;
    }

     public File getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(File uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<ActivityCode> getUploadedItems() {
        return uploadedItems;
    }

    public void setUploadedItems(List<ActivityCode> uploadedItems) {
        this.uploadedItems = uploadedItems;
    }
     public void prepareUpload(){
        uploadedFile=null;
        initUpload();
    }
     public Boolean validateSubcontractorPrice(){
        if(subcontractorPrice==null){
            return false;
        }else{
            if(vendorPrice!=null){
                return Float.valueOf(vendorPrice)>=Float.valueOf(subcontractorPrice);
            }
        }
        return false;
    }
     
     private void persistAll(List<ActivityCode> list){
        if (list!=null?list.size()>0:false) {
            try {
                for (int i = 0; i < list.size(); i++) {
                    getFacade().edit(list.get(i));
                }
                JsfUtil.addSuccessMessage("Bulk upload succeeded");
                items=null;
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
        }else{
            JsfUtil.addErrorMessage("No ActivityCodes found to update!");
            
        }
    }

    public void setSelected(ActivityCode selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ActivityCodeFacade getFacade() {
        return ejbFacade;
    }

    public ActivityCode prepareCreate() {
        selected = new ActivityCode();
        selected.setVendorPrice(Float.valueOf("0"));
        selected.setSubcontractorPrice(Float.valueOf("0"));
        prepareEdit();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ActivityCodeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ActivityCodeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ActivityCodeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ActivityCode> getItems() {
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
                     if (persistAction == PersistAction.UPDATE || persistAction == PersistAction.CREATE) {
                        selected.setSubcontractorPrice(Float.valueOf(subcontractorPrice));
                        selected.setVendorPrice(Float.valueOf(vendorPrice));
                        selected.setUm(um);
                        selected.setUmPercent(umPercent);
                    }
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

    public ActivityCode getActivityCode(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<ActivityCode> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ActivityCode> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(value="ActivityCodeControllerConverter")
    public static class ActivityCodeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ActivityCodeController controller = (ActivityCodeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "activityCodeController");
            return controller.getActivityCode(getKey(value));
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
            if (object instanceof ActivityCode) {
                ActivityCode o = (ActivityCode) object;
                return getStringKey(o.getMaterialId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ActivityCode.class.getName()});
                return null;
            }
        }

    }

     public List<ActivityCode> autoCompleteActivityCodeMaterialID(String query) {
        List<ActivityCode> allCodes = getItems();
        List<ActivityCode> filteredCodes = new ArrayList<>();
         
        for (int i = 0; i < allCodes.size(); i++) {
            ActivityCode code = allCodes.get(i);
            if(code.getMaterialId().toLowerCase().startsWith(query)) {
                filteredCodes.add(code);
            }
        }
         
        return filteredCodes;
    }
    
    public List<ActivityCode> autoCompleteActivityCodeDescription(String query) {
        List<ActivityCode> allCodes = getItems();
        List<ActivityCode> filteredCodes = new ArrayList<>();
         
        for (int i = 0; i < allCodes.size(); i++) {
            ActivityCode code = allCodes.get(i);
            if( code.getDescription().toLowerCase().contains(query)) {
                filteredCodes.add(code);
            }
        }
         
        return filteredCodes;
    }
    
     public String getSubcontractorPrice() {
        return subcontractorPrice;
    }

    public void setSubcontractorPrice(String subcontractorPrice) {
        this.subcontractorPrice = subcontractorPrice;
    }

    public String getVendorPrice() {
        return vendorPrice;
    }

    public void setVendorPrice(String vendorPrice) {
        this.vendorPrice = vendorPrice;
    }

    public Float getUmPercent() {
        return umPercent;
    }

    public void setUmPercent(Float umPercent) {
        this.umPercent = umPercent;
    }

    public Float getUm() {
        return um;
    }

    public void setUm(Float um) {
        this.um = um;
    }
    
    public void prepareEdit(){
        um=selected.getUm();
        umPercent=selected.getUmPercent();
        if(selected.getVendorPrice()!=null){
            vendorPrice=selected.getVendorPrice().toString();
        }else{
            vendorPrice=null;
        }
        if(selected.getSubcontractorPrice()!=null){
            subcontractorPrice=selected.getSubcontractorPrice().toString();
        }else{
            subcontractorPrice=null;
        }
    }
    
    public void autoCalculateMargin(){
        if(getVendorPrice()!=null){
            if(getSubcontractorPrice()!=null){
                if(Float.valueOf(vendorPrice)==0){
                    um=Float.valueOf("0");
                    umPercent=Float.valueOf("0");
                }else if(Float.valueOf(subcontractorPrice)==0){
                    um=Float.valueOf(vendorPrice);
                    umPercent=Float.valueOf("100");
                }else{
                    um=Float.valueOf(vendorPrice)-Float.valueOf(subcontractorPrice);
                    umPercent=um/Float.valueOf(vendorPrice);
                }
                
            }else{
                um=Float.valueOf("0");
                umPercent=Float.valueOf("0");
            }
        }else{
            um=Float.valueOf("0");
            umPercent=Float.valueOf("0");
        }
        
//        selected.setUm(um);
//        selected.setUmPercent(umPercent);
    }
    
     public void uploadActivityCodes(FileUploadEvent event){
        try {
            uploadedItems=ActivityCodeLoader.readFile(event.getFile().getInputstream());
            if(uploadedItems!=null?uploadedItems.size()>0:false){
                persistAll(uploadedItems);
                JsfUtil.addSuccessMessage(uploadedItems.size()+" ActivityCodes Successfully Uploaded.");
            }else{
                JsfUtil.addErrorMessage("No ActivityCode Items Found");
                
            }
        } catch (IOException ex) {
            Logger.getLogger(ActivityCodeController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, "File Upload Failed");
        }
        
    }

      @FacesConverter(value = "NumbersConverter")
    public static class NumbersConverter implements Converter{
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if(value!=null){
                Float obj=Float.valueOf(value);
                return obj;
            }
            return null;
        }
        
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if(object!=null){
                Float floatValue= (Float) object;
                return floatValue.toString();
                
            }
            return null;
        }
    }


}
