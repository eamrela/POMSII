package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Users;
import com.vodafone.poms.ii.entities.VendorPo;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-14T15:44:52")
@StaticMetamodel(VendorMd.class)
public class VendorMd_ { 

    public static volatile SingularAttribute<VendorMd, Long> id;
    public static volatile SingularAttribute<VendorMd, Double> mdFactor;
    public static volatile SingularAttribute<VendorMd, Date> sysDate;
    public static volatile SingularAttribute<VendorMd, BigInteger> remainingInMd;
    public static volatile SingularAttribute<VendorMd, BigInteger> mdValue;
    public static volatile SingularAttribute<VendorMd, VendorPo> vendorPoId;
    public static volatile SingularAttribute<VendorMd, BigInteger> mdDeserved;
    public static volatile SingularAttribute<VendorMd, String> mdNumber;
    public static volatile SingularAttribute<VendorMd, Date> mdDate;
    public static volatile SingularAttribute<VendorMd, Boolean> invoiced;
    public static volatile SingularAttribute<VendorMd, Users> creator;

}