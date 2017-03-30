package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-08T13:20:20")
@StaticMetamodel(ActivityCode.class)
public class ActivityCode_ { 

    public static volatile SingularAttribute<ActivityCode, Float> subcontractorPrice;
    public static volatile SingularAttribute<ActivityCode, Integer> quantityRequested;
    public static volatile SingularAttribute<ActivityCode, Float> um;
    public static volatile SingularAttribute<ActivityCode, String> description;
    public static volatile SingularAttribute<ActivityCode, Float> umPercent;
    public static volatile SingularAttribute<ActivityCode, Float> vendorPrice;
    public static volatile CollectionAttribute<ActivityCode, Activity> activityCollection;
    public static volatile SingularAttribute<ActivityCode, String> materialId;

}