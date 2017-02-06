package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.VendorPo;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;
import com.vodafone.poms.ii.beans.VendorPoFacade;
import com.vodafone.poms.ii.entities.Taxes;
import com.vodafone.poms.ii.entities.VendorMd;
import java.io.IOException;
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
import org.joda.time.DateTime;

@Named("vendorPoController")
@SessionScoped
public class VendorPoController implements Serializable {
    
    private BigDecimal selectedMdDeserved;
    private Date startMonth;
    private Integer numberOfMonths;
    
    @EJB
    private com.vodafone.poms.ii.beans.VendorPoFacade ejbFacade;
    private List<VendorPo> items = null;
    private VendorPo selected;
    
    @Inject
    private ActivityController activityController;
    @Inject
    private TaxesController taxesController;
    @Inject
    private UsersController usersController;
    @Inject
    private PoStatusController poStatusController;
    @Inject
    private VendorMdController vendorMdController;
    @Inject
    private AspPoController aspPoController;
    @Inject
    private PoTypesController poTypesController;
    
    public VendorPoController() {
    }

    public VendorPo getSelected() {
        return selected;
    }

    public void setSelected(VendorPo selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VendorPoFacade getFacade() {
        return ejbFacade;
    }

    public VendorPo prepareCreate() {
        selected = new VendorPo();
        selected.setTaxes(taxesController.getCurrentTaxes());
        selected.setCreator(usersController.getLoggedInUser());
        selected.setSysDate(new Date());
        selected.setPoStatus(poStatusController.getInitialStatus());
        selected.setPoType(poTypesController.getPoTypes("Extra Work"));
        initializeEmbeddableKey();
        return selected;
    }
    
    public VendorPo prepareCreateService() {
        selected = new VendorPo();
        selected.setTaxes(taxesController.getCurrentTaxes());
        selected.setCreator(usersController.getLoggedInUser());
        selected.setSysDate(new Date());
        selected.setPoStatus(poStatusController.getInitialStatus());
        selected.setPoType(poTypesController.getPoTypes("Service"));
        initializeEmbeddableKey();
        startMonth = null;
        numberOfMonths = null;
        return selected;
    }

    public void create() {
        if(selected.getRemainingInPo()==null){
                selected.setRemainingInPo(selected.getPoValue());
        }
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VendorPoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        prepareCreate();
         try {   
            FacesContext.getCurrentInstance().getExternalContext().redirect("/POMS-II/app/finance_admin/crud_v_po.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ActivityController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createServicePo(){
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
            persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VendorPoCreated"));
            if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            }
        }
        
        prepareCreate();
        prepareCreateService();
        
         try {   
            FacesContext.getCurrentInstance().getExternalContext().redirect("/POMS-II/app/finance_admin/crud_v_po.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ActivityController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VendorPoUpdated"));
    }

    public void closePO(){
        if(selected!=null){
            selected.setPoStatus(poStatusController.getFinalStatus());
            update();
            items = null;
        }
    }
    
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VendorPoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            selectedMdDeserved = null;
        }
    }

    public List<VendorPo> getItems() {
        if (items == null) {
            items = getFacade().findAllOpen();
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

    public VendorPo getVendorPo(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<VendorPo> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<VendorPo> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<VendorPo> getExportItems(Date fromDate, Date toDate) {
        return getFacade().findExportItems(fromDate,toDate);
    }

    public List<VendorPo> findAll(){
        return getFacade().findAll();
    }
    
    public List<VendorPo> getDashboardRemainingNotInvoiced(Date start, Date end) {
        return getFacade().findRemainingNotYetinvoiced(start,end);
    }

    public List<VendorPo> getDashboardMDNotYetGenerated(Date start, Date end) {
        return getFacade().findMdNotYetGenerated(start,end);
    }

    @FacesConverter(forClass = VendorPo.class)
    public static class VendorPoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VendorPoController controller = (VendorPoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vendorPoController");
            return controller.getVendorPo(getKey(value));
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
            if (object instanceof VendorPo) {
                VendorPo o = (VendorPo) object;
                return getStringKey(o.getPoNumber());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), VendorPo.class.getName()});
                return null;
            }
        }

    }

    public void createMd(){
        if(selected!=null){
            if(selected.getMdDeserved().compareTo(BigDecimal.ZERO)==1){
                vendorMdController.prepareCreate();
                vendorMdController.getSelected().setVendorPoId(selected);
                vendorMdController.getSelected().setCreator(usersController.getLoggedInUser());
                vendorMdController.getSelected().setMdDeserved(selected.getMdDeserved());
                vendorMdController.getSelected().setSysDate(new Date());
                vendorMdController.create();
                update();
                
            }
        }
    }
    
    public List<VendorPo> findMatchingPOForASP(){
        List<VendorPo> suggestedPOs = new ArrayList<>();
        if(aspPoController.getSelectedItems()!=null){
            if(aspPoController.getSelectedItems().size()>0){
                if(aspPoController.getSelectedItems().get(0).getPoType().getTypeName().equals("Service")){
            suggestedPOs = getFacade().findPOforASPService(aspPoController.getSelectedItems());
                }else{
            suggestedPOs = getFacade().findPOforASPExtraWork(aspPoController.getSelectedItems());
                }
            }
        }
        return suggestedPOs;
    }

    public BigDecimal getSelectedMdDeserved() {
        selectedMdDeserved = BigDecimal.ZERO;
        if(selected!=null){
            BigDecimal totalMdValue = BigDecimal.ZERO;
            BigDecimal totalMdDeserved = BigDecimal.valueOf(selected.getServiceValue().floatValue()*selected.getWorkDone());
            List<VendorMd> mds = vendorMdController.getSelectedPoItems();
        for (int i = 0; i < mds.size(); i++) {
            totalMdValue  = totalMdValue.add(mds.get(i).getMdValue()!=null?
                                    mds.get(i).getMdValue():BigDecimal.ZERO);
        }
            selected.setMdDeserved(totalMdDeserved.subtract(totalMdValue));
            selectedMdDeserved = selected.getMdDeserved();
        }
        return selectedMdDeserved;
    }

    public void setSelectedMdDeserved(BigDecimal selectedMdDeserved) {
        this.selectedMdDeserved = selectedMdDeserved;
    }
    
    public List<VendorPo> findMatchingVendorPOForActivity(){
        List<VendorPo> suggestedPOs = new ArrayList<>();
        if(activityController.getSelectedItems()!=null){
            suggestedPOs = getFacade().findPOforActivity(activityController.getSelectedItems());
        }
        return suggestedPOs;
    }
    
    public void clearSelected(){
        selected=null;
        items = null;
        selectedMdDeserved = null;
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
