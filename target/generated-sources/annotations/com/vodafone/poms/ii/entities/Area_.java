package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-20T17:22:53")
@StaticMetamodel(Area.class)
public class Area_ { 

    public static volatile CollectionAttribute<Area, Activity> activityCollection;
    public static volatile SingularAttribute<Area, String> areaName;
    public static volatile CollectionAttribute<Area, Users> usersCollection;
    public static volatile SingularAttribute<Area, String> areaOwner;

}