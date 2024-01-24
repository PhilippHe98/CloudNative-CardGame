package de.hskl.cloudnative.deck_of_cards.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("deck")
public class Deck {
    private boolean success;
    private String deck_id;
    private boolean shuffled;
    private String remaining;
    private List<Pile> piles;
    private List<Card> cards;
}
