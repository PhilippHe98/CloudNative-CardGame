package de.hskl.cloudnative.deck_of_cards;

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
}
