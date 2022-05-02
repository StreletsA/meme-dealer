package com.streletsa.memedealer.memestorageservice.repository;

import com.streletsa.memedealer.memestorageservice.model.Meme;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemeRepository extends MongoRepository<Meme, String> {
    List<Meme> findByApproved(Boolean approved);
}
