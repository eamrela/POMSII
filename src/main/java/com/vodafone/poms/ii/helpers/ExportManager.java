/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.helpers;

import com.vodafone.poms.ii.controllers.ActivityController;
import com.vodafone.poms.ii.controllers.AspPoController;
import com.vodafone.poms.ii.controllers.VendorPoController;
import com.vodafone.poms.ii.controllers.util.JsfUtil;
import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.AspGrn;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.VendorMd;
import com.vodafone.poms.ii.entities.VendorPo;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Amr
 */
@Named("exportManager")
@SessionScoped
public class ExportManager implements Serializable {
    
    private Date fromDate;
    private Date toDate;
    private boolean activityCorrelated;
    //<editor-fold defaultstate="collapsed" desc="Headers">
    private final String[] activityHeaders = {"Site","ASP","Area","VF Owner","Claim Status","Approval Status ","Activity Type",
                                "Phase","Date","Activity Code","Activity Description ","Activity details","Qty","Unit Price to VF",
                                "Total Price without taxes (VF)",
                                "Total Price with taxes","ASP Unit Price","ASP Total Price","UM","UM%","Comment /Hint","ASP_PO"};
    private final String[] customerPOHeaders = {"po#","poDate","domain","type","description","factor","service_value","po_value",
                                            "po_value with taxes","work done","remaining in po","taxes","md_deserved","md_value",
                                            "md_date","md_number","invoiced","remaining in md"};
    private final String[] aspPOHeaders = {"po#","poDate","domain","type","description","factor","service_value","po_value",
                                            "po_value with taxes","work done","remaining in po","taxes","ASP","Vendor PO","grn_deserved","grn_value",
                                            "grn_date","grn_number","invoiced","remaining in grn"};
//</editor-fold>
    @Inject
    private ActivityController activityController;
    @Inject
    private VendorPoController vendorPOController;
    @Inject
    private AspPoController aspPOController;
    
