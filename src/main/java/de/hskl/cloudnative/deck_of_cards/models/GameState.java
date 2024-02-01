package de.hskl.cloudnative.deck_of_cards.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import de.hskl.cloudnative.security.AuthUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("gamestate")
public class GameState {
    @Id
    private String id;
    private Deck deck;
    private AuthUser user;
    private List<Card> player_hand;
    private List<Card> opponent_hand;
    private int coins;
}
