package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.AspPo;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-04T19:02:32")
@StaticMetamodel(Subcontractors.class)
public class Subcontractors_ { 

    public static volatile SingularAttribute<Subcontractors, String> spoc;
    public static volatile CollectionAttribute<Subcontractors, Activity> activityCollection;
    public static volatile SingularAttribute<Subcontractors, String> subcontractorName;
    public static volatile SingularAttribute<Subcontractors, String> subcontractorDetails;
    public static volatile CollectionAttribute<Subcontractors, AspPo> aspPoCollection;

}