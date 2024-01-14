package de.hskl.cloudnative.deck_of_cards;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GameStateService {

    private final GameStateRepository gameStateRepository;

    public GameState save(GameState gameState) {
        return gameStateRepository.save(gameState);
    }

    public GameState find(String id) {
        return gameStateRepository.findById(id).orElseThrow(() -> new RuntimeException("Gamestate not found"));
    }

    public List<GameState> findAllGames() {
        return gameStateRepository.findAll();
    }
}
