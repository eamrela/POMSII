package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.AspGrn;
import com.vodafone.poms.ii.entities.AspPoWorkDone;
import com.vodafone.poms.ii.entities.DomainNames;
import com.vodafone.poms.ii.entities.EricssonPoChaser;
import com.vodafone.poms.ii.entities.PoStatus;
import com.vodafone.poms.ii.entities.PoTypes;
import com.vodafone.poms.ii.entities.Subcontractors;
import com.vodafone.poms.ii.entities.Users;
import com.vodafone.poms.ii.entities.VendorPo;
import com.vodafone.poms.ii.entities.VendorPoJAspPoValue;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-02-06T13:47:08")
@StaticMetamodel(AspPo.class)
public class AspPo_ { 

    public static volatile SingularAttribute<AspPo, Double> poMargin;
    public static volatile SingularAttribute<AspPo, EricssonPoChaser> poChaser;
    public static volatile SingularAttribute<AspPo, BigDecimal> remainingInPo;
    public static volatile SingularAttribute<AspPo, Double> workDone;
    public static volatile SingularAttribute<AspPo, String> poDescription;
    public static volatile SingularAttribute<AspPo, BigDecimal> serviceValue;
    public static volatile SingularAttribute<AspPo, DomainNames> domainName;
    public static volatile SingularAttribute<AspPo, String> poNumber;
    public static volatile CollectionAttribute<AspPo, AspPoWorkDone> aspPoWorkDoneCollection;
    public static volatile SingularAttribute<AspPo, Double> factor;
    public static volatile CollectionAttribute<AspPo, AspGrn> aspGrnCollection;
    public static volatile SingularAttribute<AspPo, Users> creator;
    public static volatile SingularAttribute<AspPo, Date> poDate;
    public static volatile SingularAttribute<AspPo, BigDecimal> grnDeserved;
    public static volatile CollectionAttribute<AspPo, Activity> activityCollection;
    public static volatile SingularAttribute<AspPo, Date> sysDate;
    public static volatile CollectionAttribute<AspPo, VendorPoJAspPoValue> vendorPoJAspPoValueCollection;
    public static volatile SingularAttribute<AspPo, BigDecimal> poValueTaxes;
    public static volatile CollectionAttribute<AspPo, VendorPo> vendorPoCollection;
    public static volatile SingularAttribute<AspPo, PoStatus> poStatus;
    public static volatile SingularAttribute<AspPo, Subcontractors> asp;
    public static volatile SingularAttribute<AspPo, BigDecimal> poValue;
    public static volatile SingularAttribute<AspPo, PoTypes> poType;
    public static volatile SingularAttribute<AspPo, Double> taxes;

}