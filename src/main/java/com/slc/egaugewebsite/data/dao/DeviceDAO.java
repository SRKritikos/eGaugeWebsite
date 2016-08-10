/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.data.dao;

import com.slc.egaugewebsite.data.dao.exceptions.NonexistentEntityException;
import com.slc.egaugewebsite.data.dao.exceptions.PreexistingEntityException;
import com.slc.egaugewebsite.data.dao.exceptions.RollbackFailureException;
import com.slc.egaugewebsite.data.entities.Device_Entity;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Steven Kritikos
 */
public class DeviceDAO implements Serializable {

    public DeviceDAO(UserTransaction utx, EntityManager em) {
        this.utx = utx;
        this.em = em;
    }
    private UserTransaction utx = null;
    private EntityManager em = null;

    public void create(Device_Entity device_Entity) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (device_Entity.getUsersList() == null) {
            device_Entity.setUsersList(new ArrayList<Users_Entity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            List<Users_Entity> attachedUsersList = new ArrayList<Users_Entity>();
            for (Users_Entity usersListUsers_EntityToAttach : device_Entity.getUsersList()) {
                usersListUsers_EntityToAttach = em.getReference(usersListUsers_EntityToAttach.getClass(), usersListUsers_EntityToAttach.getUserId());
                attachedUsersList.add(usersListUsers_EntityToAttach);
            }
            device_Entity.setUsersList(attachedUsersList);
            em.persist(device_Entity);
            for (Users_Entity usersListUsers_Entity : device_Entity.getUsersList()) {
                Device_Entity oldDeviceIdOfUsersListUsers_Entity = usersListUsers_Entity.getDeviceId();
                usersListUsers_Entity.setDeviceId(device_Entity);
                usersListUsers_Entity = em.merge(usersListUsers_Entity);
                if (oldDeviceIdOfUsersListUsers_Entity != null) {
                    oldDeviceIdOfUsersListUsers_Entity.getUsersList().remove(usersListUsers_Entity);
                    oldDeviceIdOfUsersListUsers_Entity = em.merge(oldDeviceIdOfUsersListUsers_Entity);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDevice_Entity(device_Entity.getDeviceId()) != null) {
                throw new PreexistingEntityException("Device_Entity " + device_Entity + " already exists.", ex);
            }
            throw ex;
        }
    }

    public void edit(Device_Entity device_Entity) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Device_Entity persistentDevice_Entity = em.find(Device_Entity.class, device_Entity.getDeviceId());
            List<Users_Entity> usersListOld = persistentDevice_Entity.getUsersList();
            List<Users_Entity> usersListNew = device_Entity.getUsersList();
            List<Users_Entity> attachedUsersListNew = new ArrayList<Users_Entity>();
            for (Users_Entity usersListNewUsers_EntityToAttach : usersListNew) {
                usersListNewUsers_EntityToAttach = em.getReference(usersListNewUsers_EntityToAttach.getClass(), usersListNewUsers_EntityToAttach.getUserId());
                attachedUsersListNew.add(usersListNewUsers_EntityToAttach);
            }
            usersListNew = attachedUsersListNew;
            device_Entity.setUsersList(usersListNew);
            device_Entity = em.merge(device_Entity);
            for (Users_Entity usersListOldUsers_Entity : usersListOld) {
                if (!usersListNew.contains(usersListOldUsers_Entity)) {
                    usersListOldUsers_Entity.setDeviceId(null);
                    usersListOldUsers_Entity = em.merge(usersListOldUsers_Entity);
                }
            }
            for (Users_Entity usersListNewUsers_Entity : usersListNew) {
                if (!usersListOld.contains(usersListNewUsers_Entity)) {
                    Device_Entity oldDeviceIdOfUsersListNewUsers_Entity = usersListNewUsers_Entity.getDeviceId();
                    usersListNewUsers_Entity.setDeviceId(device_Entity);
                    usersListNewUsers_Entity = em.merge(usersListNewUsers_Entity);
                    if (oldDeviceIdOfUsersListNewUsers_Entity != null && !oldDeviceIdOfUsersListNewUsers_Entity.equals(device_Entity)) {
                        oldDeviceIdOfUsersListNewUsers_Entity.getUsersList().remove(usersListNewUsers_Entity);
                        oldDeviceIdOfUsersListNewUsers_Entity = em.merge(oldDeviceIdOfUsersListNewUsers_Entity);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = device_Entity.getDeviceId();
                if (findDevice_Entity(id) == null) {
                    throw new NonexistentEntityException("The device_Entity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Device_Entity device_Entity;
            try {
                device_Entity = em.getReference(Device_Entity.class, id);
                device_Entity.getDeviceId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The device_Entity with id " + id + " no longer exists.", enfe);
            }
            List<Users_Entity> usersList = device_Entity.getUsersList();
            for (Users_Entity usersListUsers_Entity : usersList) {
                usersListUsers_Entity.setDeviceId(null);
                usersListUsers_Entity = em.merge(usersListUsers_Entity);
            }
            em.remove(device_Entity);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } 
    }

    public List<Device_Entity> findDevice_EntityEntities() {
        return findDevice_EntityEntities(true, -1, -1);
    }

    public List<Device_Entity> findDevice_EntityEntities(int maxResults, int firstResult) {
        return findDevice_EntityEntities(false, maxResults, firstResult);
    }

    private List<Device_Entity> findDevice_EntityEntities(boolean all, int maxResults, int firstResult) {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Device_Entity.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList(); 
    }

    public Device_Entity findDevice_Entity(String id) {
            return em.find(Device_Entity.class, id);
    }

    public int getDevice_EntityCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Device_Entity> rt = cq.from(Device_Entity.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();

    }
    
    public Device_Entity getDeviceByName(String deviceName) {
        Device_Entity rtVl = null;
        try {
            rtVl = em.createNamedQuery("Device_Entity.findByDeviceName", Device_Entity.class)
                    .setParameter("deviceName", deviceName)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        
        return rtVl;
    }

}
