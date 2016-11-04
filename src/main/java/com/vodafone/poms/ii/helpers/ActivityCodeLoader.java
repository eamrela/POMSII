/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.helpers;

import com.vodafone.poms.ii.entities.ActivityCode;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Amr
 */
public class ActivityCodeLoader {
    
        public static List<ActivityCode> readFile(InputStream fis){
        
        List<ActivityCode> activitiesCode = new ArrayList<>();
        ActivityCode activityCode = null;
        try {
        
        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
        int numberOfSheets= myWorkBook.getNumberOfSheets();
        XSSFSheet sheet = null;
        System.out.println(numberOfSheets);
            for (int i = 0; i < numberOfSheets; i++) {
                if(myWorkBook.getSheetAt(i).getSheetName().toLowerCase().contains("code")){
                    sheet = myWorkBook.getSheetAt(i);
                    break;
                }
            }
            if(sheet!=null){
              Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                  Row row = rowIterator.next();
                  if(getCellValue(row.getCell(0)).length()>0){
                      activityCode = new ActivityCode();
                      activityCode.setMaterialId(getCellValue(row.getCell(0)));
                      activityCode.setDescription(getCellValue(row.getCell(1)));
                      if(getCellValue(row.getCell(2)).matches("\\d+(?:\\.\\d+)?")){
                      activityCode.setQuantityRequested(new Integer(getCellValue(row.getCell(2))));
                      }
                      if(getCellValue(row.getCell(3)).matches("\\d+(?:\\.\\d+)?")){
                      activityCode.setVendorPrice(new Float(getCellValue(row.getCell(3))));
                      }else{continue;}
                      if(getCellValue(row.getCell(4)).matches("\\d+(?:\\.\\d+)?")){
                      activityCode.setSubcontractorPrice(new Float(getCellValue(row.getCell(4))));
                      }else{continue;}
                      activityCode.setUm(activityCode.getVendorPrice()-activityCode.getSubcontractorPrice());
                      activityCode.setUmPercent(activityCode.getUm()/activityCode.getVendorPrice());
                      activitiesCode.add(activityCode);
                  }
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ActivityCodeLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ActivityCodeLoader.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return activitiesCode;
        
    }
    
        public static String getCellValue(Cell cell){
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue();
    }

}
