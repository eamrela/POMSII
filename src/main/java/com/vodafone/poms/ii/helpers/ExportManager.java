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
import com.vodafone.poms.ii.entities.ActivityAttachments;
import com.vodafone.poms.ii.entities.AspGrn;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.VendorMd;
import com.vodafone.poms.ii.entities.VendorPo;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import javax.xml.namespace.QName;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hpsf.ClassID;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.poifs.filesystem.Ole10Native;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtension;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;


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
                                "Total Price with taxes","ASP Unit Price","ASP Total Price","UM","UM%","Comment /Hint","ASP_PO","Attachment(s)"};
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
    
    static final String drawNS = "http://schemas.microsoft.com/office/drawing/2010/main";
    static final String relationshipsNS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships";
    
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
        try {
            List<Activity> activities = activityController.getExportItems(fromDate,toDate);
            
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Master Track");
            
            int imgPckId = addImageToWorkbook(workbook, "/home/poms/uploaded_data/pkg.png", Workbook.PICTURE_TYPE_PNG);
            String imgPckRelId = addImageToSheet(sheet, imgPckId, Workbook.PICTURE_TYPE_PNG);
            
            
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
                if(activities.get(i).getActivityAttachmentsCollection()!=null){
                    if(activities.get(i).getActivityAttachmentsCollection().size()>0){
                    Object[] attachments = activities.get(i).getActivityAttachmentsCollection().toArray();
                    for (int j = 0; j < attachments.length; j++) {
                    XSSFClientAnchor imgAnchor1 = new XSSFClientAnchor(0,0,0,0,(23+j),
                                                    row.getRowNum(), (23 +j+1), row.getRowNum() + 1);
                    String oleRelId1 = addFile(sheet,((ActivityAttachments)attachments[j]).getAttachmentLocation(),(i+j+activities.get(i).getActivityId().intValue()+Math.random()) );
                    int shapeId1 = addImageToShape(sheet, imgAnchor1, imgPckId);
                    addObjectToShape(sheet, imgAnchor1, shapeId1, oleRelId1, imgPckRelId, "Objekt-Manager-Shellobjekt");
                    }
                    }
                }
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
            
        } catch (InvalidFormatException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
    
   
    private static String addFile(XSSFSheet sh, String filePath, double oleId) throws IOException, InvalidFormatException {
        File file = new File(filePath);
        FileInputStream fin = new FileInputStream(file);
        byte[] data;
        data = new byte[fin.available()];
        fin.read(data);
        Ole10Native ole10 = new Ole10Native(file.getAbsolutePath() ,file.getAbsolutePath(), file.getAbsolutePath(),data);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(500);
        ole10.writeOut(bos);

        POIFSFileSystem poifs = new POIFSFileSystem();
        poifs.getRoot().createDocument(Ole10Native.OLE10_NATIVE, new ByteArrayInputStream(bos.toByteArray()));

        poifs.getRoot().setStorageClsid(ClassID.OLE10_PACKAGE);


        final PackagePartName pnOLE = PackagingURIHelper.createPartName( "/xl/embeddings/oleObject"+oleId+Math.random()+".bin" );
        final PackagePart partOLE = sh.getWorkbook().getPackage().createPart( pnOLE, "application/vnd.openxmlformats-officedocument.oleObject" );
        PackageRelationship prOLE = sh.getPackagePart().addRelationship( pnOLE, TargetMode.INTERNAL, POIXMLDocument.OLE_OBJECT_REL_TYPE );
        OutputStream os = partOLE.getOutputStream();
        poifs.writeFilesystem(os);
        os.close();
        poifs.close();

        return prOLE.getId();

    }


    private static int addImageToWorkbook(XSSFWorkbook wb, String fileName, int pictureType) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        int imgId = wb.addPicture(fis, pictureType);
        fis.close();
        return imgId;
    }

    private static String addImageToSheet(XSSFSheet sh, int imgId, int pictureType) throws InvalidFormatException {
        final PackagePartName pnIMG  = PackagingURIHelper.createPartName( "/xl/media/image"+(imgId+1)+(pictureType == Workbook.PICTURE_TYPE_PNG ? ".png" : ".jpeg") );
        PackageRelationship prIMG = sh.getPackagePart().addRelationship( pnIMG, TargetMode.INTERNAL, PackageRelationshipTypes.IMAGE_PART );
        return prIMG.getId();
    }


    private static int addImageToShape(XSSFSheet sh, XSSFClientAnchor imgAnchor, int imgId) {
        XSSFDrawing pat = sh.createDrawingPatriarch();
        XSSFPicture pic = pat.createPicture(imgAnchor, imgId);

        CTPicture cPic = pic.getCTPicture();
        int shapeId = (int)cPic.getNvPicPr().getCNvPr().getId();
        cPic.getNvPicPr().getCNvPr().setHidden(true);
        CTOfficeArtExtensionList extLst = cPic.getNvPicPr().getCNvPicPr().addNewExtLst();
        // https://msdn.microsoft.com/en-us/library/dd911027(v=office.12).aspx
        CTOfficeArtExtension ext = extLst.addNewExt();
        ext.setUri("{63B3BB69-23CF-44E3-9099-C40C66FF867C}");
        XmlCursor cur = ext.newCursor();
        cur.toEndToken();
        cur.beginElement(new QName(drawNS, "compatExt", "a14"));
        cur.insertAttributeWithValue("spid", "_x0000_s"+shapeId);


        return shapeId;
    }



    private static void addObjectToShape(XSSFSheet sh, XSSFClientAnchor imgAnchor, int shapeId, String objRelId, String imgRelId, String progId) {
        CTWorksheet cwb = sh.getCTWorksheet();
        CTOleObjects oo = cwb.isSetOleObjects() ? cwb.getOleObjects() : cwb.addNewOleObjects();

        CTOleObject ole1 = oo.addNewOleObject();
        ole1.setProgId(progId);
        ole1.setShapeId(shapeId);
        ole1.setId(objRelId);


        XmlCursor cur1 = ole1.newCursor();
        cur1.toEndToken();
        cur1.beginElement("objectPr", XSSFRelation.NS_SPREADSHEETML);
        cur1.insertAttributeWithValue("id", relationshipsNS, imgRelId);
        cur1.insertAttributeWithValue("defaultSize", "0");
        cur1.beginElement("anchor", XSSFRelation.NS_SPREADSHEETML);
        cur1.insertAttributeWithValue("moveWithCells", "1");

        CTTwoCellAnchor anchor = CTTwoCellAnchor.Factory.newInstance();
        anchor.setFrom(imgAnchor.getFrom());
        anchor.setTo(imgAnchor.getTo());

        XmlCursor cur2 = anchor.newCursor();
        cur2.copyXmlContents(cur1);
        cur2.dispose();

        cur1.toParent();
        cur1.toFirstChild();
        cur1.setName(new QName(XSSFRelation.NS_SPREADSHEETML, "from"));
        cur1.toNextSibling();
        cur1.setName(new QName(XSSFRelation.NS_SPREADSHEETML, "to"));

        cur1.dispose();
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
