package ru.otus.hw.services.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    @Override
    @PostFilter("hasPermission(filterObject, 'READ') || hasRole('ADMIN')")
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    @PostAuthorize("hasPermission(returnObject, 'READ') || hasRole('ADMIN')")
    public Optional<Genre> findById(long id) {
        return genreRepository.findById(id);
    }
}
