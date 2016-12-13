package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.VendorPo;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-10T23:07:24")
@StaticMetamodel(DomainNames.class)
public class DomainNames_ { 

    public static volatile CollectionAttribute<DomainNames, Activity> activityCollection;
    public static volatile SingularAttribute<DomainNames, String> description;
    public static volatile CollectionAttribute<DomainNames, VendorPo> vendorPoCollection;
    public static volatile SingularAttribute<DomainNames, String> domainName;
    public static volatile CollectionAttribute<DomainNames, AspPo> aspPoCollection;

}