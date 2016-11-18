/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.helpers;

import com.vodafone.poms.ii.controllers.AspGrnController;
import com.vodafone.poms.ii.controllers.AspPoController;
import com.vodafone.poms.ii.controllers.VendorMdController;
import com.vodafone.poms.ii.controllers.VendorPoController;
import com.vodafone.poms.ii.entities.AspGrn;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.VendorMd;
import com.vodafone.poms.ii.entities.VendorPo;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.joda.time.DateTime;

/**
 *
 * @author Amr
 */
@Named("dashboardManager")
@SessionScoped
public class DashboardController implements Serializable{
    
    private Date start;
    private Date end;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private List<CustomerSummary> customerSummary;
    private List<AspGrn> aspGrns;
    private List<AspGrn> aspGrnsCOS;
    private List<AspPo> aspCommittedCost;
    private List<VendorMd> vendorMds;
    private List<VendorMd> vendorMdsNS;
    private List<VendorMd> vendorRemainingNotYetinvoiced;
    private List<VendorPo> vendorMdNotYetGenrated;
    private BigInteger NS;
    private BigInteger COS;
    private BigInteger UM;
    private Double UMPercent;
    private BigInteger committedCost;
    private BigInteger remainingNotYetInvoiced;
    private BigInteger vendorMdNotYetGenratedValue;
    
    private List<ASPAnalysis> aspAnalysis;
    private List<DomainAnalysis> domainAnalysis;
    private String aspAnalysisChartData;
    private String domainAnalysisChartData;
    
    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @Inject
    private VendorMdController vendorMDController;
    @Inject
    private VendorPoController vendorPoController;
    @Inject
    private AspGrnController aspGrnController;
    @Inject
    private AspPoController aspPoController;
    
     //<editor-fold defaultstate="collapsed" desc="Colors">
    private  String[] mColors = {
            "#39add1", // light blue
            "#3079ab", // dark blue
            "#c25975", // mauve
            "#e15258", // red
            "#f9845b", // orange
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#637a91", // dark gray
            "#f092b0", // pink
            "#b7c0c7"  // light gray
    };
//</editor-fold>
    
    public DashboardController(){
        end = DateTime.now().toDate();
        start = DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay().toDate();
    }
    
    @PostConstruct
    public void refresh(){
        boolean asp = calculateASP();
        boolean vendor = calculateVendor();
        if(asp && vendor){
            UM = NS.subtract(COS);
            if(NS.compareTo(BigInteger.ZERO)==1){
            UMPercent = round(Double.valueOf((UM.floatValue()/NS.floatValue())*100),1);
            }else{
            UMPercent = 0.0;
            }
        }
        getAspAnalysis();
        getDomainAnalysis();
        getVendorRemainingNotYetinvoiced();
        getAspCommittedCost();
        calculateCustomerSummary();
        
    }

    private static double round (double value, int precision) {
    int scale = (int) Math.pow(10, precision);
    return (double) Math.round(value * scale) / scale;
}
    
    public boolean calculateASP() {
            aspGrns = aspGrnController.getDashboardItems(start,end);
            aspCommittedCost = aspPoController.getDashboardCommittedCost(start,end);
            COS = BigInteger.ZERO;
            committedCost = BigInteger.ZERO;
            aspGrnsCOS = new ArrayList<>();
            for (AspPo aspCommittedCost1 : aspCommittedCost) {
                committedCost = committedCost.add(aspCommittedCost1.getGrnDeserved());
            }
            for (AspGrn aspGrn : aspGrns) {
                if(aspGrn.getGrnValue()!=null){
                COS = COS.add(aspGrn.getGrnValue());
                aspGrnsCOS.add(aspGrn);
                }else if(aspGrn.getRemainingInGrn()!=null){ 
                    if(aspGrn.getRemainingInGrn().compareTo(BigInteger.ZERO)==1){
                            aspCommittedCost.add(aspGrn.getAspPoId());
                            committedCost = committedCost.add(aspGrn.getRemainingInGrn());
                    }
                }
            }
            
            return true;
    }

