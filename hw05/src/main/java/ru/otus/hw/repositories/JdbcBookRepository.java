package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        var sql = "select b.id, b.title, b.author_id, b.genre_id, a.full_name as author_name, g.name as genre_name " +
                "from books b " +
                "left join authors a on b.author_id = a.id " +
                "left join genres g on b.genre_id = g.id " +
                "where b.id = :id";
        try {
            Book book = jdbc.queryForObject(sql, Map.of("id", id), new BookRowMapper());
            return
                    Optional.of(Objects.requireNonNull(book));
        } catch (EmptyResultDataAccessException exp) {
            return
                    Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        var sql = "select b.id, b.title, b.author_id, b.genre_id, a.full_name as author_name, g.name as genre_name " +
                "from books b " +
                "left join authors a on b.author_id = a.id " +
                "left join genres g on b.genre_id = g.id ";
        return
                jdbc.query(sql, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where id =:id", Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var sql = "insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)";
        var params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        jdbc.update(sql, params, keyHolder);
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        var sql = "update books set title=:title, author_id=:author_id, genre_id=:genre_id where id=:id";
        var params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        int updatedRows = jdbc.update(sql, params);

        if (updatedRows == 0) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(book.getId()));
        }

        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var id = rs.getLong("id");
            var title = rs.getString("title");

            var authorId = rs.getLong("author_id");
            var authorName = rs.getString("author_name");
            Author author = new Author(authorId, authorName);

            var genreId = rs.getLong("genre_id");
            var genreName = rs.getString("genre_name");
            Genre genre = new Genre(genreId, genreName);

            return
                    new Book(id, title, author, genre);
        }
    }
}