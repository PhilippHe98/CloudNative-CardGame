package de.hskl.cloudnative.deck_of_cards.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pile {
    private String deck_id;
    private String remaining;
    private String pile_name;
}
