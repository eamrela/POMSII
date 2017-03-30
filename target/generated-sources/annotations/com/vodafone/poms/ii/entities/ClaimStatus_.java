package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-08T13:20:20")
@StaticMetamodel(ClaimStatus.class)
public class ClaimStatus_ { 

    public static volatile SingularAttribute<ClaimStatus, String> claimName;
    public static volatile SingularAttribute<ClaimStatus, String> description;
    public static volatile CollectionAttribute<ClaimStatus, Activity> activityCollection;

}