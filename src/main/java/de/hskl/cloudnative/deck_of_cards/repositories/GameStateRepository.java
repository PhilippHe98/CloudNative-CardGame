package de.hskl.cloudnative.deck_of_cards.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.hskl.cloudnative.deck_of_cards.models.GameState;

public interface GameStateRepository extends MongoRepository<GameState, String> {
    
}
