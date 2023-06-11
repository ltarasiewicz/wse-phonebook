package com.example.javaee.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class ServiceFactory {
    private static ServiceFactory instance;
    private final EntityManagerFactory entityManagerFactory;

    private ServiceFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public static ServiceFactory getInstance() {
        if (instance == null) {
            throw new RuntimeException("ServiceFactory not initialized");
        }
        return instance;
    }

    public static void initialize(EntityManagerFactory entityManagerFactory) {
        if (instance != null) {
            throw new RuntimeException("ServiceFactory already initialized");
        }
        instance = new ServiceFactory(entityManagerFactory);
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
