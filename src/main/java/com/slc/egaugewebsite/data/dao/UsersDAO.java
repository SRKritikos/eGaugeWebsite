/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slc.egaugewebsite.data.dao;


import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.slc.egaugewebsite.data.entities.Userroles_Entity;
import com.slc.egaugewebsite.data.entities.Users_Entity;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Steven Kritikos
 */
public class UsersDAO implements Serializable {

    public UsersDAO(EntityManagerFactory emf) {
        this.emf = emf ;
    }  
    
    private EntityManagerFactory emf = null;
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
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
    
    
     public boolean insertUser(String email, byte[] password, byte[] salt, String preferredCampus, Userroles_Entity roleId) {
        boolean rtVl = false;
        EntityManager em;
        try {
            em = this.getEntityManager();
            em.getTransaction().begin();
            Users_Entity entity = new Users_Entity();
            entity.setUserId(UUID.randomUUID().toString());
            entity.setEmail(email);
            entity.setPassword(password);
            entity.setPasswordSalt(salt);
            entity.setPreferredCampus(preferredCampus);
            entity.setRoleId(roleId);
            
            em.persist(entity);
            em.getTransaction().commit();
            rtVl = true;
        } catch (ConstraintViolationException  e ) {
            System.out.println(e.toString());
        }
        return rtVl;
    }
    
    public Users_Entity getUserByEmail(String email) {
        Users_Entity rtVl = null;
        EntityManager em;
        try {
            em = this.getEntityManager();
            rtVl = em.createNamedQuery("Users_Entity.findByEmail", Users_Entity.class)
                    .setParameter("email", email) 
                    .getSingleResult();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        return rtVl;
    }

}
