package com.slc.egaugewebsite.data.entities;

import com.slc.egaugewebsite.data.entities.Userroles_Entity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-07-19T06:13:13")
@StaticMetamodel(Users_Entity.class)
public class Users_Entity_ { 

    public static volatile SingularAttribute<Users_Entity, byte[]> password;
    public static volatile SingularAttribute<Users_Entity, Userroles_Entity> roleId;
    public static volatile SingularAttribute<Users_Entity, String> preferredCampus;
    public static volatile SingularAttribute<Users_Entity, String> userId;
    public static volatile SingularAttribute<Users_Entity, byte[]> passwordSalt;
    public static volatile SingularAttribute<Users_Entity, String> email;

}