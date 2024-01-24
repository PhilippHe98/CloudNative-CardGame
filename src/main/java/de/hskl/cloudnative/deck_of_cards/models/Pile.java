package de.hskl.cloudnative.deck_of_cards.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pile {
    private String remaining;
    private String name;
    private List<Card> cards;
}
