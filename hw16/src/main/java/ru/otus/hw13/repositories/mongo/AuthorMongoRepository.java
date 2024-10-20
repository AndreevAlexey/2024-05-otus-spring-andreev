package ru.otus.hw13.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw13.models.mongo.AuthorMongo;


public interface AuthorMongoRepository extends MongoRepository<AuthorMongo, String> {
}
