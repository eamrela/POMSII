/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Amr
 */
@Entity
@Table(name = "activity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Activity.findAll", query = "SELECT a FROM Activity a")
    , @NamedQuery(name = "Activity.findByActivityId", query = "SELECT a FROM Activity a WHERE a.activityId = :activityId")
    , @NamedQuery(name = "Activity.findByActivityDate", query = "SELECT a FROM Activity a WHERE a.activityDate = :activityDate")
    , @NamedQuery(name = "Activity.findByActivityDetails", query = "SELECT a FROM Activity a WHERE a.activityDetails = :activityDetails")
    , @NamedQuery(name = "Activity.findByQty", query = "SELECT a FROM Activity a WHERE a.qty = :qty")
    , @NamedQuery(name = "Activity.findByTotalPriceVendor", query = "SELECT a FROM Activity a WHERE a.totalPriceVendor = :totalPriceVendor")
    , @NamedQuery(name = "Activity.findByTotalPriceVendorTaxes", query = "SELECT a FROM Activity a WHERE a.totalPriceVendorTaxes = :totalPriceVendorTaxes")
    , @NamedQuery(name = "Activity.findByTotalPriceAsp", query = "SELECT a FROM Activity a WHERE a.totalPriceAsp = :totalPriceAsp")
    , @NamedQuery(name = "Activity.findByTotalUm", query = "SELECT a FROM Activity a WHERE a.totalUm = :totalUm")
    , @NamedQuery(name = "Activity.findByTotalUmPercent", query = "SELECT a FROM Activity a WHERE a.totalUmPercent = :totalUmPercent")
    , @NamedQuery(name = "Activity.findByActivityComment", query = "SELECT a FROM Activity a WHERE a.activityComment = :activityComment")
    , @NamedQuery(name = "Activity.findBySysDate", query = "SELECT a FROM Activity a WHERE a.sysDate = :sysDate")
    , @NamedQuery(name = "Activity.findByTaxes", query = "SELECT a FROM Activity a WHERE a.taxes = :taxes")})
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "activity_id")
    private Long activityId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activity_date")
    @Temporal(TemporalType.DATE)
    private Date activityDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "activity_details")
    private String activityDetails;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qty")
    private int qty;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_price_vendor")
    private Float totalPriceVendor;
    @Column(name = "total_price_vendor_taxes")
    private Float totalPriceVendorTaxes;
    @Column(name = "total_price_asp")
    private Float totalPriceAsp;
    @Column(name = "total_um")
    private Float totalUm;
    @Column(name = "total_um_percent")
    private Float totalUmPercent;
    @Size(max = 2147483647)
    @Column(name = "activity_comment")
    private String activityComment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sys_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sysDate;
    @Basic(optional = false)
    @Column(name = "taxes")
    private Double taxes;
    @JoinTable(name = "asp_po_j_activity", joinColumns = {
        @JoinColumn(name = "activity_id", referencedColumnName = "activity_id")}, inverseJoinColumns = {
        @JoinColumn(name = "asp_po_id", referencedColumnName = "po_number")})
    @ManyToMany
    private Collection<AspPo> aspPoCollection;
    @JoinColumn(name = "activity_code", referencedColumnName = "material_id")
    @ManyToOne(optional = false)
    private ActivityCode activityCode;
    @JoinColumn(name = "approval_status", referencedColumnName = "status_name")
    @ManyToOne
    private ApprovalStatus approvalStatus;
    @JoinColumn(name = "area", referencedColumnName = "area_name")
    @ManyToOne(optional = false)
    private Area area;
    @JoinColumn(name = "claim_status", referencedColumnName = "claim_name")
    @ManyToOne
    private ClaimStatus claimStatus;
    @JoinColumn(name = "activity_type", referencedColumnName = "domain_name")
    @ManyToOne(optional = false)
    private DomainNames activityType;
    @JoinColumn(name = "phase", referencedColumnName = "phase_name")
    @ManyToOne
    private Phases phase;
    @JoinColumn(name = "site", referencedColumnName = "site_physical")
    @ManyToOne(optional = false)
    private Sites site;
    @JoinColumn(name = "asp", referencedColumnName = "subcontractor_name")
    @ManyToOne(optional = false)
    private Subcontractors asp;
    @JoinColumn(name = "creator", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Users creator;
    @JoinColumn(name = "vendor_owner", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private VendorOwner vendorOwner;

    public Activity() {
    }

    public Activity(Long activityId) {
        this.activityId = activityId;
    }

    public Activity(Long activityId, Date activityDate, String activityDetails, int qty, Date sysDate, Double taxes) {
        this.activityId = activityId;
        this.activityDate = activityDate;
        this.activityDetails = activityDetails;
        this.qty = qty;
        this.sysDate = sysDate;
        this.taxes = taxes;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(String activityDetails) {
        this.activityDetails = activityDetails;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Float getTotalPriceVendor() {
        if(activityCode!=null){
        totalPriceVendor = activityCode.getVendorPrice()*qty;
        }
        return totalPriceVendor;
    }

    public void setTotalPriceVendor(Float totalPriceVendor) {
        this.totalPriceVendor = totalPriceVendor;
    }

    public Float getTotalPriceVendorTaxes() {
        if(totalPriceVendor!=null){
        totalPriceVendorTaxes = totalPriceVendor+(totalPriceVendor*Float.valueOf(taxes.toString()));
        }
        return totalPriceVendorTaxes;
    }

    public void setTotalPriceVendorTaxes(Float totalPriceVendorTaxes) {
        this.totalPriceVendorTaxes = totalPriceVendorTaxes;
    }

    public Float getTotalPriceAsp() {
        if(activityCode!=null){
        totalPriceAsp = activityCode.getSubcontractorPrice()*qty;
        }
        return totalPriceAsp;
    }

    public void setTotalPriceAsp(Float totalPriceAsp) {
        this.totalPriceAsp = totalPriceAsp;
    }

    public Float getTotalUm() {
        if(totalPriceVendor!=null && totalPriceAsp!=null){
        totalUm = totalPriceVendor-totalPriceAsp;
        }
        return totalUm;
    }

    public void setTotalUm(Float totalUm) {
        this.totalUm = totalUm;
    }

    public Float getTotalUmPercent() {
        if(totalUm!=null && totalPriceVendor!=null){
        totalUmPercent = totalUm/totalPriceVendor;
        }
        return totalUmPercent;
    }

    public void setTotalUmPercent(Float totalUmPercent) {
        this.totalUmPercent = totalUmPercent;
    }

    public String getActivityComment() {
        return activityComment;
    }

    public void setActivityComment(String activityComment) {
        this.activityComment = activityComment;
    }

    public Date getSysDate() {
        return sysDate;
    }

    public void setSysDate(Date sysDate) {
        this.sysDate = sysDate;
    }

    public Double getTaxes() {
        return taxes;
    }

    public void setTaxes(Double taxes) {
        this.taxes = taxes;
    }

    @XmlTransient
    public Collection<AspPo> getAspPoCollection() {
        return aspPoCollection;
    }

    public void setAspPoCollection(Collection<AspPo> aspPoCollection) {
        this.aspPoCollection = aspPoCollection;
    }

    public ActivityCode getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(ActivityCode activityCode) {
        this.activityCode = activityCode;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public DomainNames getActivityType() {
        return activityType;
    }

    public void setActivityType(DomainNames activityType) {
        this.activityType = activityType;
    }

    public Phases getPhase() {
        return phase;
    }

    public void setPhase(Phases phase) {

        this.phase = phase;
    }

    public Sites getSite() {
        return site;
    }

    public void setSite(Sites site) {
        this.site = site;
    }

    public Subcontractors getAsp() {
        return asp;
    }

    public void setAsp(Subcontractors asp) {
        this.asp = asp;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    public VendorOwner getVendorOwner() {
        return vendorOwner;
    }

    public void setVendorOwner(VendorOwner vendorOwner) {
        this.vendorOwner = vendorOwner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activityId != null ? activityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
       
        if (!(object instanceof Activity)) {
            return false;
        }
        Activity other = (Activity) object;
        if ((this.activityId == null && other.activityId != null) || (this.activityId != null && !this.activityId.equals(other.activityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.poms.ii.entities.Activity[ activityId=" + activityId + " ]";
    }
    
}
