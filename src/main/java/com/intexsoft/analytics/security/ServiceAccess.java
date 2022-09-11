package com.intexsoft.analytics.security;

import java.util.UUID;

import com.intexsoft.analytics.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ServiceAccess {

    @Value("${spring.security.enabled}")
    private boolean securityEnabled;

    public boolean canAccess(final Authentication authentication) {
        return !securityEnabled || canAccessByRole(authentication);
    }

    public boolean canAccessToDepartmentData(final Authentication authentication, UUID departmentId) {
        return !securityEnabled || canAccessByRoleAndDepartment(authentication, departmentId);
    }

    public boolean canAccessByRole(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken userAuth) {
            return userAuth.getAuthorities().stream()
                    .anyMatch(authority -> Role.DEPARTMENT_MANAGER.name().equals(authority.getAuthority()));
        }
        return false;
    }

    public boolean canAccessByRoleAndDepartment(Authentication authentication, UUID departmentId) {
        if (departmentId == null) {
            return false;
        }
        if (authentication instanceof UsernamePasswordAuthenticationToken userAuth) {
            return userAuth.getCredentials().toString().equals(departmentId.toString()) && userAuth.getAuthorities()
                    .stream().anyMatch(authority -> Role.DEPARTMENT_MANAGER.name().equals(authority.getAuthority()));
        }
        return false;
    }

}
