/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.helpers;

import com.vodafone.poms.ii.controllers.AspGrnController;
import com.vodafone.poms.ii.controllers.AspPoController;
import com.vodafone.poms.ii.controllers.VendorInvoiceController;
import com.vodafone.poms.ii.controllers.VendorMdController;
import com.vodafone.poms.ii.controllers.VendorPoController;
import com.vodafone.poms.ii.entities.AspGrn;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.DomainNames;
import com.vodafone.poms.ii.entities.VendorInvoice;
import com.vodafone.poms.ii.entities.VendorMd;
import com.vodafone.poms.ii.entities.VendorPo;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
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
    private Float sekConversionRate = null;
    private String currency = null;
    private String startStr;
    private String endStr;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private List<DomainNames> selectedDomains;
    private String selectedDomainsStr="*";
    
    // Figures 
    private BigInteger NS;
    private BigInteger COS;
    private BigInteger UM;
    private Double UMPercent;
    private List<VendorInvoice> NS_invoices;
    private List<AspGrn> COS_Grns;
    // Remaining Not Yet invoiced
    private List<VendorMd> remainingNotyetInvoiced;
    private BigInteger remainingNotyetInvoiced_Value;
    // Commited Cost
     private List<AspPo> committedCost;
    private BigInteger committedCost_Value;
    
    
    private List<CustomerSummary> customerSummary;
    
    private List<ASPAnalysis> aspAnalysis;
    private List<DomainAnalysis> domainAnalysis;
    private List<MasterGraph> masterGraph;
    private String aspAnalysisChartData;
    private String domainAnalysisChartData;
    private String masterGraphData;
    
    @PersistenceContext(unitName = "com.vodafone_POMS-II_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @Inject
    private VendorInvoiceController vendorInvoiceController;
    @Inject
    private VendorMdController vendorMdController;
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
    
//    @PostConstruct
    public void refresh(boolean redirect){
        calculateDashboardFigues();
        calculateRemainingNotYetInvoiced();
        calculateCommitedCost();
        getAspAnalysis();
        getDomainAnalysis();
        calculateCustomerSummary();
        getCurrency();
        if(redirect){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }

    private static double round (double value, int precision) {
    int scale = (int) Math.pow(10, precision);
    return (double) Math.round(value * scale) / scale;
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
        customerSummary = null;
        refresh(false);
    }

    
    
    
    //<editor-fold defaultstate="collapsed" desc="new Dashboard">
    public void calculateDashboardFigues(){
        NS_invoices = vendorInvoiceController.findDashboardItems(start, end,getSelectedDomainsStr());
        COS_Grns = aspGrnController.getDashboardItems(start, end,getSelectedDomainsStr());
        NS = BigInteger.ZERO;
        COS = BigInteger.ZERO;
        for (VendorInvoice NS_invoice : NS_invoices) {
            NS = NS.add(NS_invoice.getInvoiceValue());
        }
        for (AspGrn COS_Grn : COS_Grns) {
            COS = COS.add(COS_Grn.getGrnValue());
        }
        UM = NS.subtract(COS);
        if(NS.compareTo(BigInteger.ZERO)==1){
        UMPercent = round(Double.valueOf((UM.floatValue()/NS.floatValue())*100),1);
        }else{
        UMPercent = 0.0;
        }
    }
    public void calculateRemainingNotYetInvoiced(){
        remainingNotyetInvoiced = vendorMdController.getDashboardItems(getSelectedDomainsStr());
        remainingNotyetInvoiced_Value = BigInteger.ZERO;
        BigInteger totalMdValue = BigInteger.ZERO;
        BigInteger totalInvoiceValue = BigInteger.ZERO;
        remainingNotyetInvoiced_Value = BigInteger.ZERO;
        for (VendorMd vendorMd : remainingNotyetInvoiced) {
           totalMdValue = totalMdValue.add(vendorMd.getMdValue()!=null?vendorMd.getMdValue():BigInteger.ZERO);
           vendorMdController.setSelected(vendorMd);
           List<VendorInvoice> invoices = vendorInvoiceController.getSelectedMdItems();
            for (VendorInvoice invoice : invoices) {
                totalInvoiceValue = totalInvoiceValue.add(invoice.getInvoiceValue()!=null?invoice.getInvoiceValue():BigInteger.ZERO);
            }
        }
        remainingNotyetInvoiced_Value = totalMdValue.subtract(totalInvoiceValue);
    }
    public void calculateCommitedCost(){
        committedCost = aspPoController.getDashboardCommittedCost(getSelectedDomainsStr());
        committedCost_Value = BigInteger.ZERO;
        for (AspPo committedCost1 : committedCost) {
            BigInteger totalWorkValue = 
                    BigDecimal.valueOf(committedCost1.getWorkDone().floatValue()*committedCost1.getServiceValue().floatValue()).toBigInteger();
            aspPoController.setSelected(committedCost1);
            List<AspGrn> grns = aspGrnController.getSelectedPoItems();
            BigInteger grnValues = BigInteger.ZERO;
            for (int i = 0; i < grns.size(); i++) {
                grnValues = grnValues.add(grns.get(i).getGrnValue()!=null?grns.get(i).getGrnValue():BigInteger.ZERO);
            }
            committedCost_Value = committedCost_Value.add((totalWorkValue.subtract(grnValues)));
            
        }
    }
//</editor-fold>
    
    
    
    
    public List<AspGrn> getCOS_Grns() {
        return COS_Grns;
    }

    public void setCOS_Grns(List<AspGrn> COS_Grns) {
        this.COS_Grns = COS_Grns;
    }

    

    
    
    public List<VendorInvoice> getNS_invoices() {
        return NS_invoices;
    }

    public void setNS_invoices(List<VendorInvoice> NS_invoices) {
        this.NS_invoices = NS_invoices;
    }

    public List<VendorMd> getRemainingNotyetInvoiced() {
        return remainingNotyetInvoiced;
    }

    public void setRemainingNotyetInvoiced(List<VendorMd> remainingNotyetInvoiced) {
        this.remainingNotyetInvoiced = remainingNotyetInvoiced;
    }

    public BigInteger getRemainingNotyetInvoiced_Value() {
        return remainingNotyetInvoiced_Value;
    }

    public List<AspPo> getCommittedCost() {
        return committedCost;
    }

    public BigInteger getCommittedCost_Value() {
        return committedCost_Value;
    }
    

    
    
    
    //<editor-fold defaultstate="collapsed" desc="Setters/Getters">
   public void setStart(Date start) {
        this.start = start;
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
    
    public BigInteger getNS() {
        return BigDecimal.valueOf(NS.floatValue()*getSekConversionRate()).toBigInteger();
    }

    public void setNS(BigInteger NS) {
        this.NS = NS;
    }

    public BigInteger getCOS() {
        return BigDecimal.valueOf(COS.floatValue()*getSekConversionRate()).toBigInteger();
    }

    public String getStartStr() {
        startStr = sdf.format(start);
        return startStr;
    }

    public void setStartStr(String startStr) {
        this.startStr = startStr;
    }

    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }

    public String getEndStr() {
        endStr = sdf.format(end);
        return endStr;
    }
    

    public void setCOS(BigInteger COS) {
        this.COS = COS;
    }

    public BigInteger getUM() {
        return BigDecimal.valueOf(UM.floatValue()*getSekConversionRate()).toBigInteger();
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

    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Charts Data">
    public String getDomainAnalysisChartData(){
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
    //getMasterGraphData
    public String getMasterGraphData() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM-YYYY");
        //<editor-fold defaultstate="collapsed" desc="Data Access">
        masterGraph = em.createNativeQuery(" select target_month,ns_t,cos_t,um_t,ns_iso,cos_iso,  "+
" case when um_iso is null then 0.0 else um_iso end um_iso  "+
" from (  "+
" select target_month,ns_t,cos_t,um_t,  "+
" case when ns_iso is null then 0.0 else ns_iso end ns_iso,  "+
" case when cos_iso is null then 0.0 else cos_iso end cos_iso,  "+
" (ns_iso-cos_iso) um_iso  "+
" from (  "+
" select target_month,ns_t,cos_t,um_t,  "+
" round(vendor.ns_iso/1000000,2) ns_iso,  "+
" round(asp.cos_iso/1000000,2) cos_iso  "+
" from targets  "+
" left join   "+
" (select extract(month from invoice_date) month_no,sum(invoice_value) ns_iso  "+
" from vendor_invoice  "+
" where invoice_date between '"+sdf.format(DateTime.now().withMonthOfYear(1).toDate())+"' and '"+sdf.format(DateTime.now().withMonthOfYear(12).toDate())+"'  "+
(!getSelectedDomainsStr().contains("*")?"and  md_id in " +
" (select id from vendor_md where vendor_po_id in  " +
" (select po_number from vendor_po where domain_name in ("+getSelectedDomainsStr()+")))":"")+
" group by extract(month from invoice_date)) vendor  "+
" on target_month=vendor.month_no  "+
" left join   "+
" (select month_no,sum(cos_iso) cos_iso  "+
" from (  "+
" select extract(month from grn_date) month_no,sum(grn_value) cos_iso  "+
" from asp_grn  "+
" where grn_date between '"+sdf.format(DateTime.now().withMonthOfYear(1).toDate())+"' and '"+sdf.format(DateTime.now().withMonthOfYear(12).toDate())+"'  "+
(!getSelectedDomainsStr().contains("*")?" and asp_po_id in (select po_number from asp_po where domain_name in ("+getSelectedDomainsStr()+"))":"")+
" group by extract(month from grn_date)   "+
" union   "+
" select extract(month from po_date) month_no,sum(grn_deserved) cos_iso   "+
" from asp_po   "+
" where po_date between '"+sdf.format(DateTime.now().withMonthOfYear(1).toDate())+"' and '"+sdf.format(DateTime.now().withMonthOfYear(12).toDate())+"'  "+
" group by extract(month from po_date) ) a  "+
" group by a.month_no) asp  "+
" on target_month=asp.month_no) master_initial)  "+
" master ", MasterGraph.class).getResultList();
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Start of string">
        
        masterGraphData = "{\n" +
                            "    \"theme\": \"none\", " +
                            "    \"type\": \"serial\", " +
                            "    \"dataProvider\": [";
//</editor-fold>

        for (int i = 0; i < masterGraph.size(); i++) {
            
            masterGraphData+= 
                        "{ " +
                        "\"date\": \""+dateFormatter.format(DateTime.now().withMonthOfYear(
                                        masterGraph.get(i).getMonth()).toDate())+"\", " +
                        "    \"ns_iso\": "+masterGraph.get(i).getNS_ISO()+", " +
                        "    \"cos_iso\": "+masterGraph.get(i).getCOS_ISO()+", " +
                        "    \"um_iso\": "+masterGraph.get(i).getUM_ISO()+", " +
                        "    \"ns_t\": "+masterGraph.get(i).getNS_T()+", " +
                        "    \"cos_t\": "+masterGraph.get(i).getCOS_T()+", " +
                        "    \"um_t\": "+masterGraph.get(i).getUM_T()+""+
                        "}"+(i==masterGraph.size()-1?"":",");
        }
 
            //<editor-fold defaultstate="collapsed" desc="End of String">

            masterGraphData+= "]," +
"    \"legend\": {" +
"    \"useGraphSettings\": true," +
"    \"position\": \"top\"" +
"  }," +
"    \"startDuration\": 1," +
"    \"graphs\": [{" +
"        \"balloonText\": \"[[title]]<br /><b style='font-size: 130%'>[[value]]</b>\"," +
"        \"fillAlphas\": 0.9," +
"        \"lineAlpha\": 0.2," +
"        \"title\": \"NS(ISO)\"," +
"        \"type\": \"column\"," +
"        \"valueField\": \"ns_iso\"" +
"    }, " +
"    {" +
"        \"balloonText\": \"[[title]]<br /><b style='font-size: 130%'>[[value]]</b>\"," +
"        \"fillAlphas\": 0.9," +
"        \"lineAlpha\": 0.2," +
"        \"title\": \"COS(ISO)\"," +
"        \"type\": \"column\"," +
"        \"valueField\": \"cos_iso\"" +
"    }," +
"    {" +
"        \"balloonText\": \"[[title]]<br /><b style='font-size: 130%'>[[value]]</b>\"," +
"        \"fillAlphas\": 0.9," +
"        \"lineAlpha\": 0.2," +
"        \"title\": \"UM(ISO)\"," +
"        \"type\": \"column\"," +
"        \"valueField\": \"um_iso\"" +
"    }," +
"    {" +
"    \"bullet\": \"round\"," +
"    \"bulletBorderAlpha\": 1," +
"    \"bulletColor\": \"#FFFFFF\"," +
"    \"bulletSize\": 5," +
"    \"hideBulletsCount\": 50," +
"    \"lineThickness\": 2," +
"    \"lineColor\": \"#7D8778\"," +
"    \"type\": \"smoothedLine\"," +
"    \"dashLength\": 1," +
"    \"title\": \"NS(T)\"," +
"    \"useLineColorForBulletBorder\": true," +
"    \"valueField\": \"ns_t\"," +
"    \"balloonText\": \"[[title]]<br /><b style='font-size: 130%'>[[value]]</b>\"" +
"  }," +
"  {" +
"    \"bullet\": \"round\"," +
"    \"bulletBorderAlpha\": 1," +
"    \"bulletColor\": \"#FFFFFF\"," +
"    \"bulletSize\": 5," +
"    \"hideBulletsCount\": 50," +
"    \"lineThickness\": 2," +
"    \"lineColor\": \"#2564E3\"," +
"    \"type\": \"smoothedLine\"," +
"    \"dashLength\": 1," +
"    \"title\": \"COS(T)\"," +
"    \"useLineColorForBulletBorder\": true," +
"    \"valueField\": \"cos_t\"," +
"    \"balloonText\": \"[[title]]<br /><b style='font-size: 130%'>[[value]]</b>\"" +
"  }," +
"  {" +
"    \"bullet\": \"round\"," +
"    \"bulletBorderAlpha\": 1," +
"    \"bulletColor\": \"#FFFFFF\"," +
"    \"bulletSize\": 5," +
"    \"hideBulletsCount\": 50," +
"    \"lineThickness\": 2," +
"    \"lineColor\": \"#F3126B\"," +
"    \"type\": \"smoothedLine\"," +
"    \"dashLength\": 1," +
"    \"title\": \"UM(T)\"," +
"    \"useLineColorForBulletBorder\": true," +
"    \"valueField\": \"um_t\"," +
"    \"balloonText\": \"[[title]]<br /><b style='font-size: 130%'>[[value]]</b>\"" +
"  }]," +
"    \"plotAreaFillAlphas\": 0.1," +
"    \"depth3D\": 10," +
"    \"angle\": 5," +
"    \"categoryField\": \"date\"," +
"    \"categoryAxis\": {" +
"        \"gridPosition\": \"start\"" +
"    }," +
"    \"export\": {" +
"      \"enabled\": true" +
"     }," +
"     \"chartCursor\": {" +
"    \"pan\": true," +
"    \"valueLineEnabled\": true," +
"    \"valueLineBalloonEnabled\": true," +
"    \"cursorAlpha\": 0," +
"    \"valueLineAlpha\": 0.2 " +
"  } " +
"}";
            //</editor-fold>
        return masterGraphData;
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
       List<VendorPo> customerPos = vendorPoController.findAll();
       List<AspPo> aspPos = aspPoController.findAll();
       customerSummary = new ArrayList<>();
       CustomerSummary summary = new CustomerSummary();
       BigInteger totalPoValue=BigInteger.ZERO;
       BigInteger totalRemainingFromPo=BigInteger.ZERO;
       BigInteger totalMdRecieved=BigInteger.ZERO;
       BigInteger totalMdDeserved=BigInteger.ZERO;
       BigInteger totalInvoiceDeserved=BigInteger.ZERO;
       BigInteger totalInvoiceValue=BigInteger.ZERO;
       BigInteger totalRemainingFromInvoice=BigInteger.ZERO;
       BigInteger totalRemainingFromMd=BigInteger.ZERO;
        for (VendorPo customerPo : customerPos) {
            totalPoValue = totalPoValue.add(customerPo.getPoValue());
            totalRemainingFromPo = totalRemainingFromPo.add(customerPo.getRemainingInPo()!=null?
                                customerPo.getRemainingInPo():BigInteger.ZERO);
            totalMdDeserved = totalMdDeserved.add(
                    BigDecimal.valueOf(customerPo.getWorkDone()*customerPo.getServiceValue().floatValue()).toBigInteger());
            vendorPoController.setSelected(customerPo);
            List<VendorMd> mds=vendorMdController.getSelectedPoItems();
           for (VendorMd md : mds) {
               totalMdRecieved = totalMdRecieved.add(md.getMdValue()!=null?md.getMdValue():BigInteger.ZERO);
               vendorMdController.setSelected(md);
               List<VendorInvoice> invoices = vendorInvoiceController.getSelectedMdItems();
                for (VendorInvoice invoice : invoices) {
                    totalInvoiceValue = totalInvoiceValue.add(
                            invoice.getInvoiceValue()!=null?invoice.getInvoiceValue():BigInteger.ZERO);
                    totalInvoiceDeserved = totalInvoiceDeserved.add(
                            invoice.getInvoiceDeserved()!=null?invoice.getInvoiceDeserved():BigInteger.ZERO);
                    
                }
           }
        }
        totalRemainingFromInvoice = totalInvoiceDeserved.subtract(totalInvoiceValue);
        totalRemainingFromMd = totalMdDeserved.subtract(totalMdRecieved);
        
        for (AspPo aspPo : aspPos) {
            summary.setTotalCommittedCost((aspPo.getGrnDeserved()!=null?aspPo.getGrnDeserved():BigInteger.ZERO));
        }
        summary.setTotalMdDeserved(totalMdDeserved);
        summary.setTotalMdRecieved(totalMdRecieved);
        summary.setTotalRemainingFromMd(totalRemainingFromMd);
        
        summary.setTotalPoValue(totalPoValue);
        summary.setTotalRemainingInPo(totalRemainingFromPo);
        
        summary.setTotalMdValue(totalInvoiceValue);
        summary.setTotalMdNotYetInvoiced(totalRemainingFromInvoice);
        customerSummary.add(summary);
       
    }
//</editor-fold>
    
    public String getColor() {
        Random randomGenerator = new Random(); // Construct a new Random number generator
        int randomNumber = randomGenerator.nextInt(mColors.length);
        return mColors[randomNumber];
    }

    public List<DomainNames> getSelectedDomains() {
        return selectedDomains;
    }

    public void setSelectedDomains(List<DomainNames> selectedDomains) {
        this.selectedDomains = selectedDomains;
    }

    public String getSelectedDomainsStr() {
        selectedDomainsStr = "*";
        if(selectedDomains!=null){
            if(!selectedDomains.isEmpty()){
                selectedDomainsStr="";
            }
            for (int i = 0; i < selectedDomains.size(); i++) {
                if(i==selectedDomains.size()-1){
                    selectedDomainsStr += "\'"+selectedDomains.get(i).getDomainName()+"\' ";
                }else{
                    selectedDomainsStr += "\'"+selectedDomains.get(i).getDomainName()+"\', ";
                }
            }
        }
        return selectedDomainsStr;
    }

    public Float getSekConversionRate() {
        if(sekConversionRate==null){
            return 1f;
        }
        return sekConversionRate;
    }

    public void setSekConversionRate(Float sekConversionRate) {
        this.sekConversionRate = sekConversionRate;
        
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        if(currency==null){
            return "EGP";
        }
        return this.currency;
    }
    
    
    
    
}
