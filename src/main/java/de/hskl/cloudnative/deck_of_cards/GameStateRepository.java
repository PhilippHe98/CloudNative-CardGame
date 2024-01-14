package de.hskl.cloudnative.deck_of_cards;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameStateRepository extends MongoRepository<GameState, String> {
    
}
