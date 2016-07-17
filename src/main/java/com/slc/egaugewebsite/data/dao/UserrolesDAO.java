/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.data.dao;

import com.slc.egaugewebsite.data.dao.exceptions.NonexistentEntityException;
import com.slc.egaugewebsite.data.dao.exceptions.RollbackFailureException;
import com.slc.egaugewebsite.data.entities.Userroles_Entity;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import com.slc.egaugewebsite.utils.UserRole;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Steven Kritikos
 * email: stevenrktitikos@outlook.com
 */
public class UserrolesDAO implements Serializable {

    public UserrolesDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Userroles_Entity userroles_Entity) throws RollbackFailureException, Exception {
        if (userroles_Entity.getUsersEntityList() == null) {
            userroles_Entity.setUsersEntityList(new ArrayList<Users_Entity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Users_Entity> attachedUsersEntityList = new ArrayList<Users_Entity>();
            for (Users_Entity usersEntityListUsers_EntityToAttach : userroles_Entity.getUsersEntityList()) {
                usersEntityListUsers_EntityToAttach = em.getReference(usersEntityListUsers_EntityToAttach.getClass(), usersEntityListUsers_EntityToAttach.getUserId());
                attachedUsersEntityList.add(usersEntityListUsers_EntityToAttach);
            }
            userroles_Entity.setUsersEntityList(attachedUsersEntityList);
            em.persist(userroles_Entity);
            for (Users_Entity usersEntityListUsers_Entity : userroles_Entity.getUsersEntityList()) {
                Userroles_Entity oldRoleIdOfUsersEntityListUsers_Entity = usersEntityListUsers_Entity.getRoleId();
                usersEntityListUsers_Entity.setRoleId(userroles_Entity);
                usersEntityListUsers_Entity = em.merge(usersEntityListUsers_Entity);
                if (oldRoleIdOfUsersEntityListUsers_Entity != null) {
                    oldRoleIdOfUsersEntityListUsers_Entity.getUsersEntityList().remove(usersEntityListUsers_Entity);
                    oldRoleIdOfUsersEntityListUsers_Entity = em.merge(oldRoleIdOfUsersEntityListUsers_Entity);
                }
            }
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

    public void edit(Userroles_Entity userroles_Entity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Userroles_Entity persistentUserroles_Entity = em.find(Userroles_Entity.class, userroles_Entity.getRoleId());
            List<Users_Entity> usersEntityListOld = persistentUserroles_Entity.getUsersEntityList();
            List<Users_Entity> usersEntityListNew = userroles_Entity.getUsersEntityList();
            List<Users_Entity> attachedUsersEntityListNew = new ArrayList<Users_Entity>();
            for (Users_Entity usersEntityListNewUsers_EntityToAttach : usersEntityListNew) {
                usersEntityListNewUsers_EntityToAttach = em.getReference(usersEntityListNewUsers_EntityToAttach.getClass(), usersEntityListNewUsers_EntityToAttach.getUserId());
                attachedUsersEntityListNew.add(usersEntityListNewUsers_EntityToAttach);
            }
            usersEntityListNew = attachedUsersEntityListNew;
            userroles_Entity.setUsersEntityList(usersEntityListNew);
            userroles_Entity = em.merge(userroles_Entity);
            for (Users_Entity usersEntityListOldUsers_Entity : usersEntityListOld) {
                if (!usersEntityListNew.contains(usersEntityListOldUsers_Entity)) {
                    usersEntityListOldUsers_Entity.setRoleId(null);
                    usersEntityListOldUsers_Entity = em.merge(usersEntityListOldUsers_Entity);
                }
            }
            for (Users_Entity usersEntityListNewUsers_Entity : usersEntityListNew) {
                if (!usersEntityListOld.contains(usersEntityListNewUsers_Entity)) {
                    Userroles_Entity oldRoleIdOfUsersEntityListNewUsers_Entity = usersEntityListNewUsers_Entity.getRoleId();
                    usersEntityListNewUsers_Entity.setRoleId(userroles_Entity);
                    usersEntityListNewUsers_Entity = em.merge(usersEntityListNewUsers_Entity);
                    if (oldRoleIdOfUsersEntityListNewUsers_Entity != null && !oldRoleIdOfUsersEntityListNewUsers_Entity.equals(userroles_Entity)) {
                        oldRoleIdOfUsersEntityListNewUsers_Entity.getUsersEntityList().remove(usersEntityListNewUsers_Entity);
                        oldRoleIdOfUsersEntityListNewUsers_Entity = em.merge(oldRoleIdOfUsersEntityListNewUsers_Entity);
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
                Integer id = userroles_Entity.getRoleId();
                if (findUserroles_Entity(id) == null) {
                    throw new NonexistentEntityException("The userroles_Entity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Userroles_Entity userroles_Entity;
            try {
                userroles_Entity = em.getReference(Userroles_Entity.class, id);
                userroles_Entity.getRoleId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userroles_Entity with id " + id + " no longer exists.", enfe);
            }
            List<Users_Entity> usersEntityList = userroles_Entity.getUsersEntityList();
            for (Users_Entity usersEntityListUsers_Entity : usersEntityList) {
                usersEntityListUsers_Entity.setRoleId(null);
                usersEntityListUsers_Entity = em.merge(usersEntityListUsers_Entity);
            }
            em.remove(userroles_Entity);
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

    public List<Userroles_Entity> findUserroles_EntityEntities() {
        return findUserroles_EntityEntities(true, -1, -1);
    }

    public List<Userroles_Entity> findUserroles_EntityEntities(int maxResults, int firstResult) {
        return findUserroles_EntityEntities(false, maxResults, firstResult);
    }

    private List<Userroles_Entity> findUserroles_EntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Userroles_Entity.class));
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

    public Userroles_Entity findUserroles_Entity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Userroles_Entity.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserroles_EntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Userroles_Entity> rt = cq.from(Userroles_Entity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Userroles_Entity getUserRoleByName(UserRole  userRole) {
        Userroles_Entity rtVl = null;
        try {
            EntityManager em = getEntityManager();
            rtVl = em.createNamedQuery("UserRole_Entity.findByRoleName", Userroles_Entity.class)
                    .setParameter("roleName", userRole.name())
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return rtVl;
    }

}
