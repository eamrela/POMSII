package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import com.vodafone.poms.ii.entities.Area;
import com.vodafone.poms.ii.entities.AspGrn;
import com.vodafone.poms.ii.entities.AspPo;
import com.vodafone.poms.ii.entities.UsersRoles;
import com.vodafone.poms.ii.entities.VendorInvoice;
import com.vodafone.poms.ii.entities.VendorMd;
import com.vodafone.poms.ii.entities.VendorPo;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-20T17:22:53")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, Short> enabled;
    public static volatile CollectionAttribute<Users, UsersRoles> usersRolesCollection;
    public static volatile SingularAttribute<Users, String> firstLastName;
    public static volatile CollectionAttribute<Users, VendorMd> vendorMdCollection;
    public static volatile SingularAttribute<Users, String> password;
    public static volatile CollectionAttribute<Users, AspGrn> aspGrnCollection;
    public static volatile SingularAttribute<Users, String> username;
    public static volatile CollectionAttribute<Users, Activity> activityCollection;
    public static volatile SingularAttribute<Users, String> email;
    public static volatile CollectionAttribute<Users, VendorPo> vendorPoCollection;
    public static volatile CollectionAttribute<Users, VendorInvoice> vendorInvoiceCollection;
    public static volatile CollectionAttribute<Users, AspPo> aspPoCollection;
    public static volatile CollectionAttribute<Users, Area> areaCollection;

}