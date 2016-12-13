package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-10T23:07:24")
@StaticMetamodel(ApprovalStatus.class)
public class ApprovalStatus_ { 

    public static volatile CollectionAttribute<ApprovalStatus, Activity> activityCollection;
    public static volatile SingularAttribute<ApprovalStatus, String> statusName;
    public static volatile SingularAttribute<ApprovalStatus, String> description;

}