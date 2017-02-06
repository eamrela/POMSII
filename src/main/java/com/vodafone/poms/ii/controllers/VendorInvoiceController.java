package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.VendorInvoice;
import com.vodafone.poms.ii.beans.VendorInvoiceFacade;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.entities.DomainNames;
import com.vodafone.poms.ii.entities.VendorMd;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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

@Named("vendorInvoiceController")
@SessionScoped
public class VendorInvoiceController implements Serializable {

    private Double editinvoiceFactor;
    private BigDecimal editInvoiceValue;
    private BigDecimal editInvoiceDeserved;
    private Date editInvoiceDate;
    private String editInvoiceNumber;
    private BigDecimal editRemainingInInvoice;
    private Boolean editInvoiced;
    @EJB
    private com.vodafone.poms.ii.beans.VendorInvoiceFacade ejbFacade;
    private List<VendorInvoice> items = null;
    private List<VendorInvoice> selectedMdItems = null;
    private VendorInvoice selected;
    
    @Inject 
    private VendorMdController vendorMdController;

    public VendorInvoiceController() {
    }

    public VendorInvoice getSelected() {
        return selected;
    }

    public void setSelected(VendorInvoice selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VendorInvoiceFacade getFacade() {
        return ejbFacade;
    }

    public VendorInvoice prepareCreate() {
        selected = new VendorInvoice();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VendorInvoiceCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            selectedMdItems = null;
        }
    }

    public void update() {
         persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VendorInvoiceUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VendorInvoiceDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            selectedMdItems = null;
        }
    }

    public List<VendorInvoice> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<VendorInvoice> getSelectedMdItems() {
        if(vendorMdController.getSelected()!=null){
            selectedMdItems = getFacade().findRelatedItems(vendorMdController.getSelected());
        }
        return selectedMdItems;
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

    public VendorInvoice getVendorInvoice(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<VendorInvoice> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<VendorInvoice> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<VendorInvoice> findDashboardItems(Date start, Date end,String domains) {
        return getFacade().findDashboardItems(start,end,domains);
    }

    List<VendorInvoice> getSelectedMdItems(VendorMd md) {
            return getFacade().findRelatedItems(md);
    }

    @FacesConverter(forClass = VendorInvoice.class)
    public static class VendorInvoiceControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VendorInvoiceController controller = (VendorInvoiceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vendorInvoiceController");
            return controller.getVendorInvoice(getKey(value));
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
            if (object instanceof VendorInvoice) {
                VendorInvoice o = (VendorInvoice) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), VendorInvoice.class.getName()});
                return null;
            }
        }

    }
    
    public void clearSelected(){
        selected = null;
    }

    public void setEditInvoiceDate(Date editInvoiceDate) {
        this.editInvoiceDate = editInvoiceDate;
    }

    public void setEditInvoiceDeserved(BigDecimal editInvoiceDeserved) {
        this.editInvoiceDeserved = editInvoiceDeserved;
    }

    public void setEditInvoiceNumber(String editInvoiceNumber) {
        this.editInvoiceNumber = editInvoiceNumber;
    }

    public void setEditInvoiceValue(BigDecimal editInvoiceValue) {
        this.editInvoiceValue = editInvoiceValue;
        calculateFactor();
        calculateRemaining();
    }

    public void setEditInvoiced(Boolean editInvoiced) {
        this.editInvoiced = editInvoiced;
    }

    public void setEditRemainingInInvoice(BigDecimal editRemainingInInvoice) {
        this.editRemainingInInvoice = editRemainingInInvoice;
    }

    public void setEditinvoiceFactor(Double editinvoiceFactor) {
        this.editinvoiceFactor = editinvoiceFactor;
        calculateValue();
        calculateRemaining();
    }

    public Date getEditInvoiceDate() {
        return editInvoiceDate;
    }

    public BigDecimal getEditInvoiceDeserved() {
        return editInvoiceDeserved;
    }

    public String getEditInvoiceNumber() {
        return editInvoiceNumber;
    }

    public BigDecimal getEditInvoiceValue() {
        return editInvoiceValue;
    }

    public Boolean getEditInvoiced() {
        return editInvoiced;
    }

    public BigDecimal getEditRemainingInInvoice() {
        return editRemainingInInvoice;
    }

    public Double getEditinvoiceFactor() {
        return editinvoiceFactor;
    }
    
    private void calculateValue() {
        if(editinvoiceFactor!=null)
        editInvoiceValue = BigDecimal.valueOf(BigDecimal.valueOf(editinvoiceFactor).multiply(BigDecimal.valueOf(editInvoiceDeserved.intValue())).intValue());
    }
    
    private void calculateFactor() {
        if(editInvoiceValue!=null)
        editinvoiceFactor = Double.valueOf(editInvoiceValue.floatValue()/editInvoiceDeserved.floatValue());
    }
    
    private void calculateRemaining() {
        if(editInvoiceValue!=null)
          editRemainingInInvoice = editInvoiceDeserved.subtract(editInvoiceValue);
    }
    
    public void prepareEdit(){
       if(selected!=null){
       editinvoiceFactor = selected.getInvoiceFactor();
       editInvoiceValue = selected.getInvoiceValue();
       editInvoiceDate = selected.getInvoiceDate();
       editInvoiceNumber = selected.getInvoiceNumber();
       editInvoiced = selected.getInvoiced();
       editInvoiceDeserved=selected.getInvoiceDeserved();
       }
    }
    
    public void updateEdit() {
        if(editInvoiceDate!=null && editinvoiceFactor!=null && editInvoiceValue!=null){
            if(editInvoiceDeserved.compareTo(editInvoiceValue)==1
                    || editInvoiceDeserved.compareTo(editInvoiceValue)==0){
            selected.setInvoiceDate(editInvoiceDate);
            selected.setInvoiceFactor(editinvoiceFactor);
            selected.setInvoiceValue(editInvoiceValue);
            selected.setInvoiced(editInvoiced);
            selected.setInvoiceNumber(editInvoiceNumber);
            selected.setRemainingInInvoice(editRemainingInInvoice);
            update();
//            vendorMdController.update();
            }else{
                JsfUtil.addErrorMessage("Invoice Value is more than the amount deserved.");
            }
        }
    }
   
}
