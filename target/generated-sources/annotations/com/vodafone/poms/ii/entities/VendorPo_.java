package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.DomainNames;
import com.vodafone.poms.ii.entities.EricssonPoChaser;
import com.vodafone.poms.ii.entities.PoOwner;
import com.vodafone.poms.ii.entities.PoStatus;
import com.vodafone.poms.ii.entities.PoTypes;
import com.vodafone.poms.ii.entities.Users;
import com.vodafone.poms.ii.entities.VendorMd;
import com.vodafone.poms.ii.entities.VendorPoWorkDone;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-20T17:22:53")
@StaticMetamodel(VendorPo.class)
public class VendorPo_ { 

    public static volatile SingularAttribute<VendorPo, PoOwner> poOwner;
    public static volatile SingularAttribute<VendorPo, EricssonPoChaser> poChaser;
    public static volatile SingularAttribute<VendorPo, BigInteger> remainingInPo;
    public static volatile SingularAttribute<VendorPo, Double> workDone;
    public static volatile SingularAttribute<VendorPo, String> poDescription;
    public static volatile SingularAttribute<VendorPo, BigInteger> serviceValue;
    public static volatile CollectionAttribute<VendorPo, VendorMd> vendorMdCollection;
    public static volatile SingularAttribute<VendorPo, BigInteger> mdDeserved;
    public static volatile SingularAttribute<VendorPo, DomainNames> domainName;
    public static volatile SingularAttribute<VendorPo, String> poNumber;
    public static volatile SingularAttribute<VendorPo, Double> factor;
    public static volatile SingularAttribute<VendorPo, Users> creator;
    public static volatile SingularAttribute<VendorPo, Date> poDate;
    public static volatile CollectionAttribute<VendorPo, Activity> activityCollection;
    public static volatile SingularAttribute<VendorPo, Date> sysDate;
    public static volatile SingularAttribute<VendorPo, BigInteger> poValueTaxes;
    public static volatile SingularAttribute<VendorPo, PoStatus> poStatus;
    public static volatile SingularAttribute<VendorPo, BigInteger> poValue;
    public static volatile CollectionAttribute<VendorPo, VendorPoWorkDone> vendorPoWorkDoneCollection;
    public static volatile SingularAttribute<VendorPo, PoTypes> poType;
    public static volatile SingularAttribute<VendorPo, Double> taxes;
    public static volatile CollectionAttribute<VendorPo, AspPo> aspPoCollection;

}