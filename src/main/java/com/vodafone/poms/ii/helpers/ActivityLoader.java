/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.helpers;

import com.vodafone.poms.ii.controllers.ActivityCodeController;
import com.vodafone.poms.ii.controllers.ActivityController;
import com.vodafone.poms.ii.controllers.ApprovalStatusController;
import com.vodafone.poms.ii.controllers.AreaController;
import com.vodafone.poms.ii.controllers.ClaimStatusController;
import com.vodafone.poms.ii.controllers.DomainNamesController;
import com.vodafone.poms.ii.controllers.PhasesController;
import com.vodafone.poms.ii.controllers.SitesController;
import com.vodafone.poms.ii.controllers.SubcontractorsController;
import com.vodafone.poms.ii.controllers.UsersController;
import com.vodafone.poms.ii.controllers.VendorOwnerController;
import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.ActivityCode;
import com.vodafone.poms.ii.entities.ApprovalStatus;
import com.vodafone.poms.ii.entities.Area;
import com.vodafone.poms.ii.entities.ClaimStatus;
import com.vodafone.poms.ii.entities.DomainNames;
import com.vodafone.poms.ii.entities.Phases;
import com.vodafone.poms.ii.entities.Sites;
import com.vodafone.poms.ii.entities.Subcontractors;
import com.vodafone.poms.ii.entities.Users;
import com.vodafone.poms.ii.entities.VendorOwner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Amr
 */
@Named("activityUploader")
@SessionScoped
public class ActivityLoader implements Serializable{
    
    @Inject
    private SitesController sitesController;
    @Inject
    private ClaimStatusController claimStatusController;
    @Inject
    private SubcontractorsController subContractorsController;
    @Inject
    private AreaController areaController;
    @Inject
    private VendorOwnerController vendorOwnerController;
    @Inject
    private ApprovalStatusController approvalStatusController;
    @Inject
    private PhasesController phasesController;
    @Inject
    private DomainNamesController domainNamesController;
    @Inject
    private ActivityCodeController activityCodeController;
    @Inject
    private UsersController usersController;
    @Inject
    private ActivityController activityController;
    
     public void readFile(FileUploadEvent event){
        if(event.getFile() != null) {
        Activity activity = null;
        List<Activity> activities = new ArrayList<>();
        try {
        
        XSSFWorkbook myWorkBook = new XSSFWorkbook (event.getFile().getInputstream());
        int numberOfSheets= myWorkBook.getNumberOfSheets();
        XSSFSheet sheet = null;
        System.out.println(numberOfSheets);
            for (int i = 0; i < numberOfSheets; i++) {
                if(myWorkBook.getSheetAt(i).getSheetName().toLowerCase().contains("rack")){
                    sheet = myWorkBook.getSheetAt(i);
                    break;
                }
            }
            
            if(sheet!=null){
              Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                  if(getCellValue(row.getCell(0)).length()>0){
                      activity = new Activity();
                      Sites site = null;
                      
                      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                      if((site = sitesController.getSites(getCellValue(row.getCell(0))))!=null){
                        activity.setSite(site); // Site
                        activity.setAsp(subContractorsController.getSubcontractors(getCellValue(row.getCell(1)))); // ASP
                        activity.setArea(areaController.getArea(getCellValue(row.getCell(2)))); // Area
                        if(vendorOwnerController.getByName(getCellValue(row.getCell(3)))!=null){
                        activity.setVendorOwner(vendorOwnerController.getByName(getCellValue(row.getCell(3)))); // Owner
                        }else{
                            continue;
                        }
                        activity.setClaimStatus(claimStatusController.getClaimStatus(getCellValue(row.getCell(4)))); // Claim
                        activity.setApprovalStatus(approvalStatusController.getApprovalStatus(getCellValue(row.getCell(5)))); // Approval
                        activity.setActivityType(domainNamesController.getDomainNames(getCellValue(row.getCell(6)))); // type
                        activity.setPhase(phasesController.getPhases(getCellValue(row.getCell(7)))); // phase
                        if(row.getCell(8).getCellType()==XSSFCell.CELL_TYPE_NUMERIC){
                        activity.setActivityDate(getDateCellValue(row.getCell(8))); // date
                        }else{
                            continue;
                        }
                        activity.setActivityCode(activityCodeController.getActivityCode(getCellValue(row.getCell(9)))); // Activity Code
                        
                        activity.setAcDescription(getCellValue(row.getCell(10))); // Claim
                        activity.setActivityDetails(getCellValue(row.getCell(11))); // Claim
                        activity.setQty(Double.parseDouble((getCellValue(row.getCell(12))!=null?getCellValue(row.getCell(12)):"0"))); // Qty
                        activity.setAcVendorPrice(Float.parseFloat((getCellValue(row.getCell(13))!=null?getCellValue(row.getCell(13)):"0"))); // vendor price
                        activity.setTotalPriceVendor(Float.parseFloat((getCellValue(row.getCell(14))!=null?getCellValue(row.getCell(14)):"0"))); // vendor price
                        activity.setTotalPriceVendorTaxes(Float.parseFloat((getCellValue(row.getCell(15))!=null?getCellValue(row.getCell(15)):"0"))); // vendor price
                        activity.setAcSubcontractorPrice(Float.parseFloat((getCellValue(row.getCell(16))!=null?getCellValue(row.getCell(16)):"0"))); // vendor price
                        activity.setTotalPriceAsp(Float.parseFloat((getCellValue(row.getCell(17))!=null?getCellValue(row.getCell(17)):"0"))); // vendor price
                        activity.setTotalUm(Float.parseFloat((getCellValue(row.getCell(18))!=null?getCellValue(row.getCell(18)):"0"))); // vendor price
                        activity.setTotalUmPercent(Float.parseFloat((getCellValue(row.getCell(19))!=null?getCellValue(row.getCell(19)):"0"))); // vendor price
                        activity.setActivityComment(getCellValue(row.getCell(20))); // vendor price
                        activity.setCorrelateTo(getCellValue(row.getCell(21))); // vendor price
                        activity.setSysDate(new Date());
                        activity.setCreator(usersController.getLoggedInUser());
                        activity.setTaxes(13.0);
                        activities.add(activity);
                      }
                  }
                }        
            }
            
            activityController.createMultiple(activities);
            
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ActivityLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ActivityLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        
        }
    }
    
    public String getCellValue(Cell cell){
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue();
    }
    public Date getDateCellValue(Cell cell){
        
        return cell.getDateCellValue();
    }
}
