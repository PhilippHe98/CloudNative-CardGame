package de.hskl.cloudnative.deck_of_cards.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DrawCards {
    private boolean success;
    private String deck_id;
    private List<Card> cards;
    private String remaining;


    public String createStringOfCards() {
        String cards = "";
        for (Card card : this.cards) {
            cards += card.getCode() + ",";
        }
        return cards.substring(0, cards.length() - 1);
    }
}
