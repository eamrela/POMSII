package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.UserPrivilege;
import com.vodafone.poms.ii.entities.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-05T22:11:53")
@StaticMetamodel(UserRole.class)
public class UserRole_ { 

    public static volatile SingularAttribute<UserRole, Long> id;
    public static volatile SingularAttribute<UserRole, String> roleDescription;
    public static volatile CollectionAttribute<UserRole, Users> usersCollection;
    public static volatile CollectionAttribute<UserRole, UserPrivilege> userPrivilegeCollection;
    public static volatile SingularAttribute<UserRole, String> roleName;

}