package ru.otus.hw.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.CommentsRepositoryCustom;

@RestController
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentRepository commentRepository;

    private final CommentsRepositoryCustom commentsRepositoryCustom;


    @GetMapping("/api/comment")
    public Flux<CommentDto> getComments() {
        return commentsRepositoryCustom.findAll()
                .map(CommentDto::toDto);
    }

    @GetMapping("/api/comment/{id}")
    public Mono<ResponseEntity<CommentDto>> getCommentById(@PathVariable long id) {
        return
                commentsRepositoryCustom.findById(id)
                        .map(CommentDto::toDto)
                        .map(ResponseEntity::ok)
                        .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @GetMapping("/api/comment/book/{id}")
    public Flux<CommentDto> getCommentsByBook(@PathVariable("id") Long bookId) {
        return
                commentsRepositoryCustom.findByBookId(bookId)
                        .map(CommentDto::toDto);
    }

    @PostMapping("/api/comment")
    public Mono<CommentDto> createComment(@RequestBody Comment comment) {
        return
                commentsRepositoryCustom.insert(comment)
                        .map(CommentDto::toDto);
    }

    @PutMapping("/api/comment")
    public Mono<CommentDto> updateComment(@RequestBody Comment comment) {
        return
                commentsRepositoryCustom.update(comment)
                        .map(CommentDto::toDto);
    }

    @DeleteMapping("/api/comment/{id}")
    public Mono<ResponseEntity<Void>> deleteComment(@PathVariable Long id) {
        return
                commentRepository.findById(id)
                        .flatMap(comment -> commentRepository.delete(comment)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