    public boolean calculateVendor(){
        vendorMds = vendorMDController.getDashboardItems(start, end);
        vendorRemainingNotYetinvoiced = new ArrayList<>();
        vendorMdNotYetGenrated = vendorPoController.getDashboardMDNotYetGenerated(start,end);
        vendorMdNotYetGenratedValue = BigInteger.ZERO;
        for (VendorPo vendorMdNotYetGenrated1 : vendorMdNotYetGenrated) {
            vendorMdNotYetGenratedValue = vendorMdNotYetGenratedValue.add(vendorMdNotYetGenrated1.getMdDeserved());
        }
        NS = BigInteger.ZERO;
        vendorMdsNS = new ArrayList<>();
        remainingNotYetInvoiced = BigInteger.ZERO;
        
        for (VendorMd vendorMd : vendorMds) {
            if(vendorMd.getInvoiced()!=null){
                if(vendorMd.getInvoiced() && vendorMd.getMdValue()!=null){
                    NS = NS.add(vendorMd.getMdValue());
                    vendorMdsNS.add(vendorMd);
                    if(vendorMd.getRemainingInMd()!=null){
                        if(vendorMd.getRemainingInMd().compareTo(BigInteger.ZERO)==1){
                            remainingNotYetInvoiced = remainingNotYetInvoiced.add(vendorMd.getRemainingInMd());
                            vendorRemainingNotYetinvoiced.add(vendorMd);
                        }
                    }
                }else{
                    vendorRemainingNotYetinvoiced.add(vendorMd);
                }
            }
        }
        return true;
    }

    public List<ASPAnalysis> getAspAnalysis() {
        if(aspAnalysis == null){
            aspAnalysis = em.createNativeQuery(" select asp_name, "+
"         case when amount is null then 0 else amount end amount "+
" from ( "+
"  select asp_name,  "+
"         sum(val) amount  "+
"  from (  "+
"  select subcontractor_name asp_name,  "+
"       (select sum(total_price_asp)  "+
"        from activity  "+
"        where asp = asp.subcontractor_name  "+
"        and activity_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"'  "+
"        and activity_id not in "+
"                 (select activity_id from asp_po_j_activity)) val  "+
" from subcontractors asp "+
" union  "+
" select subcontractor_name asp_name,  "+
"       (select sum(po_value)  "+
"        from asp_po  "+
"        where asp = asp.subcontractor_name  "+
"        and po_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"'  "+
"        and po_number not in "+
"                 (select asp_po_id from asp_po_j_activity)) val  "+
" from subcontractors asp "+
" ) a "+
" group by asp_name) b "
,ASPAnalysis.class).getResultList();
        }
        return aspAnalysis;
    }

    public List<DomainAnalysis> getDomainAnalysis() {
        if(domainAnalysis == null){
            domainAnalysis = em.createNativeQuery(" select domain_name, "+
"   case when amount is null then 0 else amount end amount "+
" from ( "+
"  select domain_name,  "+
"   sum(val) amount  "+
"  from (  "+
"  select domain_name, "+
"       (select sum(total_price_asp)  "+
"        from activity  "+
"        where activity_type = domains.domain_name  "+
"        and activity_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"'  "+
"        and activity_id not in "+
"     (select activity_id from asp_po_j_activity)) val  "+
" from domain_names domains "+
" union  "+
" select domain_name, "+
"       (select sum(po_value)  "+
"        from asp_po  "+
"        where domain_name = domains.domain_name  "+
"        and po_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"'  "+
"        and po_number not in "+
"     (select asp_po_id from asp_po_j_activity)) val  "+
" from domain_names domains "+
" ) a "+
" group by domain_name) b "
,DomainAnalysis.class).getResultList();
        }
        return domainAnalysis;
    }

    
    public void clearSelected(){
        COS =  null;
        NS = null;
        UM = null;
        UMPercent = null;
        aspAnalysis = null;
        domainAnalysis = null;
        aspAnalysisChartData = null;
        domainAnalysisChartData = null;
        aspCommittedCost = null;
        aspGrns = null;
        aspGrnsCOS = null;
        committedCost = null;
        remainingNotYetInvoiced = null;
        vendorMds = null;
        vendorMdsNS = null;
        vendorRemainingNotYetinvoiced = null;
        vendorMdNotYetGenrated = null;
        vendorMdNotYetGenratedValue = BigInteger.ZERO;
        customerSummary = null;
        refresh();
    }
    

    
    
    
    //<editor-fold defaultstate="collapsed" desc="Setters/Getters">
    public void setRemainingNotYetInvoiced(BigInteger remainingNotYetInvoiced) {
        this.remainingNotYetInvoiced = remainingNotYetInvoiced;
    }

    public BigInteger getVendorMdNotYetGenratedValue() {
        return vendorMdNotYetGenratedValue;
    }

    public void setVendorMdNotYetGenratedValue(BigInteger vendorMdNotYetGenratedValue) {
        this.vendorMdNotYetGenratedValue = vendorMdNotYetGenratedValue;
    }
    
    public void setStart(Date start) {
        this.start = start;
    }

