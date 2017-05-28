package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-25T15:11:53")
@StaticMetamodel(VendorOwner.class)
public class VendorOwner_ { 

    public static volatile SingularAttribute<VendorOwner, String> ownerNumber;
    public static volatile SingularAttribute<VendorOwner, String> ownerName;
    public static volatile SingularAttribute<VendorOwner, Long> id;
    public static volatile CollectionAttribute<VendorOwner, Activity> activityCollection;

}