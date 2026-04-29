package com.marketplace.userservice.ejb;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Startup
public class SessionTrackerEJB {

    private Map<String, Long> activeSessions = new ConcurrentHashMap<>();

    public String createSession(Long userId) {
        String token = UUID.randomUUID().toString();
        activeSessions.put(token, userId);
        return token;
    }

    public Long getUserIdFromToken(String token) {
        return activeSessions.get(token);
    }

    public void invalidateSession(String token) {
        activeSessions.remove(token);
    }
}