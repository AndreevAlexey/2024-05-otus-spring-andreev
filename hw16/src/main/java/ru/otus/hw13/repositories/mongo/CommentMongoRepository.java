package ru.otus.hw13.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw13.models.mongo.CommentMongo;

public interface CommentMongoRepository extends MongoRepository<CommentMongo, String> {

}
