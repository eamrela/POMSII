package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.VendorMd;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.VendorMdFacade;
import com.vodafone.poms.ii.entities.VendorInvoice;

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

@Named("vendorMdController")
@SessionScoped
public class VendorMdController implements Serializable {
    
    private Double editMdFactor;
    private BigInteger editMdValue;
    private BigInteger editMdDeserved;
    private Date editMdDate;
    private String editMdNumber;
    private BigInteger editRemainingInMd;
    private BigInteger selectedMdInvoiceValue;
    private BigInteger selectedPoInvoiceValue;
    private Boolean editInvoiced;
    @EJB
    private com.vodafone.poms.ii.beans.VendorMdFacade ejbFacade;
    private List<VendorMd> items = null;
    private List<VendorMd> selectedPoItems = null;
    private VendorMd selected;
    
    
    @Inject
    private VendorInvoiceController vendorInvoiceController;
    @Inject
    private UsersController usersController;
    @Inject
    private VendorPoController vendorPoController;

    public VendorMdController() {
    }

    public VendorMd getSelected() {
        return selected;
    }

    public void setSelected(VendorMd selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VendorMdFacade getFacade() {
        return ejbFacade;
    }

    public VendorMd prepareCreate() {
        selected = new VendorMd();
        initializeEmbeddableKey();
        return selected;
    }

    public void prepareEdit(){
       if(selected!=null){
       editMdFactor = selected.getMdFactor();
       editMdValue = selected.getMdValue();
       editMdDate = selected.getMdDate();
       editMdNumber = selected.getMdNumber();
       editInvoiced = selected.getInvoiced();
       editMdDeserved=selected.getMdDeserved();
       }
    }
    public VendorMd create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VendorMdCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            selectedPoItems = null;
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VendorMdUpdated"));
    }

