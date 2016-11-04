/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.helpers;


import com.vodafone.poms.ii.controllers.ActivityController;
import com.vodafone.poms.ii.entities.Activity;
import java.io.IOException;
import java.io.Serializable;
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
@Named("activityExporter")
@SessionScoped
public class ActivityExporter implements Serializable{
    
    @Inject
    private ActivityController activityController;
    
    //<editor-fold defaultstate="collapsed" desc="Headers">
    private String[] headers = {"Site","ASP","Area","VF Owner","Claim Status","Approval Status ","Activity Type",
                                "Phase","Date","Activity Code","Activity Description ","Activity details","Qty","Unit Price to VF",
                                "Total Price without taxes (VF)",
                                "Total Price with taxes","ASP Unit Price","ASP Total Price","UM","UM%","Comment /Hint"};
//</editor-fold>
    
    public void exportReport(){
        List<Activity> activities = activityController.getItems();
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Master Track");
        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            row.createCell(i).setCellValue(headers[i]);
        }
        for (int i = 0; i < activities.size(); i++) {
            row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(activities.get(i).getSite().getSitePhysical());
            row.createCell(1).setCellValue(activities.get(i).getAsp().getSubcontractorName());
            row.createCell(2).setCellValue(activities.get(i).getArea().getAreaName());
            row.createCell(3).setCellValue(activities.get(i).getVendorOwner().getOwnerName());
            row.createCell(4).setCellValue(activities.get(i).getClaimStatus().getClaimName());
            row.createCell(5).setCellValue(activities.get(i).getApprovalStatus().getStatusName());
            row.createCell(6).setCellValue(activities.get(i).getActivityType().getDomainName());
            row.createCell(7).setCellValue(activities.get(i).getPhase().getPhaseName());
            row.createCell(8).setCellValue(activities.get(i).getActivityDate());
            row.createCell(9).setCellValue(activities.get(i).getActivityCode().getMaterialId());
            row.createCell(10).setCellValue(activities.get(i).getActivityCode().getDescription());
            row.createCell(11).setCellValue(activities.get(i).getActivityDetails());
            row.createCell(12).setCellValue(activities.get(i).getQty());
            row.createCell(13).setCellValue(activities.get(i).getActivityCode().getVendorPrice());
            row.createCell(14).setCellValue(activities.get(i).getTotalPriceVendor());
            row.createCell(15).setCellValue(activities.get(i).getTotalPriceVendorTaxes());
            row.createCell(16).setCellValue(activities.get(i).getActivityCode().getSubcontractorPrice());
            row.createCell(17).setCellValue(activities.get(i).getTotalPriceAsp());
            row.createCell(18).setCellValue(activities.get(i).getTotalUm());
            row.createCell(19).setCellValue(activities.get(i).getTotalUmPercent());
            row.createCell(20).setCellValue(activities.get(i).getActivityComment());
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"G-Cairo Region Extra Work.xlsx\"");

        try {
            workbook.write(externalContext.getResponseOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ActivityExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        facesContext.responseComplete();
        
    }
}
