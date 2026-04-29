package com.marketplace.userservice.ejb;

import com.marketplace.userservice.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UserServiceEJB {

    @PersistenceContext(unitName = "usersPU")
    private EntityManager em;

    public User register(User user) {
        em.persist(user);
        return user;
    }

    public User login(String username, String password) {
        return em.createQuery(
                        "SELECT u FROM User u WHERE u.username = :u AND u.password = :p", User.class)
                .setParameter("u", username)
                .setParameter("p", password)
                .getSingleResult();
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public User addFunds(Long customerId, double amount) {
        User u = em.find(User.class, customerId);
        u.setWalletBalance(u.getWalletBalance() + amount);
        return em.merge(u);
    }

    public boolean deductFunds(Long customerId, double amount) {
        User u = em.find(User.class, customerId);
        if (u.getWalletBalance() < amount) return false;
        u.setWalletBalance(u.getWalletBalance() - amount);
        em.merge(u);
        return true;
    }

    public void refundFunds(Long customerId, double amount) {
        User u = em.find(User.class, customerId);
        u.setWalletBalance(u.getWalletBalance() + amount);
        em.merge(u);
    }
}