package com.example.employeemanagementsystemapi.type;

public enum RoleType {
    ADMIN,       // Full access
    HR_PERSONNEL, // Can perform CRUD on employees
    MANAGER       // Can view/update employees within their department
}
