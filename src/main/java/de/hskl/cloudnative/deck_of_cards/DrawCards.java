package de.hskl.cloudnative.deck_of_cards;

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
    private int remaining;
}
