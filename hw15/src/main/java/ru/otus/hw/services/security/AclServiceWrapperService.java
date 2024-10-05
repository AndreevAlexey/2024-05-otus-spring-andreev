package ru.otus.hw.services.security;

import org.springframework.security.acls.model.Permission;

public interface AclServiceWrapperService {

    void createPermission(Object object, Permission permission);

    void createFullPermission(Object object);

}
