package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.book.BookService;


@Component
@RequiredArgsConstructor
public class BookHeathIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        int bookCount = bookService.findAll().size();
        if (bookCount == 0) {
            return
                    Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "No books in library!")
                    .build();
        }
        return
                Health.up().build();
    }
}
