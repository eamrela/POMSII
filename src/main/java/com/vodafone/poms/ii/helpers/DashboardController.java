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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.joda.time.DateTime;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

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
    private List<AspGrn> aspGrns;
    private List<AspGrn> aspGrnsCOS;
    private List<AspPo> aspCommittedCost;
    private List<VendorMd> vendorMds;
    private List<VendorMd> vendorMdsNS;
    private List<VendorPo> vendorRemainingNotYetinvoiced;
    private BigInteger NS;
    private BigInteger COS;
    private BigInteger UM;
    private Double UMPercent;
    private BigInteger committedCost;
    private BigInteger remainingNotYetInvoiced;
    
    private List<ASPAnalysis> aspAnalysis;
    private String aspAnalysisChartData;
    
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
        end = DateTime.now().withTimeAtStartOfDay().toDate();
        start = DateTime.now().withTimeAtStartOfDay().minusDays(30).toDate();
    }
    
    @PostConstruct
    public void refresh(){
        boolean asp = calculateASP();
        boolean vendor = calculateVendor();
        if(asp && vendor){
            UM = NS.subtract(COS);
            if(NS.compareTo(BigInteger.ZERO)==1){
            UMPercent = Double.valueOf(UM.intValue()/NS.intValue());
            }else{
            UMPercent = 0.0;
            }
        }
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
        vendorRemainingNotYetinvoiced = vendorPoController.getDashboardRemainingNotInvoiced(start, end);
        NS = BigInteger.ZERO;
        remainingNotYetInvoiced = BigInteger.ZERO;
        for (VendorPo vendorRemainingNotYetinvoiced1 : vendorRemainingNotYetinvoiced) {
            remainingNotYetInvoiced = remainingNotYetInvoiced.add(vendorRemainingNotYetinvoiced1.getMdDeserved());
        }
        for (VendorMd vendorMd : vendorMds) {
            if(vendorMd.getInvoiced()!=null){
                if(vendorMd.getInvoiced() && vendorMd.getMdValue()!=null){
                    NS = NS.add(vendorMd.getMdValue());
                    if(vendorMd.getRemainingInMd()!=null){
                        if(vendorMd.getRemainingInMd().compareTo(BigInteger.ZERO)==1){
                            remainingNotYetInvoiced = remainingNotYetInvoiced.add(vendorMd.getRemainingInMd());
                            vendorRemainingNotYetinvoiced.add(vendorMd.getVendorPoId());
                        }
                    }
                }
            }
        }
        return true;
    }

    public List<ASPAnalysis> getAspAnalysis() {
        if(aspAnalysis == null){
            aspAnalysis = em.createNativeQuery(" select asp_name, " +
                                                "	case when val is null then 0 else val end amount " +
                                                " from ( " +
                                                " select subcontractor_name asp_name, " +
                                                "      (select sum(total_price_asp) " +
                                                "       from activity " +
                                                "       where asp = asp.subcontractor_name " +
                                                "       and activity_date between '"+sdf.format(start)+"' "
                                                + " and '"+sdf.format(end)+"' " +
                                                "       ) val " +
                                                "from subcontractors asp) a",ASPAnalysis.class).getResultList();
        }
        return aspAnalysis;
    }

    

    
    
    
    //<editor-fold defaultstate="collapsed" desc="Setters/Getters">
    public void setRemainingNotYetInvoiced(BigInteger remainingNotYetInvoiced) {
        this.remainingNotYetInvoiced = remainingNotYetInvoiced;
    }

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

    public List<VendorPo> getVendorRemainingNotYetinvoiced() {
        return vendorRemainingNotYetinvoiced;
    }

    public void setVendorRemainingNotYetinvoiced(List<VendorPo> vendorRemainingNotYetinvoiced) {
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
    
    
    public String getColor() {
        Random randomGenerator = new Random(); // Construct a new Random number generator
        int randomNumber = randomGenerator.nextInt(mColors.length);
        return mColors[randomNumber];
    }
}
