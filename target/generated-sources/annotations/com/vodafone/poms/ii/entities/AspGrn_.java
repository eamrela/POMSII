package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.Users;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-07T18:29:59")
@StaticMetamodel(AspGrn.class)
public class AspGrn_ { 

    public static volatile SingularAttribute<AspGrn, Long> id;
    public static volatile SingularAttribute<AspGrn, BigInteger> grnDeserved;
    public static volatile SingularAttribute<AspGrn, String> grnNumber;
    public static volatile SingularAttribute<AspGrn, Date> sysDate;
    public static volatile SingularAttribute<AspGrn, BigInteger> remainingInGrn;
    public static volatile SingularAttribute<AspGrn, Date> grnDate;
    public static volatile SingularAttribute<AspGrn, Double> grnFactor;
    public static volatile SingularAttribute<AspGrn, AspPo> aspPoId;
    public static volatile SingularAttribute<AspGrn, BigInteger> grnValue;
    public static volatile SingularAttribute<AspGrn, Boolean> invoiced;
    public static volatile SingularAttribute<AspGrn, Users> creator;

}