package ru.otus.hw.services.security;

import ru.otus.hw.models.security.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();
}
