package ru.otus.hw.services.author;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    @PostFilter("hasPermission(filterObject, 'READ') || hasRole('ADMIN')")
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    @PostAuthorize("hasPermission(returnObject, 'READ') || hasRole('ADMIN')")
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }
}