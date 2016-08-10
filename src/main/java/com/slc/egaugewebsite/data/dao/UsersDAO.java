/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.data.dao;

import com.slc.egaugewebsite.data.dao.exceptions.NonexistentEntityException;
import com.slc.egaugewebsite.data.dao.exceptions.PreexistingEntityException;
import com.slc.egaugewebsite.data.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.slc.egaugewebsite.data.entities.Userroles_Entity;
import com.slc.egaugewebsite.data.entities.Device_Entity;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Steven Kritikos
 */
public class UsersDAO implements Serializable {

    public UsersDAO(UserTransaction utx, EntityManager em) {
        this.utx = utx;
        this.em = em;
    }
    private UserTransaction utx = null;
    private EntityManager em = null;


    public void create(Users_Entity users_Entity) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Userroles_Entity roleId = users_Entity.getRoleId();
            if (roleId != null) {
                roleId = em.getReference(roleId.getClass(), roleId.getRoleId());
                users_Entity.setRoleId(roleId);
            }
            Device_Entity deviceId = users_Entity.getDeviceId();
            if (deviceId != null) {
                deviceId = em.getReference(deviceId.getClass(), deviceId.getDeviceId());
                users_Entity.setDeviceId(deviceId);
            }
            em.persist(users_Entity);
            if (roleId != null) {
                roleId.getUsersList().add(users_Entity);
                roleId = em.merge(roleId);
            }
            if (deviceId != null) {
                deviceId.getUsersList().add(users_Entity);
                deviceId = em.merge(deviceId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsers_Entity(users_Entity.getUserId()) != null) {
                throw new PreexistingEntityException("Users_Entity " + users_Entity + " already exists.", ex);
            }
            throw ex;
        } 
    }

    public void edit(Users_Entity users_Entity) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Users_Entity persistentUsers_Entity = em.find(Users_Entity.class, users_Entity.getUserId());
            Userroles_Entity roleIdOld = persistentUsers_Entity.getRoleId();
            Userroles_Entity roleIdNew = users_Entity.getRoleId();
            Device_Entity deviceIdOld = persistentUsers_Entity.getDeviceId();
            Device_Entity deviceIdNew = users_Entity.getDeviceId();
            if (roleIdNew != null) {
                roleIdNew = em.getReference(roleIdNew.getClass(), roleIdNew.getRoleId());
                users_Entity.setRoleId(roleIdNew);
            }
            if (deviceIdNew != null) {
                deviceIdNew = em.getReference(deviceIdNew.getClass(), deviceIdNew.getDeviceId());
                users_Entity.setDeviceId(deviceIdNew);
            }
            users_Entity = em.merge(users_Entity);
            if (roleIdOld != null && !roleIdOld.equals(roleIdNew)) {
                roleIdOld.getUsersList().remove(users_Entity);
                roleIdOld = em.merge(roleIdOld);
            }
            if (roleIdNew != null && !roleIdNew.equals(roleIdOld)) {
                roleIdNew.getUsersList().add(users_Entity);
                roleIdNew = em.merge(roleIdNew);
            }
            if (deviceIdOld != null && !deviceIdOld.equals(deviceIdNew)) {
                deviceIdOld.getUsersList().remove(users_Entity);
                deviceIdOld = em.merge(deviceIdOld);
            }
            if (deviceIdNew != null && !deviceIdNew.equals(deviceIdOld)) {
                deviceIdNew.getUsersList().add(users_Entity);
                deviceIdNew = em.merge(deviceIdNew);
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
                String id = users_Entity.getUserId();
                if (findUsers_Entity(id) == null) {
                    throw new NonexistentEntityException("The users_Entity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Users_Entity users_Entity;
            try {
                users_Entity = em.getReference(Users_Entity.class, id);
                users_Entity.getUserId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users_Entity with id " + id + " no longer exists.", enfe);
            }
            Userroles_Entity roleId = users_Entity.getRoleId();
            if (roleId != null) {
                roleId.getUsersList().remove(users_Entity);
                roleId = em.merge(roleId);
            }
            Device_Entity deviceId = users_Entity.getDeviceId();
            if (deviceId != null) {
                deviceId.getUsersList().remove(users_Entity);
                deviceId = em.merge(deviceId);
            }
            em.remove(users_Entity);
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

    public List<Users_Entity> findUsers_EntityEntities() {
        return findUsers_EntityEntities(true, -1, -1);
    }

    public List<Users_Entity> findUsers_EntityEntities(int maxResults, int firstResult) {
        return findUsers_EntityEntities(false, maxResults, firstResult);
    }

    private List<Users_Entity> findUsers_EntityEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Users_Entity.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Users_Entity findUsers_Entity(String id) {
        return em.find(Users_Entity.class, id);

    }

    public int getUsers_EntityCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Users_Entity> rt = cq.from(Users_Entity.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
        
    }

     public Users_Entity getUserByEmail(String email) {
        Users_Entity rtVl = null;
        try {
            rtVl = em.createNamedQuery("Users_Entity.findByEmail", Users_Entity.class)
                    .setParameter("email", email) 
                    .getSingleResult();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        return rtVl;
    }
}
