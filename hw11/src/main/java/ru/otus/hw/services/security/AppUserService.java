package ru.otus.hw.services.security;

import ru.otus.hw.models.security.AppUser;

import java.util.List;

public interface AppUserService {

    List<AppUser> findAll();

    AppUser update(AppUser appUser);
}
