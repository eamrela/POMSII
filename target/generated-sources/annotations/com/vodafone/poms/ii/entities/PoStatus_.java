package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.VendorPo;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-02-06T13:47:08")
@StaticMetamodel(PoStatus.class)
public class PoStatus_ { 

    public static volatile SingularAttribute<PoStatus, String> statusName;
    public static volatile SingularAttribute<PoStatus, String> description;
    public static volatile CollectionAttribute<PoStatus, VendorPo> vendorPoCollection;
    public static volatile CollectionAttribute<PoStatus, AspPo> aspPoCollection;

}