package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.AspGrn;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.UserRole;
import com.vodafone.poms.ii.entities.VendorMd;
import com.vodafone.poms.ii.entities.VendorPo;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-18T21:09:03")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, Long> id;
    public static volatile SingularAttribute<Users, String> lastName;
    public static volatile SingularAttribute<Users, Boolean> enabled;
    public static volatile SingularAttribute<Users, String> pwd;
    public static volatile CollectionAttribute<Users, Activity> activityCollection;
    public static volatile SingularAttribute<Users, String> manager;
    public static volatile SingularAttribute<Users, String> phoneNumber;
    public static volatile SingularAttribute<Users, String> email;
    public static volatile SingularAttribute<Users, Boolean> tokenExpired;
    public static volatile CollectionAttribute<Users, UserRole> userRoleCollection;
    public static volatile CollectionAttribute<Users, VendorMd> vendorMdCollection;
    public static volatile CollectionAttribute<Users, VendorPo> vendorPoCollection;
    public static volatile SingularAttribute<Users, String> firstName;
    public static volatile CollectionAttribute<Users, AspGrn> aspGrnCollection;
    public static volatile CollectionAttribute<Users, AspPo> aspPoCollection;

}