    public void exportActivityForUser(){
        List<Activity> activities = activityController.getUserItems();
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Master Track");
        Row row = sheet.createRow(0);
        for (int i = 0; i < activityHeaders.length; i++) {
            row.createCell(i).setCellValue(activityHeaders[i]);
        }
        for (int i = 0; i < activities.size(); i++) {
            row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(activities.get(i).getSite().getSitePhysical());
            row.createCell(1).setCellValue(activities.get(i).getAsp().getSubcontractorName());
            row.createCell(2).setCellValue(activities.get(i).getArea().getAreaName());
            row.createCell(3).setCellValue(activities.get(i).getVendorOwner().getOwnerName());
            if(activities.get(i).getClaimStatus()!=null){
            row.createCell(4).setCellValue(activities.get(i).getClaimStatus().getClaimName());
            }else{
            row.createCell(4).setCellValue(""); 
            }
            if(activities.get(i).getApprovalStatus()!=null){
            row.createCell(5).setCellValue(activities.get(i).getApprovalStatus().getStatusName());
            }else{
            row.createCell(5).setCellValue(""); 
            }
            row.createCell(6).setCellValue(activities.get(i).getActivityType().getDomainName());
            if(activities.get(i).getPhase()!=null){
            row.createCell(7).setCellValue(activities.get(i).getPhase().getPhaseName());
            }else{
            row.createCell(7).setCellValue(""); 
            }
            row.createCell(8).setCellValue(activities.get(i).getActivityDate());
            row.createCell(9).setCellValue(activities.get(i).getAcMaterialId());
            row.createCell(10).setCellValue(activities.get(i).getAcDescription());
            row.createCell(11).setCellValue(activities.get(i).getActivityDetails());
            row.createCell(12).setCellValue(activities.get(i).getQty());
            row.createCell(13).setCellValue(activities.get(i).getAcVendorPrice());
            row.createCell(14).setCellValue(activities.get(i).getTotalPriceVendor());
            row.createCell(15).setCellValue(activities.get(i).getTotalPriceVendorTaxes());
            row.createCell(16).setCellValue(activities.get(i).getAcSubcontractorPrice());
            row.createCell(17).setCellValue(activities.get(i).getTotalPriceAsp());
            row.createCell(18).setCellValue(activities.get(i).getTotalUm());
            row.createCell(19).setCellValue(activities.get(i).getTotalUmPercent());
            row.createCell(20).setCellValue(activities.get(i).getActivityComment());
            row.createCell(21).setCellValue((activities.get(i).getAspPoCollection().isEmpty()?
                                    "Uncorrelated":
                                    ((AspPo)activities.get(i).getAspPoCollection().toArray()[0]).getPoNumber()));
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"G-Cairo Region Extra Work.xlsx\"");

        try {
            workbook.write(externalContext.getResponseOutputStream());
            externalContext.getResponseOutputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        facesContext.responseComplete();
        JsfUtil.addSuccessMessage("Activity Report is now exported");
        
    }
    
    public void exportActivity(){
        List<Activity> activities = activityController.getExportItems(fromDate,toDate);
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Master Track");
        Row row = sheet.createRow(0);
        for (int i = 0; i < activityHeaders.length; i++) {
            row.createCell(i).setCellValue(activityHeaders[i]);
        }
        for (int i = 0; i < activities.size(); i++) {
            row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(activities.get(i).getSite().getSitePhysical());
            row.createCell(1).setCellValue(activities.get(i).getAsp().getSubcontractorName());
            row.createCell(2).setCellValue(activities.get(i).getArea().getAreaName());
            row.createCell(3).setCellValue(activities.get(i).getVendorOwner().getOwnerName());
            if(activities.get(i).getClaimStatus()!=null){
            row.createCell(4).setCellValue(activities.get(i).getClaimStatus().getClaimName());
            }else{
            row.createCell(4).setCellValue(""); 
            }
            if(activities.get(i).getApprovalStatus()!=null){
            row.createCell(5).setCellValue(activities.get(i).getApprovalStatus().getStatusName());
            }else{
            row.createCell(5).setCellValue(""); 
            }
            row.createCell(6).setCellValue(activities.get(i).getActivityType().getDomainName());
            if(activities.get(i).getPhase()!=null){
            row.createCell(7).setCellValue(activities.get(i).getPhase().getPhaseName());
            }else{
            row.createCell(7).setCellValue(""); 
            }
            row.createCell(8).setCellValue(activities.get(i).getActivityDate());
            row.createCell(9).setCellValue(activities.get(i).getAcMaterialId());
            row.createCell(10).setCellValue(activities.get(i).getAcDescription());
            row.createCell(11).setCellValue(activities.get(i).getActivityDetails());
            row.createCell(12).setCellValue(activities.get(i).getQty());
            row.createCell(13).setCellValue(activities.get(i).getAcVendorPrice());
            row.createCell(14).setCellValue(activities.get(i).getTotalPriceVendor());
            row.createCell(15).setCellValue(activities.get(i).getTotalPriceVendorTaxes());
            row.createCell(16).setCellValue(activities.get(i).getAcSubcontractorPrice());
            row.createCell(17).setCellValue(activities.get(i).getTotalPriceAsp());
            row.createCell(18).setCellValue(activities.get(i).getTotalUm());
            row.createCell(19).setCellValue(activities.get(i).getTotalUmPercent());
            row.createCell(20).setCellValue(activities.get(i).getActivityComment());
            row.createCell(21).setCellValue((activities.get(i).getAspPoCollection().isEmpty()?
                                    "Uncorrelated":
                                    ((AspPo)activities.get(i).getAspPoCollection().toArray()[0]).getPoNumber()));
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"G-Cairo Region Extra Work.xlsx\"");

        try {
            workbook.write(externalContext.getResponseOutputStream());
            externalContext.getResponseOutputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        facesContext.responseComplete();
        JsfUtil.addSuccessMessage("Activity Report is now exported");
        
    }
    
    public void exportCustomerPO(){
       List<VendorPo> vendorPOs = vendorPOController.getExportItems(fromDate,toDate);
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("All POs");
        Row row = sheet.createRow(0);
        for (int i = 0; i < customerPOHeaders.length; i++) {
            row.createCell(i).setCellValue(customerPOHeaders[i]);
        }
        int innerRow = 0;
        for (int i = 0; i < vendorPOs.size(); i++) {
            row = sheet.createRow(i+1+innerRow);
            //po#
            row.createCell(0).setCellValue(vendorPOs.get(i).getPoNumber());
            //poDate
            row.createCell(1).setCellValue(vendorPOs.get(i).getPoDate());
            //domain
            row.createCell(2).setCellValue(vendorPOs.get(i).getDomainName().getDomainName());
            //type
            row.createCell(3).setCellValue(vendorPOs.get(i).getPoType().getTypeName());
            //description
            row.createCell(4).setCellValue(vendorPOs.get(i).getPoDescription());
            //factor
            row.createCell(5).setCellValue(vendorPOs.get(i).getFactor());
            //service_value
            row.createCell(6).setCellValue(vendorPOs.get(i).getServiceValue().toString());
            //po_value
            row.createCell(7).setCellValue(vendorPOs.get(i).getPoValue().toString());
            //po_value with taxes
            row.createCell(8).setCellValue(vendorPOs.get(i).getPoValueTaxes().toString());
            //work done
            row.createCell(9).setCellValue(vendorPOs.get(i).getWorkDone());
            //remaining in po
            row.createCell(10).setCellValue(vendorPOs.get(i).getRemainingInPo().toString());
            //taxes
            row.createCell(11).setCellValue(vendorPOs.get(i).getTaxes());
            Object[] mds = vendorPOs.get(i).getVendorMdCollection().toArray();
            for (int j = 0; j < mds.length; j++) {
                if(j>1){
                row = sheet.createRow(i+1+innerRow);
                innerRow++;
                }
            //md_deserved
                row.createCell(12).setCellValue(((VendorMd) mds[j]).getMdDeserved().toString());
            //md_value
                row.createCell(13).setCellValue(((VendorMd) mds[j]).getMdValue()!=null?
                                                    ((VendorMd) mds[j]).getMdValue().toString():"");
            //md_date
                row.createCell(14).setCellValue(((VendorMd) mds[j]).getMdDate()!=null?
                                                    ((VendorMd) mds[j]).getMdDate():null);
            //md_number
                row.createCell(15).setCellValue(((VendorMd) mds[j]).getMdNumber()!=null?
                                                    ((VendorMd) mds[j]).getMdNumber():"");
            //invoiced
                row.createCell(16).setCellValue(((VendorMd) mds[j]).getInvoiced()!=null?
                                                    ((VendorMd) mds[j]).getInvoiced():false);
            //remaining in md
                row.createCell(17).setCellValue(((VendorMd) mds[j]).getRemainingInMd()!=null?
                                                    ((VendorMd) mds[j]).getRemainingInMd().toString():"");
                
                
            }
            
            
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"Customer POs.xlsx\"");

        try {
            workbook.write(externalContext.getResponseOutputStream());
            externalContext.getResponseOutputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        facesContext.responseComplete();
        JsfUtil.addSuccessMessage("Customer PO Report is now exported");
         
    }
    
    public void exportASPPO(){
       List<AspPo> aspPOs = aspPOController.getExportItems(fromDate,toDate);
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("All POs");
        Row row = sheet.createRow(0);
        for (int i = 0; i < aspPOHeaders.length; i++) {
            row.createCell(i).setCellValue(aspPOHeaders[i]);
        }
        int innerRow = 0;
        for (int i = 0; i < aspPOs.size(); i++) {
            row = sheet.createRow(i+1+innerRow);
            //po#
            row.createCell(0).setCellValue(aspPOs.get(i).getPoNumber());
            //poDate
            row.createCell(1).setCellValue(aspPOs.get(i).getPoDate());
            //domain
            row.createCell(2).setCellValue(aspPOs.get(i).getDomainName().getDomainName());
            //type
            row.createCell(3).setCellValue(aspPOs.get(i).getPoType().getTypeName());
            //description
            row.createCell(4).setCellValue(aspPOs.get(i).getPoDescription());
            //factor
            row.createCell(5).setCellValue(aspPOs.get(i).getFactor());
            //service_value
            row.createCell(6).setCellValue(aspPOs.get(i).getServiceValue().toString());
            //po_value
            row.createCell(7).setCellValue(aspPOs.get(i).getPoValue().toString());
            //po_value with taxes
            row.createCell(8).setCellValue(aspPOs.get(i).getPoValueTaxes().toString());
            //work done
            row.createCell(9).setCellValue(aspPOs.get(i).getWorkDone());
            //remaining in po
            row.createCell(10).setCellValue(aspPOs.get(i).getRemainingInPo().toString());
            //taxes
            row.createCell(11).setCellValue(aspPOs.get(i).getTaxes());
            //ASP
            row.createCell(12).setCellValue(aspPOs.get(i).getAsp().getSubcontractorName());
            //VendorPO
             row.createCell(13).setCellValue((aspPOs.get(i).getVendorPoCollection().isEmpty()?
                                    "Uncorrelated":
                                    ((VendorPo)aspPOs.get(i).getVendorPoCollection().toArray()[0]).getPoNumber()));
            Object[] grns = aspPOs.get(i).getAspGrnCollection().toArray();
            for (int j = 0; j < grns.length; j++) {
                if(j>1){
                row = sheet.createRow(i+1+innerRow);
                innerRow++;
                }
            //md_deserved
                row.createCell(14).setCellValue(((AspGrn) grns[j]).getGrnDeserved().toString());
            //md_value
                row.createCell(15).setCellValue(((AspGrn) grns[j]).getGrnValue()!=null?
                                                    ((AspGrn) grns[j]).getGrnValue().toString():"");
            //md_date
                row.createCell(16).setCellValue(((AspGrn) grns[j]).getGrnDate()!=null?
                                                    ((AspGrn) grns[j]).getGrnDate():null);
            //md_number
                row.createCell(17).setCellValue(((AspGrn) grns[j]).getGrnNumber()!=null?
                                                    ((AspGrn) grns[j]).getGrnNumber():"");
            //invoiced
                row.createCell(18).setCellValue(((AspGrn) grns[j]).getInvoiced()!=null?
                                                    ((AspGrn) grns[j]).getInvoiced():false);
            //remaining in md
                row.createCell(19).setCellValue(((AspGrn) grns[j]).getRemainingInGrn()!=null?
                                                    ((AspGrn) grns[j]).getRemainingInGrn().toString():"");
                
                
            }
            
            
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"ASP POs.xlsx\"");

        try {
            workbook.write(externalContext.getResponseOutputStream());
            externalContext.getResponseOutputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        facesContext.responseComplete();
        JsfUtil.addSuccessMessage("Customer PO Report is now exported");
         
    }
    
   


//<editor-fold defaultstate="collapsed" desc="Setters/Getters">

    
    public boolean isActivityCorrelated() {
        return activityCorrelated;
    }

    public void setActivityCorrelated(boolean activityCorrelated) {
        this.activityCorrelated = activityCorrelated;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
//</editor-fold>
}
