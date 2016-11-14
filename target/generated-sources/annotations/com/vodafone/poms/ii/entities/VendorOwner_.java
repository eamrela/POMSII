package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-14T15:44:52")
@StaticMetamodel(VendorOwner.class)
public class VendorOwner_ { 

    public static volatile SingularAttribute<VendorOwner, Long> id;
    public static volatile SingularAttribute<VendorOwner, String> ownerName;
    public static volatile CollectionAttribute<VendorOwner, Activity> activityCollection;
    public static volatile SingularAttribute<VendorOwner, String> ownerNumber;

}