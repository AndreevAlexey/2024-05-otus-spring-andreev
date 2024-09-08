package ru.otus.hw.repositories.security;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.security.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