    public List<VendorPo> getVendorMdNotYetGenrated() {
        return vendorMdNotYetGenrated;
    }

    public void setVendorMdNotYetGenrated(List<VendorPo> vendorMdNotYetGenrated) {
        this.vendorMdNotYetGenrated = vendorMdNotYetGenrated;
    }

   

    
    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
        
    public List<AspGrn> getAspGrnsCOS() {
        return aspGrnsCOS;
    }

    public void setAspGrnsCOS(List<AspGrn> aspGrnsCOS) {
        this.aspGrnsCOS = aspGrnsCOS;
    }

    public List<AspPo> getAspCommittedCost() {
        return aspCommittedCost;
    }

    public void setAspCommittedCost(List<AspPo> aspCommittedCost) {
        this.aspCommittedCost = aspCommittedCost;
    }

    public List<VendorMd> getVendorMds() {
        return vendorMds;
    }

    public void setVendorMds(List<VendorMd> vendorMds) {
        this.vendorMds = vendorMds;
    }

    public List<VendorMd> getVendorMdsNS() {
        return vendorMdsNS;
    }

    public void setVendorMdsNS(List<VendorMd> vendorMdsNS) {
        this.vendorMdsNS = vendorMdsNS;
    }

    public List<VendorMd> getVendorRemainingNotYetinvoiced() {
        return vendorRemainingNotYetinvoiced;
    }

    public void setVendorRemainingNotYetinvoiced(List<VendorMd> vendorRemainingNotYetinvoiced) {
        this.vendorRemainingNotYetinvoiced = vendorRemainingNotYetinvoiced;
    }

    public BigInteger getNS() {
        return NS;
    }

    public void setNS(BigInteger NS) {
        this.NS = NS;
    }

    public BigInteger getCOS() {
        return COS;
    }

    public void setCOS(BigInteger COS) {
        this.COS = COS;
    }

    public BigInteger getUM() {
        return UM;
    }

    public void setUM(BigInteger UM) {
        this.UM = UM;
    }

    public Double getUMPercent() {
        return UMPercent;
    }

    public void setUMPercent(Double UMPercent) {
        this.UMPercent = UMPercent;
    }

    public BigInteger getCommittedCost() {
        return committedCost;
    }

    public void setCommittedCost(BigInteger committedCost) {
        this.committedCost = committedCost;
    }

    public BigInteger getRemainingNotYetInvoiced() {
        return remainingNotYetInvoiced;
    }

//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Charts Data">

