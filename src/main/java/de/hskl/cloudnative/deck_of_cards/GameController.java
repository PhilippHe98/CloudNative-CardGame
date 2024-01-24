package de.hskl.cloudnative.deck_of_cards;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.hskl.cloudnative.deck_of_cards.models.Card;
import de.hskl.cloudnative.deck_of_cards.models.Deck;
import de.hskl.cloudnative.deck_of_cards.models.DrawCards;
import de.hskl.cloudnative.deck_of_cards.models.GameState;
import de.hskl.cloudnative.deck_of_cards.service.DeckService;
import de.hskl.cloudnative.deck_of_cards.service.GameStateService;
import de.hskl.cloudnative.security.AuthUser;
import de.hskl.cloudnative.security.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RequestMapping("/game")
@Controller
public class GameController {

    private DeckService deckService;
    private GameStateService gameStateService;
    private UserService userService;

    @GetMapping("/start")
    public String loadStartPage(Model model) {
        List<GameState> allGames = gameStateService.findAllGames();
        model.addAttribute("games", allGames);
        return "start";
    }

    @PostMapping("/start/create-game")
    public String startNewGame(Model model) {
        // Deck currentDeck = deckService.createNewDeck();
        Deck currentDeck = deckService.createPartialDeck(
                "9H,9H,9H,9H,9H,0S,0S,0S,0S,0S,JD,JD,JD,JD,JD,QC,QC,QC,QC,QC,KH,KH,KH,KH,KH,AS,AS,AS,AS,AS");
        AuthUser currentUser = userService.getCurrentUser();
        GameState gameState = GameState.builder()
                .deck(currentDeck)
                .user(currentUser)
                .build();
        gameStateService.save(gameState);
        model.addAttribute("deck", currentDeck);
        model.addAttribute("gamestate", gameState);
        return "redirect:/game/start";
    }

    @PostMapping("/start/delete-game")
    public String deleteGame(@RequestParam String gameId) {

        GameState game = gameStateService.find(gameId);
        AuthUser currentUser = userService.getCurrentUser();

        // Prüft autorisierung
        if (game.getUser().getEmail().equals(currentUser.getUsername())
                || currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            gameStateService.delete(gameId);
        }

        return "redirect:/game/start";
    }

    @GetMapping("/play")
    public String redirectToGame(@RequestParam("gameId") String gameId, Model model) {
        GameState game = gameStateService.find(gameId);
        AuthUser currentUser = userService.getCurrentUser();

        // Prüft autorisierung
        if (!game.getUser().getEmail().equals(currentUser.getUsername())) {
            return "redirect:/game/start";
        }

        // model.addAttribute("deck", game.getDeck());
        // model.addAttribute("gamestate", game);

        Deck currentDeck = game.getDeck();

        // Draw 5 cards from the deck for the player
        DrawCards cardsDrawn = deckService.drawCardsFromDeck(currentDeck.getDeck_id(), 5);
        List<Card> player_hand = cardsDrawn.getCards();
        game.setPlayer_hand(player_hand);

        // Draw 5 cards from the deck for the opponent
        cardsDrawn = deckService.drawCardsFromDeck(currentDeck.getDeck_id(), 5);
        List<Card> opponent_hand = cardsDrawn.getCards();
        game.setOpponent_hand(opponent_hand);

        // Update the game state with the drawn cards
        currentDeck.setRemaining(cardsDrawn.getRemaining());
        gameStateService.save(game);

        // Sort the cards and set the hand position id
        sortCards(player_hand);
        for (int i = 0; i < player_hand.size(); i++) {
            player_hand.get(i).setHandPositionId(i);
        }

        model.addAttribute("deck", currentDeck);
        model.addAttribute("gamestate", game);
        model.addAttribute("player_hand", player_hand);
        model.addAttribute("opponent_hand", opponent_hand);

        return "play";
    }

    @GetMapping("/play/drawCards")
    public String drawCards(@RequestParam("gameId") String gameId, @RequestParam("count") int count, Model model) {
        // Get the deck from game
        GameState gameState = gameStateService.find(gameId);
        Deck currentDeck = gameState.getDeck();

        // Draw Cards from current deck
        DrawCards cardsDrawn = deckService.drawCardsFromDeck(currentDeck.getDeck_id(), count);
        List<Card> cards = cardsDrawn.getCards();

        // Gives Every Card a id for the hand, card with smallest id is the first card
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setHandPositionId(i);
        }

        // Update the game state with the drawn cards
        currentDeck.setRemaining(cardsDrawn.getRemaining());
        gameStateService.save(gameState);

        model.addAttribute("gamestate", gameState);
        model.addAttribute("deck", currentDeck);
        model.addAttribute("cards", cards);
        return "play";
    }

    @GetMapping("/play/exchangeCards")
    public String exchangeCards(@RequestParam("gameId") String gameId, @RequestParam("cardCodes") String cards,
            Model model) {
        GameState gameState = gameStateService.find(gameId);
        Deck currentDeck = gameState.getDeck();
        Deck deckInfo = deckService.returnCardsToDeck(currentDeck.getDeck_id(), cards);

        currentDeck.setRemaining(deckInfo.getRemaining());
        gameStateService.save(gameState);

        int numberOfCards = cards.split(",").length;

        return "redirect:/game/play/drawCards?gameId=" + gameId + "&count=" + numberOfCards;
    }

    @GetMapping("/play/backToHomepage")
    public String backToHomepage() {
        return "redirect:/game/start";
    }

    private void sortCards(List<Card> cards) {
        cards.sort((a, b) -> {
            String valueA = a.getValue();
            String valueB = b.getValue();
            int intA = 0;
            int intB = 0;

            if (valueA.equals("ACE")) {
                intA = 14;
            } else if (valueA.equals("KING")) {
                intA = 13;
            } else if (valueA.equals("QUEEN")) {
                intA = 12;
            } else if (valueA.equals("JACK")) {
                intA = 11;
            } else if (valueA.equals("0")) {
                intA = 10;
            } else {
                intA = Integer.parseInt(valueA);
            }

            if (valueB.equals("ACE")) {
                intB = 14;
            } else if (valueB.equals("KING")) {
                intB = 13;
            } else if (valueB.equals("QUEEN")) {
                intB = 12;
            } else if (valueB.equals("JACK")) {
                intB = 11;
            } else if (valueA.equals("0")) {
                intA = 10;
            } else {
                intB = Integer.parseInt(valueB);
            }

            return intA - intB;
        });
    }

    /*
     * Ablauf Spiel:
     * Spieler und Gegner bekommen 5 Karten
     * Spieler kann 0-5 Karten auswechseln oder Karten behalten
     * danach werden die Karten verglichen
     * derjenige der die besser Karten hat gewinnt
     */

    /*
     * To create player hand:
     * Have a deck of cards
     * Draw 5 cards from the deck
     * Create a pile player_hand of these cards
     * Send the pile to the player (frontend)
     */

    /*
     * To exchange player hand:
     * Send the pile player_hand to the backend with selected cards to exchange
     * return the selected cards to the deck
     * draw new cards from the deck
     * add the new cards to the pile player_hand
     * send the pile to the player (frontend)
     */

}