    public void updateEdit() {
        if(editMdDate!=null && editMdFactor!=null && editMdValue!=null){
            if(editMdDeserved.compareTo(editMdValue)==1
                    || editMdDeserved.compareTo(editMdValue)==0){
            selected.setMdDate(editMdDate);
            selected.setMdFactor(editMdFactor);
            selected.setMdValue(editMdValue);
            selected.setInvoiced(editInvoiced);
            selected.setMdNumber(editMdNumber);
            selected.setRemainingInMd(editRemainingInMd);
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VendorMdUpdated"));
            }else{
                JsfUtil.addErrorMessage("MD Value is more than the amount deserved.");
            }
        }
    }
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VendorMdDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            selectedPoItems = null;
        }
    }

    public List<VendorMd> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<VendorMd> getSelectedPoItems() {
        if(vendorPoController.getSelected()!=null){
            selectedPoItems = getFacade().findRelatedItems(vendorPoController.getSelected());
        }
        return selectedPoItems;
    }

    
    private VendorMd persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    selected = getFacade().merge(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
                return selected;
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
        return null;
    }

    public VendorMd getVendorMd(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<VendorMd> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<VendorMd> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<VendorMd> getDashboardItems(String domains) {
        return getFacade().findDashboardItems(domains);
    }

    public BigInteger getSelectedMdInvoiceValue() {
        selectedMdInvoiceValue = BigInteger.ZERO;
        BigInteger totalInvoices = BigInteger.ZERO;
        if(selected!=null){
        List<VendorInvoice> invoices = vendorInvoiceController.getSelectedMdItems();
        for (int i = 0; i < invoices.size(); i++) {
            if(invoices.get(i).getInvoiceValue()!=null)
            totalInvoices = totalInvoices.add(invoices.get(i).getInvoiceValue());
        }
        if(selected.getMdValue()!=null){
            selectedMdInvoiceValue = selected.getMdValue().subtract(totalInvoices);
        }
        }
        return selectedMdInvoiceValue;
    }
    
    public BigInteger getSelectedPoInvoiceValue() {
        selectedPoInvoiceValue = BigInteger.ZERO;
        BigInteger totalInvoices = BigInteger.ZERO;
        BigInteger totalMdValue = BigInteger.ZERO;
        if(vendorPoController.getSelected()!=null){
        List<VendorMd> mds = getSelectedPoItems();
        for (VendorMd md : mds) {
            if(md.getMdValue()!=null){
                totalMdValue = totalMdValue.add(md.getMdValue());
            }
            List<VendorInvoice> invoices = vendorInvoiceController.getSelectedMdItems(md);
            for (VendorInvoice invoice : invoices) {
                if(invoice.getInvoiceValue()!=null){
                    totalInvoices = totalInvoices.add(invoice.getInvoiceValue());
                }
            }
        }
        selectedPoInvoiceValue = totalMdValue.subtract(totalInvoices);
        }
        return selectedPoInvoiceValue;
    }

    

    

    @FacesConverter(forClass = VendorMd.class)
    public static class VendorMdControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VendorMdController controller = (VendorMdController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vendorMdController");
            return controller.getVendorMd(getKey(value));
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
            if (object instanceof VendorMd) {
                VendorMd o = (VendorMd) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), VendorMd.class.getName()});
                return null;
            }
        }

    }

    public Boolean getEditInvoiced() {
        return editInvoiced;
    }

    public Date getEditMdDate() {
        return editMdDate;
    }

    public BigInteger getEditMdDeserved() {
        return editMdDeserved;
    }

    public Double getEditMdFactor() {
        return editMdFactor;
    }

    public String getEditMdNumber() {
        return editMdNumber;
    }

    public BigInteger getEditMdValue() {
        return editMdValue;
    }

    public BigInteger getEditRemainingInMd() {
        return editRemainingInMd;
    }

    public void setEditInvoiced(Boolean editInvoiced) {
        this.editInvoiced = editInvoiced;
    }

    public void setEditMdDate(Date editMdDate) {
        this.editMdDate = editMdDate;
    }

    public void setEditMdDeserved(BigInteger editMdDeserved) {
        this.editMdDeserved = editMdDeserved;
    }

    public void setEditMdFactor(Double editMdFactor) {
        this.editMdFactor = editMdFactor;
        calculateValue();
        calculateRemaining();
    }

    public void setEditMdNumber(String editMdNumber) {
        this.editMdNumber = editMdNumber;
    }

    public void setEditMdValue(BigInteger editMdValue) {
        this.editMdValue = editMdValue;
        calculateFactor();
        calculateRemaining();
    }

    public void setEditRemainingInMd(BigInteger editRemainingInMd) {
        this.editRemainingInMd = editRemainingInMd;
    }

    private void calculateValue() {
        if(editMdFactor!=null)
        editMdValue = BigInteger.valueOf(BigDecimal.valueOf(editMdFactor).multiply(BigDecimal.valueOf(editMdDeserved.intValue())).intValue());
    }
    
    private void calculateFactor() {
        if(editMdValue!=null)
        editMdFactor = Double.valueOf(editMdValue.floatValue()/editMdDeserved.floatValue());
    }
    
    private void calculateRemaining() {
        if(editMdValue!=null)
          editRemainingInMd = editMdDeserved.subtract(editMdValue);
    }
    
    public void clearSelected(){
        selected = null;
        selectedMdInvoiceValue = BigInteger.ZERO;
    }
    
   
    public void createInvoice(){
        if(selected!=null){
            if(getSelectedMdInvoiceValue().compareTo(BigInteger.ZERO)==1){
                vendorInvoiceController.prepareCreate();
                vendorInvoiceController.getSelected().setMdId(selected);
                vendorInvoiceController.getSelected().setCreator(usersController.getLoggedInUser());
                vendorInvoiceController.getSelected().setInvoiceDeserved(getSelectedMdInvoiceValue());
                vendorInvoiceController.getSelected().setSysDate(new Date());
                vendorInvoiceController.create();
                selected.setInvoiced(Boolean.TRUE);
                update();
        }
    }
    }
}
