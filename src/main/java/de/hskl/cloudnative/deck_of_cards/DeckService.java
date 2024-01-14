package de.hskl.cloudnative.deck_of_cards;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeckService {

    @Value("${deck-of-cards-api.url}")
    private String apiUrl;

    private RestTemplate restTemplate;

    public DeckService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Deck createNewDeck() {
        String url = apiUrl + "/new/shuffle/?deck_count=1";
        return restTemplate.getForObject(url, Deck.class);
    }

    public DrawCards drawCardsFromDeck(String deckId, int numberOfCards) {
        String url = apiUrl + "/" + deckId + "/draw/?count=" + numberOfCards;
        return restTemplate.getForObject(url, DrawCards.class);
    }
}