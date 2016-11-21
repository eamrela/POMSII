package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.UsersRoles;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-11-21T01:22:19")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, Short> enabled;
    public static volatile SingularAttribute<Users, String> username;
    public static volatile CollectionAttribute<Users, UsersRoles> usersRolesCollection;
    public static volatile SingularAttribute<Users, String> email;
    public static volatile SingularAttribute<Users, String> firstLastName;
    public static volatile SingularAttribute<Users, String> password;

}