package com.slc.egaugewebsite.data.entities;

import com.slc.egaugewebsite.data.entities.Users_Entity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-09T18:57:12")
@StaticMetamodel(Device_Entity.class)
public class Device_Entity_ { 

    public static volatile ListAttribute<Device_Entity, Users_Entity> usersList;
    public static volatile SingularAttribute<Device_Entity, String> deviceId;
    public static volatile SingularAttribute<Device_Entity, String> deviceName;

}