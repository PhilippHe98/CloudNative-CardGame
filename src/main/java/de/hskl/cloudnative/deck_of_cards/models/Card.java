package de.hskl.cloudnative.deck_of_cards.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String code;
    private String image;
    private String value;
    private String suit;
    
}