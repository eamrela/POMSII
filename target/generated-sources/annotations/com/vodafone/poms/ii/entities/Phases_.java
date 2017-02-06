package com.vodafone.poms.ii.entities;

import com.vodafone.poms.ii.entities.Activity;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-02-06T13:47:08")
@StaticMetamodel(Phases.class)
public class Phases_ { 

    public static volatile CollectionAttribute<Phases, Activity> activityCollection;
    public static volatile SingularAttribute<Phases, String> description;
    public static volatile SingularAttribute<Phases, String> phaseName;

}