    public String getDomainAnalysisChartData() {
          //<editor-fold defaultstate="collapsed" desc="Start of string">
        
        domainAnalysisChartData = "{" +
                        "    \"theme\": \"light\", " +
                        "    \"type\": \"serial\", " +
                        "	\"startDuration\": 1, " +
                        "    \"dataProvider\": [";
//</editor-fold>

        for (int i = 0; i < domainAnalysis.size(); i++) {
            
            domainAnalysisChartData+= 
                        "{ " +
                        "        \"DomainName\": \""+domainAnalysis.get(i).getName()+"\", " +
                        "        \"Amount\": "+domainAnalysis.get(i).getAmount()+", " +
                        "        \"color\": \""+getColor()+"\" " +
                        "}"+(i==domainAnalysis.size()-1?"":",");
        }
 
            //<editor-fold defaultstate="collapsed" desc="End of String">

            domainAnalysisChartData+= " ], " +
                        "    \"valueAxes\": [{ " +
                        "        \"position\": \"left\", " +
                        "        \"title\": \"Money\" " +
                        "    }], " +
                        "    \"graphs\": [{ " +
                        "        \"balloonText\": \"[[category]]: <b>[[value]]</b>\", " +
                        "        \"fillColorsField\": \"color\", " +
                        "        \"fillAlphas\": 1, " +
                        "        \"lineAlpha\": 0.1, " +
                        "        \"type\": \"column\", " +
                        "        \"valueField\": \"Amount\" " +
                        "    }], " +
                        "    \"depth3D\": 20, " +
                        "	\"angle\": 30, " +
                        "    \"chartCursor\": { " +
                        "        \"categoryBalloonEnabled\": false, " +
                        "        \"cursorAlpha\": 0, " +
                        "        \"zoomable\": false " +
                        "    }, " +
                        "    \"categoryField\": \"DomainName\", " +
                        "    \"categoryAxis\": { " +
                        "        \"gridPosition\": \"start\", " +
                        "        \"labelRotation\": 90 " +
                        "    }, " +
                        "    \"export\": { " +
                        "    	\"enabled\": true " +
                        "     } " +
                        " " +
                        "}";
            //</editor-fold>
        return domainAnalysisChartData;
    }
    
    
    public String getAspAnalysisChartData() {
         //<editor-fold defaultstate="collapsed" desc="Start of string">
        
        aspAnalysisChartData = "{" +
                        "    \"theme\": \"light\", " +
                        "    \"type\": \"serial\", " +
                        "	\"startDuration\": 1, " +
                        "    \"dataProvider\": [";
//</editor-fold>

        for (int i = 0; i < aspAnalysis.size(); i++) {
            
            aspAnalysisChartData+= 
                        "{ " +
                        "        \"ASPName\": \""+aspAnalysis.get(i).getName()+"\", " +
                        "        \"Amount\": "+aspAnalysis.get(i).getAmount()+", " +
                        "        \"color\": \""+getColor()+"\" " +
                        "}"+(i==aspAnalysis.size()-1?"":",");
        }
 
            //<editor-fold defaultstate="collapsed" desc="End of String">

            aspAnalysisChartData+= " ], " +
                        "    \"valueAxes\": [{ " +
                        "        \"position\": \"left\", " +
                        "        \"title\": \"Money\" " +
                        "    }], " +
                        "    \"graphs\": [{ " +
                        "        \"balloonText\": \"[[category]]: <b>[[value]]</b>\", " +
                        "        \"fillColorsField\": \"color\", " +
                        "        \"fillAlphas\": 1, " +
                        "        \"lineAlpha\": 0.1, " +
                        "        \"type\": \"column\", " +
                        "        \"valueField\": \"Amount\" " +
                        "    }], " +
                        "    \"depth3D\": 20, " +
                        "	\"angle\": 30, " +
                        "    \"chartCursor\": { " +
                        "        \"categoryBalloonEnabled\": false, " +
                        "        \"cursorAlpha\": 0, " +
                        "        \"zoomable\": false " +
                        "    }, " +
                        "    \"categoryField\": \"ASPName\", " +
                        "    \"categoryAxis\": { " +
                        "        \"gridPosition\": \"start\", " +
                        "        \"labelRotation\": 90 " +
                        "    }, " +
                        "    \"export\": { " +
                        "    	\"enabled\": true " +
                        "     } " +
                        " " +
                        "}";
            //</editor-fold>
        return aspAnalysisChartData;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Customer Summary">
     public List<CustomerSummary> getCustomerSummary() {
        return customerSummary;
    }

    public void setCustomerSummary(List<CustomerSummary> customerSummary) {
        this.customerSummary = customerSummary;
    }
    
    public void calculateCustomerSummary(){
       List<VendorPo> customerPos = vendorPoController.getExportItems(start, end);
       List<AspPo> aspPos = aspPoController.getExportItems(start, end);
       customerSummary = new ArrayList<>();
       CustomerSummary summary = new CustomerSummary();
        for (VendorPo customerPo : customerPos) {
            summary.setTotalPoValue(customerPo.getPoValue());
            summary.setTotalRemainingInPo((customerPo.getRemainingInPo()!=null?customerPo.getRemainingInPo():BigInteger.ZERO));
            summary.setTotalMdDeserved((customerPo.getMdDeserved()!=null?customerPo.getMdDeserved():BigInteger.ZERO));
            Object[] mds = customerPo.getVendorMdCollection().toArray();
           for (Object md : mds) {
               VendorMd vMd = ((VendorMd)md);
               summary.setTotalMdDeserved((vMd.getMdDeserved()!=null?vMd.getMdDeserved():BigInteger.ZERO));
               summary.setTotalRemainingFromMd((vMd.getRemainingInMd()!=null?vMd.getRemainingInMd():BigInteger.ZERO));
               summary.setTotalMdRecieved((vMd.getMdValue()!=null?vMd.getMdValue():BigInteger.ZERO));
               if(vMd.getInvoiced()!=null){
                if(vMd.getInvoiced()){
                   summary.setTotalMdValue((vMd.getMdValue()!=null?vMd.getMdValue():BigInteger.ZERO));
                }
               }
           }
        }
        
        for (AspPo aspPo : aspPos) {
            summary.setTotalCommittedCost((aspPo.getGrnDeserved()!=null?aspPo.getGrnDeserved():BigInteger.ZERO));
        }
        customerSummary.add(summary);
       
    }
//</editor-fold>
    
    public String getColor() {
        Random randomGenerator = new Random(); // Construct a new Random number generator
        int randomNumber = randomGenerator.nextInt(mColors.length);
        return mColors[randomNumber];
    }
}