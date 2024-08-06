package ru.otus.hw.changelogs;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;


@ChangeLog(order = "001")
public class InitMongoDbChangeLog {

    private final List<Author> authors = new ArrayList<>();

    private final List<Genre> genres = new ArrayList<>();

    private final List<Book> books = new ArrayList<>();

    private final List<Comment> comments = new ArrayList<>();

    public InitMongoDbChangeLog() {
        init();
    }

    private void init() {
        fillAuthors();
        fillGenres();
        fillBooks();
        fillComments();
    }

    private void fillAuthors() {
        authors.add(new Author("1", "Author_1"));
        authors.add(new Author("2", "Author_2"));
        authors.add(new Author("3", "Author_3"));
    }

    private void fillGenres() {
        genres.add(new Genre("1", "Genre_1"));
        genres.add(new Genre("2", "Genre_2"));
        genres.add(new Genre("3", "Genre_3"));
    }

    private void fillBooks() {
        books.add(new Book("1", "BookTitle_1", authors.get(0), genres.get(0)));
        books.add(new Book("2", "BookTitle_2", authors.get(1), genres.get(1)));
        books.add(new Book("3", "BookTitle_3", authors.get(2), genres.get(2)));
    }

    private void fillComments() {
        comments.add(new Comment("1", books.get(0), "Comment_1_1"));
        comments.add(new Comment("2", books.get(1), "Comment_2_1"));
        comments.add(new Comment("3", books.get(1), "Comment_2_2"));
        comments.add(new Comment("4", books.get(2), "Comment_3_1"));
        comments.add(new Comment("5", books.get(2), "Comment_3_2"));
        comments.add(new Comment("6", books.get(2), "Comment_3_3"));
    }

    @ChangeSet(order = "000", id = "dropDb", author = "AndreevAA", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "AndreevAA", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        repository.saveAll(authors);
    }

    @ChangeSet(order = "002", id = "initGenres", author = "AndreevAA", runAlways = true)
    public void initGenres(GenreRepository repository) {
        repository.saveAll(genres);
    }

    @ChangeSet(order = "003", id = "initBooks", author = "AndreevAA", runAlways = true)
    public void initBooks(BookRepository repository) {
        repository.saveAll(books);
    }

    @ChangeSet(order = "003", id = "initComments", author = "AndreevAA", runAlways = true)
    public void initComments(CommentRepository repository) {
        repository.saveAll(comments);
    }
}
