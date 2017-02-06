package com.vodafone.poms.ii.controllers;

import com.vodafone.poms.ii.entities.UsersRoles;
import com.vodafone.poms.ii.beans.UsersRolesFacade;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.controllers.util.JsfUtil.PersistAction;


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
import javax.inject.Inject;

@Named("usersRolesController")
@SessionScoped
public class UsersRolesController implements Serializable {

    @EJB
    private com.vodafone.poms.ii.beans.UsersRolesFacade ejbFacade;
    private List<UsersRoles> items = null;
    private UsersRoles selected;
    private ArrayList<String> rolesList;

    @Inject
    UsersController usersController;
    
    public UsersRolesController() {
        rolesList= new ArrayList<>();
        rolesList.add("ROLE_ADMIN");
        rolesList.add("ROLE_ADMIN");
        rolesList.add("ROLE_FINANCEADMIN");
        rolesList.add("ROLE_BUSINESSPROVIDER");
        rolesList.add("ROLE_SYSADMIN");
    }

    public ArrayList<String> getRolesList() {
        return rolesList;
    }

    public void setRolesList(ArrayList<String> rolesList) {
        this.rolesList = rolesList;
    }

    
    public UsersRoles getSelected() {
        return selected;
    }

    public void setSelected(UsersRoles selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UsersRolesFacade getFacade() {
        return ejbFacade;
    }

     public void resetSelection(){
        System.out.println("Resetting UsersRoles Selection");
        selected=null;
        usersController.setSelected(null);
    }
     
    public UsersRoles prepareCreate() {
        selected = new UsersRoles();
        usersController.prepareCreate();
        selected.setUsername(usersController.getSelected());
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, "Created");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

     public void createNewUserRole() {
        usersController.getSelected().setEnabled(Short.valueOf("1"));
        usersController.create();
        persist(PersistAction.CREATE, "Created");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    

    public void updateUsersRoles() {
        usersController.update();
        persist(PersistAction.UPDATE, "Updated");
    }
    public void update() {
        persist(PersistAction.UPDATE, "Updated");
    }

    public void prepareEdit(){
        usersController.setSelected(selected.getUsername());
    }
    
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UsersRolesDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<UsersRoles> getItems() {
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

    public UsersRoles getUsersRoles(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<UsersRoles> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<UsersRoles> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = UsersRoles.class)
    public static class UsersRolesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsersRolesController controller = (UsersRolesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usersRolesController");
            return controller.getUsersRoles(getKey(value));
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
            if (object instanceof UsersRoles) {
                UsersRoles o = (UsersRoles) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), UsersRoles.class.getName()});
                return null;
            }
        }

    }

}
