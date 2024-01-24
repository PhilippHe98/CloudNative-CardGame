package de.hskl.cloudnative.deck_of_cards.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import de.hskl.cloudnative.deck_of_cards.models.Deck;
import de.hskl.cloudnative.deck_of_cards.models.DrawCards;
import de.hskl.cloudnative.deck_of_cards.models.Pile;

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

    public Pile createPile(String deckId, String pileName, String cards) {
        String url = apiUrl + "/" + deckId + "/pile/" + pileName + "/add/?cards=" + cards;
        return restTemplate.getForObject(url, Pile.class);
    }

    public Deck createPartialDeck(String cards) {
        String url = apiUrl + "/new/shuffle/?cards=" + cards;
        return restTemplate.getForObject(url, Deck.class);
    }

    public Deck returnCardsToDeck(String deckId, String cards) {
        String url = apiUrl + "/" + deckId + "/return/?cards=" + cards;
        return restTemplate.getForObject(url, Deck.class);
    }
}