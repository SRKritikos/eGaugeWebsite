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
 */
public class UserrolesDAO implements Serializable {

    public UserrolesDAO(UserTransaction utx, EntityManager em) {
        this.utx = utx;
        this.em = em;
    }
    private UserTransaction utx = null;
    private EntityManager em = null;


    public void create(Userroles_Entity userroles_Entity) throws RollbackFailureException, Exception {
        if (userroles_Entity.getUsersList() == null) {
            userroles_Entity.setUsersList(new ArrayList<Users_Entity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            List<Users_Entity> attachedUsersList = new ArrayList<Users_Entity>();
            for (Users_Entity usersListUsers_EntityToAttach : userroles_Entity.getUsersList()) {
                usersListUsers_EntityToAttach = em.getReference(usersListUsers_EntityToAttach.getClass(), usersListUsers_EntityToAttach.getUserId());
                attachedUsersList.add(usersListUsers_EntityToAttach);
            }
            userroles_Entity.setUsersList(attachedUsersList);
            em.persist(userroles_Entity);
            for (Users_Entity usersListUsers_Entity : userroles_Entity.getUsersList()) {
                Userroles_Entity oldRoleIdOfUsersListUsers_Entity = usersListUsers_Entity.getRoleId();
                usersListUsers_Entity.setRoleId(userroles_Entity);
                usersListUsers_Entity = em.merge(usersListUsers_Entity);
                if (oldRoleIdOfUsersListUsers_Entity != null) {
                    oldRoleIdOfUsersListUsers_Entity.getUsersList().remove(usersListUsers_Entity);
                    oldRoleIdOfUsersListUsers_Entity = em.merge(oldRoleIdOfUsersListUsers_Entity);
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
        }
    }

    public void edit(Userroles_Entity userroles_Entity) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Userroles_Entity persistentUserroles_Entity = em.find(Userroles_Entity.class, userroles_Entity.getRoleId());
            List<Users_Entity> usersListOld = persistentUserroles_Entity.getUsersList();
            List<Users_Entity> usersListNew = userroles_Entity.getUsersList();
            List<Users_Entity> attachedUsersListNew = new ArrayList<Users_Entity>();
            for (Users_Entity usersListNewUsers_EntityToAttach : usersListNew) {
                usersListNewUsers_EntityToAttach = em.getReference(usersListNewUsers_EntityToAttach.getClass(), usersListNewUsers_EntityToAttach.getUserId());
                attachedUsersListNew.add(usersListNewUsers_EntityToAttach);
            }
            usersListNew = attachedUsersListNew;
            userroles_Entity.setUsersList(usersListNew);
            userroles_Entity = em.merge(userroles_Entity);
            for (Users_Entity usersListOldUsers_Entity : usersListOld) {
                if (!usersListNew.contains(usersListOldUsers_Entity)) {
                    usersListOldUsers_Entity.setRoleId(null);
                    usersListOldUsers_Entity = em.merge(usersListOldUsers_Entity);
                }
            }
            for (Users_Entity usersListNewUsers_Entity : usersListNew) {
                if (!usersListOld.contains(usersListNewUsers_Entity)) {
                    Userroles_Entity oldRoleIdOfUsersListNewUsers_Entity = usersListNewUsers_Entity.getRoleId();
                    usersListNewUsers_Entity.setRoleId(userroles_Entity);
                    usersListNewUsers_Entity = em.merge(usersListNewUsers_Entity);
                    if (oldRoleIdOfUsersListNewUsers_Entity != null && !oldRoleIdOfUsersListNewUsers_Entity.equals(userroles_Entity)) {
                        oldRoleIdOfUsersListNewUsers_Entity.getUsersList().remove(usersListNewUsers_Entity);
                        oldRoleIdOfUsersListNewUsers_Entity = em.merge(oldRoleIdOfUsersListNewUsers_Entity);
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
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Userroles_Entity userroles_Entity;
            try {
                userroles_Entity = em.getReference(Userroles_Entity.class, id);
                userroles_Entity.getRoleId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userroles_Entity with id " + id + " no longer exists.", enfe);
            }
            List<Users_Entity> usersList = userroles_Entity.getUsersList();
            for (Users_Entity usersListUsers_Entity : usersList) {
                usersListUsers_Entity.setRoleId(null);
                usersListUsers_Entity = em.merge(usersListUsers_Entity);
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
        }
    }

    public List<Userroles_Entity> findUserroles_EntityEntities() {
        return findUserroles_EntityEntities(true, -1, -1);
    }

    public List<Userroles_Entity> findUserroles_EntityEntities(int maxResults, int firstResult) {
        return findUserroles_EntityEntities(false, maxResults, firstResult);
    }

    private List<Userroles_Entity> findUserroles_EntityEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Userroles_Entity.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Userroles_Entity findUserroles_Entity(Integer id) {
        return em.find(Userroles_Entity.class, id);

    }

    public int getUserroles_EntityCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Userroles_Entity> rt = cq.from(Userroles_Entity.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();

    }
    
    public Userroles_Entity getUserRoleByName(UserRole  userRole) {
        Userroles_Entity rtVl = null;
        try {
            rtVl = em.createNamedQuery("Userroles_Entity.findByRoleName", Userroles_Entity.class)
                    .setParameter("roleName", userRole.name())
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return rtVl;
    }

}
