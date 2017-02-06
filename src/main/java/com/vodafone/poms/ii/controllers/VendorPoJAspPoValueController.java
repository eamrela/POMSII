package com.vodafone.poms.ii.controllers;



import com.vodafone.poms.ii.beans.VendorPoJAspPoValueFacade;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.VendorPo;
import com.vodafone.poms.ii.entities.VendorPoJAspPoValue;
import com.vodafone.poms.ii.entities.VendorPoJAspPoValuePK;
import java.io.Serializable;
import java.math.BigDecimal;
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

@Named("vendorPoJAspPoValueController")
@SessionScoped
public class VendorPoJAspPoValueController implements Serializable {

    @EJB
    private VendorPoJAspPoValueFacade ejbFacade;
    private List<VendorPoJAspPoValue> items = null;
    private VendorPoJAspPoValue selected;

    public VendorPoJAspPoValueController() {
    }

    public VendorPoJAspPoValue getSelected() {
        return selected;
    }

    public void setSelected(VendorPoJAspPoValue selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getVendorPoJAspPoValuePK().setAspPoId(selected.getAspPo().getPoNumber());
        selected.getVendorPoJAspPoValuePK().setVendorPoId(selected.getVendorPo().getPoNumber());
    }

    protected void initializeEmbeddableKey() {
        selected.setVendorPoJAspPoValuePK(new VendorPoJAspPoValuePK());
    }

    private VendorPoJAspPoValueFacade getFacade() {
        return ejbFacade;
    }

    public VendorPoJAspPoValue prepareCreate() {
        selected = new VendorPoJAspPoValue();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, "Amount to be taken Created");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, "Amount taken is now updated");
    }

    public void destroy() {
        persist(PersistAction.DELETE, "Value taken is now deleted");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<VendorPoJAspPoValue> getItems() {
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

    public VendorPoJAspPoValue getVendorPoJAspPoValue(VendorPoJAspPoValuePK id) {
        return getFacade().find(id);
    }

    public List<VendorPoJAspPoValue> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<VendorPoJAspPoValue> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public BigDecimal getValue(AspPo selected) {
        List<VendorPoJAspPoValue> selectedItems = getFacade().findAllItems(selected.getPoNumber());
        BigDecimal totalCorrrelatedValue = new BigDecimal("0.0");
        for (int i = 0; i < selectedItems.size(); i++) {
            totalCorrrelatedValue = totalCorrrelatedValue.add(selectedItems.get(i).getValueToTake());
        }
        return totalCorrrelatedValue;
    }

    public List<VendorPoJAspPoValue> getSelectedItems(VendorPo get) {
       return getFacade().findVendorItems(get.getPoNumber());
    }

    public void merge() {
        persist(PersistAction.CREATE, "Amount to be taken Created");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    void createOrIncrease() {
        if(selected!=null){
            VendorPoJAspPoValue previousItem = null;
            if((previousItem = getFacade().findById(selected.getVendorPo().getPoNumber(),selected.getAspPo().getPoNumber()))!=null){
                selected.setValueToTake(selected.getValueToTake().add(previousItem.getValueToTake()));
                update();
            }
            merge();
        }
    }

    public VendorPoJAspPoValue findById(String poNumber, String poNumber0) {
        return getFacade().findById(poNumber, poNumber0);
    }

    @FacesConverter(forClass = VendorPoJAspPoValue.class)
    public static class VendorPoJAspPoValueControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VendorPoJAspPoValueController controller = (VendorPoJAspPoValueController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vendorPoJAspPoValueController");
            return controller.getVendorPoJAspPoValue(getKey(value));
        }

        VendorPoJAspPoValuePK getKey(String value) {
            VendorPoJAspPoValuePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new VendorPoJAspPoValuePK();
            key.setVendorPoId(values[0]);
            key.setAspPoId(values[1]);
            return key;
        }

        String getStringKey(VendorPoJAspPoValuePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getVendorPoId());
            sb.append(SEPARATOR);
            sb.append(value.getAspPoId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof VendorPoJAspPoValue) {
                VendorPoJAspPoValue o = (VendorPoJAspPoValue) object;
                return getStringKey(o.getVendorPoJAspPoValuePK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), VendorPoJAspPoValue.class.getName()});
                return null;
            }
        }

    }

}
