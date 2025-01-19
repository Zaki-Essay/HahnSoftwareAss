package com.example.employeemanagementsystemapi.config;

import com.example.employeemanagementsystemapi.dto.UserSession;
import lombok.Data;

@Data
public class SecurityContext {
    private static final ThreadLocal<UserSession> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(UserSession session) {
        currentUser.set(session);
    }

    public static UserSession getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }

    public static boolean hasRole(String role) {
        UserSession session = getCurrentUser();
        return session != null && session.getRoles().contains(role);
    }

    public static boolean hasAnyRole(String... roles) {
        UserSession session = getCurrentUser();
        if (session == null) return false;

        for (String role : roles) {
            if (session.getRoles().contains(role)) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrentDepartment() {
        UserSession session = getCurrentUser();
        return session != null ? session.getDepartment() : null;
    }
}
