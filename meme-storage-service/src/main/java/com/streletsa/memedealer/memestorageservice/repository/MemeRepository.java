package com.streletsa.memedealer.memestorageservice.repository;

import com.streletsa.memedealer.memestorageservice.model.Meme;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemeRepository extends MongoRepository<Meme, String> {

}
