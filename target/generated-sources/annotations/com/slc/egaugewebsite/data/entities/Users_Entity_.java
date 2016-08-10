package com.slc.egaugewebsite.data.entities;

import com.slc.egaugewebsite.data.entities.Device_Entity;
import com.slc.egaugewebsite.data.entities.Userroles_Entity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-09T18:57:12")
@StaticMetamodel(Users_Entity.class)
public class Users_Entity_ { 

    public static volatile SingularAttribute<Users_Entity, String> lastName;
    public static volatile SingularAttribute<Users_Entity, Integer> extendIimeTries;
    public static volatile SingularAttribute<Users_Entity, Date> availaleStartTime;
    public static volatile SingularAttribute<Users_Entity, Userroles_Entity> roleId;
    public static volatile SingularAttribute<Users_Entity, String> preferredCampus;
    public static volatile SingularAttribute<Users_Entity, Boolean> isActive;
    public static volatile SingularAttribute<Users_Entity, String> userId;
    public static volatile SingularAttribute<Users_Entity, Device_Entity> deviceId;
    public static volatile SingularAttribute<Users_Entity, String> firstName;
    public static volatile SingularAttribute<Users_Entity, byte[]> password;
    public static volatile SingularAttribute<Users_Entity, Date> timeEnteredQueue;
    public static volatile SingularAttribute<Users_Entity, Date> availableEndTime;
    public static volatile SingularAttribute<Users_Entity, byte[]> passwordSalt;
    public static volatile SingularAttribute<Users_Entity, String> email;

}