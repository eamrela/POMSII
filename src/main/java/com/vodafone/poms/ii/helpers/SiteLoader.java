/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.helpers;


import com.vodafone.poms.ii.entities.Sites;
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
public class SiteLoader {
    
     public  static List<Sites> readFile(InputStream fis){
        List<Sites> sites = new ArrayList<>();
        Sites site = null;
        try {

        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
        int numberOfSheets= myWorkBook.getNumberOfSheets();
        XSSFSheet sheet = null;
            System.out.println(numberOfSheets);
            for (int i = 0; i < numberOfSheets; i++) {
                sheet = myWorkBook.getSheetAt(i);
                if(sheet.getRow(0)!=null && sheet.getRow(0).getCell(0)!=null){
                    if(sheet.getRow(0).getCell(0).getStringCellValue().toLowerCase().contains("site")){
                        //<editor-fold defaultstate="collapsed" desc="Process Sheet">
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                  Row row = rowIterator.next();
                  if(getCellValue(row.getCell(0)).length()>0){
                    site = new Sites();
                    site.setSitePhysical(getCellValue(row.getCell(0)));
                    site.setGfRt(getCellValue(row.getCell(1)));
                    sites.add(site);
                  }
                      }
//</editor-fold>
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SiteLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SiteLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sites;
    }
    
    public static String getCellValue(Cell cell){
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue();
    }
}
