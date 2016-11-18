package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.VendorPo;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-18T21:09:03")
@StaticMetamodel(PoTypes.class)
public class PoTypes_ { 

    public static volatile SingularAttribute<PoTypes, String> typeName;
    public static volatile SingularAttribute<PoTypes, String> description;
    public static volatile CollectionAttribute<PoTypes, VendorPo> vendorPoCollection;
    public static volatile CollectionAttribute<PoTypes, AspPo> aspPoCollection;

}