package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-04T19:02:32")
@StaticMetamodel(Sites.class)
public class Sites_ { 

    public static volatile CollectionAttribute<Sites, Activity> activityCollection;
    public static volatile SingularAttribute<Sites, String> sitePhysical;
    public static volatile SingularAttribute<Sites, String> gfRt;

}