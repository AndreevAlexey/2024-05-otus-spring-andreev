package ru.otus.hw13.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw13.models.mongo.BookMongo;


public interface BookMongoRepository extends MongoRepository<BookMongo, String> {

}
