package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Genre> findAll() {
        return jdbc.query("select id, name from genres", new GnreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        try {
            var sql = "select id, name from genres where id = :id";
            Genre genre = jdbc.queryForObject(sql, Map.of("id", id), new GnreRowMapper());
            return
                    Optional.of(Objects.requireNonNull(genre));
        } catch (EmptyResultDataAccessException exp) {
            return
                    Optional.empty();
        }
    }

    private static class GnreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return
                    new Genre(id, name);
        }
    }
}