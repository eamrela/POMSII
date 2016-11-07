package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.VendorMd;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.VendorMdFacade;

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

@Named("vendorMdController")
@SessionScoped
public class VendorMdController implements Serializable {
    
    private Double editMdFactor;
    private BigInteger editMdValue;
    private BigInteger editMdDeserved;
    private Date editMdDate;
    private String editMdNumber;
    private BigInteger editRemainingInMd;
    private Boolean editInvoiced;
    @EJB
    private com.vodafone.poms.ii.beans.VendorMdFacade ejbFacade;
    private List<VendorMd> items = null;
    private VendorMd selected;

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
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VendorMdUpdated"));
    }

    public void updateEdit() {
        if(editMdDate!=null && editMdFactor!=null && editMdValue!=null){
            if(getFacade().findByMdNumber(editMdNumber).isEmpty()){
            selected.setMdDate(editMdDate);
            selected.setMdFactor(editMdFactor);
            selected.setMdValue(editMdValue);
            selected.setInvoiced(editInvoiced);
            selected.setMdNumber(editMdNumber);
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AspGrnUpdated"));
            }else{
                JsfUtil.addErrorMessage("GRN Number already exist");
            }
        }
    }
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VendorMdDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<VendorMd> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
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
}
