package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.UserRole;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-18T21:09:03")
@StaticMetamodel(UserPrivilege.class)
public class UserPrivilege_ { 

    public static volatile SingularAttribute<UserPrivilege, Long> id;
    public static volatile CollectionAttribute<UserPrivilege, UserRole> userRoleCollection;
    public static volatile SingularAttribute<UserPrivilege, String> privilegeDescription;
    public static volatile SingularAttribute<UserPrivilege, String> privilegeName;

}