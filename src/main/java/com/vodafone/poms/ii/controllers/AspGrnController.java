package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.AspGrn;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.AspGrnFacade;

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

@Named("aspGrnController")
@SessionScoped
public class AspGrnController implements Serializable {

    private Double editGrnFactor;
    private BigDecimal editGrnValue;
    private BigDecimal editGrnDeserved;
    private Date editGrnDate;
    private String editGrnNumber;
    private BigDecimal editRemainingInGrn;
    private Boolean editInvoiced;
    @EJB
    private com.vodafone.poms.ii.beans.AspGrnFacade ejbFacade;
    private List<AspGrn> items = null;
    private List<AspGrn> selectedPoItems = null;
    private AspGrn selected;
    
    @Inject
    private AspPoController aspPoController;
    
    public AspGrnController() {
    }

    public AspGrn getSelected() {
        return selected;
    }

    public void setSelected(AspGrn selected) {
        this.selected = selected;
    }

   protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AspGrnFacade getFacade() {
        return ejbFacade;
    }

    public AspGrn prepareCreate() {
        selected = new AspGrn();
        initializeEmbeddableKey();
        return selected;
    }
    
    public void prepareEdit(){
       if(selected!=null){
       editGrnFactor = selected.getGrnFactor();
       editGrnValue = selected.getGrnValue();
       editGrnDate = selected.getGrnDate();
       editGrnNumber = selected.getGrnNumber();
       editInvoiced = selected.getInvoiced();
       editGrnDeserved=selected.getGrnDeserved();
       }
    }
    
    public AspGrn create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AspGrnCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            selectedPoItems = null;
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AspGrnUpdated"));
    }
    
    public void updateEdit() {
        if(editGrnDate!=null && editGrnFactor!=null && editGrnValue!=null){
            if(editGrnDeserved.compareTo(editGrnValue)==1
                    || editGrnDeserved.compareTo(editGrnValue)==0){
            selected.setGrnDate(editGrnDate);
            selected.setGrnFactor(editGrnFactor);
            selected.setGrnValue(editGrnValue);
            selected.setInvoiced(editInvoiced);
            selected.setGrnNumber(editGrnNumber);
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AspGrnUpdated"));
            }else{
                JsfUtil.addErrorMessage("GRN Value is more than the amount deserved");
            }
        }
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AspGrnDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            selectedPoItems = null;
        }
    }

    public List<AspGrn> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<AspGrn> getSelectedPoItems() {
        if(aspPoController.getSelected()!=null){
            selectedPoItems = getFacade().findRelatedItems(aspPoController.getSelected());
        }
        return selectedPoItems;
    }
    
    private AspGrn persist(PersistAction persistAction, String successMessage) {
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

    public AspGrn getAspGrn(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AspGrn> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AspGrn> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<AspGrn> getDashboardItems(Date start, Date end,String domains) {
        return getFacade().findDashboardItems(start,end,domains);
    }

    @FacesConverter(forClass = AspGrn.class)
    public static class AspGrnControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AspGrnController controller = (AspGrnController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "aspGrnController");
            return controller.getAspGrn(getKey(value));
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
            if (object instanceof AspGrn) {
                AspGrn o = (AspGrn) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AspGrn.class.getName()});
                return null;
            }
        }

    }

    public Date getEditGrnDate() {
        return editGrnDate;
    }

    public Double getEditGrnFactor() {
        return editGrnFactor;
    }

    public String getEditGrnNumber() {
        return editGrnNumber;
    }

    public BigDecimal getEditGrnValue() {
        return editGrnValue;
    }

    public Boolean getEditInvoiced() {
        return editInvoiced;
    }

    public void setEditGrnDate(Date editGrnDate) {
        this.editGrnDate = editGrnDate;
    }

    public void setEditGrnFactor(Double editGrnFactor) {
        this.editGrnFactor = editGrnFactor;
        calculateValue();
        calculateRemaining();
    }

    public void setEditGrnNumber(String editGrnNumber) {
        this.editGrnNumber = editGrnNumber;
    }

    public void setEditGrnValue(BigDecimal editGrnValue) {
        this.editGrnValue = editGrnValue;
        calculateFactor();
        calculateRemaining();
    }

    public void setEditInvoiced(Boolean editInvoiced) {
        this.editInvoiced = editInvoiced;
    }

    public BigDecimal getEditRemainingInGrn() {
        return editRemainingInGrn;
    }

    public void setEditRemainingInGrn(BigDecimal editRemainingInGrn) {
        this.editRemainingInGrn = editRemainingInGrn;
    }

    

    private void calculateValue() {
        if(editGrnFactor!=null)
        editGrnValue = BigDecimal.valueOf(BigDecimal.valueOf(editGrnFactor).multiply(BigDecimal.valueOf(editGrnDeserved.intValue())).intValue());
    }
    
    private void calculateFactor() {
        if(editGrnValue!=null)
        editGrnFactor = Double.valueOf(editGrnValue.floatValue()/editGrnDeserved.floatValue());
    }
    
    private void calculateRemaining() {
        if(editGrnValue!=null)
          editRemainingInGrn = editGrnDeserved.subtract(editGrnValue);
    }
    
}
