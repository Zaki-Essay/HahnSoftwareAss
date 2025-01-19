package com.example.employeemanagementsystemapi.config;

import org.springframework.security.access.AccessDeniedException;

public class SecurityUtils {
    public static final String ROLE_HR = "HR";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";

    public static void checkPermission(String... allowedRoles) {
        if (!SecurityContext.hasAnyRole(allowedRoles)) {
            throw new AccessDeniedException("You don't have permission to perform this operation");
        }
    }

    public static void checkDepartmentAccess(String departmentToAccess) {
        if (SecurityContext.hasRole(ROLE_HR) || SecurityContext.hasRole(ROLE_ADMIN)) {
            return; // HR and Admin can access all departments
        }

        if (SecurityContext.hasRole(ROLE_MANAGER)) {
            String userDepartment = SecurityContext.getCurrentDepartment();
            if (!departmentToAccess.equals(userDepartment)) {
                throw new AccessDeniedException("You can only access employees in your department");
            }
        }
    }
}
