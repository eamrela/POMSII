package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.ActivityCode;
import com.vodafone.poms.ii.entities.ApprovalStatus;
import com.vodafone.poms.ii.entities.Area;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.ClaimStatus;
import com.vodafone.poms.ii.entities.DomainNames;
import com.vodafone.poms.ii.entities.Phases;
import com.vodafone.poms.ii.entities.Sites;
import com.vodafone.poms.ii.entities.Subcontractors;
import com.vodafone.poms.ii.entities.Users;
import com.vodafone.poms.ii.entities.VendorOwner;
import com.vodafone.poms.ii.entities.VendorPo;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-20T17:22:53")
@StaticMetamodel(Activity.class)
public class Activity_ { 

    public static volatile SingularAttribute<Activity, Float> totalPriceAsp;
    public static volatile SingularAttribute<Activity, String> acDescription;
    public static volatile SingularAttribute<Activity, Float> totalUm;
    public static volatile SingularAttribute<Activity, String> acMaterialId;
    public static volatile SingularAttribute<Activity, Float> acSubcontractorPrice;
    public static volatile SingularAttribute<Activity, Float> totalPriceVendor;
    public static volatile SingularAttribute<Activity, VendorOwner> vendorOwner;
    public static volatile SingularAttribute<Activity, Users> creator;
    public static volatile SingularAttribute<Activity, Long> activityId;
    public static volatile SingularAttribute<Activity, String> activityComment;
    public static volatile SingularAttribute<Activity, Float> acVendorPrice;
    public static volatile SingularAttribute<Activity, Area> area;
    public static volatile SingularAttribute<Activity, Date> sysDate;
    public static volatile SingularAttribute<Activity, String> correlateTo;
    public static volatile SingularAttribute<Activity, ApprovalStatus> approvalStatus;
    public static volatile SingularAttribute<Activity, Sites> site;
    public static volatile SingularAttribute<Activity, Float> acUm;
    public static volatile SingularAttribute<Activity, ActivityCode> activityCode;
    public static volatile SingularAttribute<Activity, String> activityDetails;
    public static volatile SingularAttribute<Activity, Float> totalUmPercent;
    public static volatile SingularAttribute<Activity, ClaimStatus> claimStatus;
    public static volatile SingularAttribute<Activity, Double> qty;
    public static volatile SingularAttribute<Activity, Float> acUmPercent;
    public static volatile SingularAttribute<Activity, Float> totalPriceVendorTaxes;
    public static volatile SingularAttribute<Activity, Date> activityDate;
    public static volatile CollectionAttribute<Activity, VendorPo> vendorPoCollection;
    public static volatile SingularAttribute<Activity, Subcontractors> asp;
    public static volatile SingularAttribute<Activity, DomainNames> activityType;
    public static volatile SingularAttribute<Activity, Phases> phase;
    public static volatile SingularAttribute<Activity, Double> taxes;
    public static volatile CollectionAttribute<Activity, AspPo> aspPoCollection;

}