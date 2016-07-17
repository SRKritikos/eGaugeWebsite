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

    public UsersDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users_Entity users_Entity) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Userroles_Entity roleId = users_Entity.getRoleId();
            if (roleId != null) {
                roleId = em.getReference(roleId.getClass(), roleId.getRoleId());
                users_Entity.setRoleId(roleId);
            }
            em.persist(users_Entity);
            if (roleId != null) {
                roleId.getUsersEntityList().add(users_Entity);
                roleId = em.merge(roleId);
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
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users_Entity users_Entity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users_Entity persistentUsers_Entity = em.find(Users_Entity.class, users_Entity.getUserId());
            Userroles_Entity roleIdOld = persistentUsers_Entity.getRoleId();
            Userroles_Entity roleIdNew = users_Entity.getRoleId();
            if (roleIdNew != null) {
                roleIdNew = em.getReference(roleIdNew.getClass(), roleIdNew.getRoleId());
                users_Entity.setRoleId(roleIdNew);
            }
            users_Entity = em.merge(users_Entity);
            if (roleIdOld != null && !roleIdOld.equals(roleIdNew)) {
                roleIdOld.getUsersEntityList().remove(users_Entity);
                roleIdOld = em.merge(roleIdOld);
            }
            if (roleIdNew != null && !roleIdNew.equals(roleIdOld)) {
                roleIdNew.getUsersEntityList().add(users_Entity);
                roleIdNew = em.merge(roleIdNew);
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
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users_Entity users_Entity;
            try {
                users_Entity = em.getReference(Users_Entity.class, id);
                users_Entity.getUserId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users_Entity with id " + id + " no longer exists.", enfe);
            }
            Userroles_Entity roleId = users_Entity.getRoleId();
            if (roleId != null) {
                roleId.getUsersEntityList().remove(users_Entity);
                roleId = em.merge(roleId);
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
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users_Entity> findUsers_EntityEntities() {
        return findUsers_EntityEntities(true, -1, -1);
    }

    public List<Users_Entity> findUsers_EntityEntities(int maxResults, int firstResult) {
        return findUsers_EntityEntities(false, maxResults, firstResult);
    }

    private List<Users_Entity> findUsers_EntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users_Entity.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Users_Entity findUsers_Entity(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users_Entity.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsers_EntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users_Entity> rt = cq.from(Users_Entity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
