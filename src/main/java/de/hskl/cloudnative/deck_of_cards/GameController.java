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
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RequestMapping("/game")
@Controller
public class GameController {

    private DeckService deckService;
    private GameStateService gameStateService;
    private UserService userService;

    @Operation(summary = "Loads the start page with all games from the database")
    @GetMapping("/start")
    public String loadStartPage(Model model, @RequestParam(required = false) String errorMessage) {
        List<GameState> allGames = gameStateService.findAllGames();
        model.addAttribute("games", allGames);
        if (errorMessage != null)
            model.addAttribute("errorMessage", errorMessage);

        return "start";
    }

    @Operation(summary = "Creates a new game but user stays on the start page")
    @PostMapping("/start/create-game")
    public String startNewGame(Model model) {

        Deck currentDeck = deckService.createPartialDeck(
                "9H,9H,9H,9H,9H,0S,0S,0S,0S,0S,JD,JD,JD,JD,JD,QC,QC,QC,QC,QC,KH,KH,KH,KH,KH,AS,AS,AS,AS,AS");
        AuthUser currentUser = userService.getCurrentUser();
        GameState gameState = GameState.builder()
                .deck(currentDeck)
                .user(currentUser)
                .coins(5)
                .build();
        gameStateService.save(gameState);
        model.addAttribute("deck", currentDeck);
        model.addAttribute("gamestate", gameState);
        return "redirect:/game/start";
    }

    @Operation(summary = "Deletes a game from the database")
    @PostMapping("/start/delete-game")
    public String deleteGame(@RequestParam String gameId) {

        GameState game = gameStateService.find(gameId);
        AuthUser currentUser = userService.getCurrentUser();
        // Is user authorized to delete the game?
        if (game.getUser().getEmail().equals(currentUser.getUsername()) ||
                currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    
            gameStateService.delete(gameId);
            return "redirect:/game/start";
        }

        return "redirect:/game/start?errorMessage=You are not authorized to delete this game!";
    }

    @Operation(summary = "Redirects from the start page to the play page and draws 5 cards from the deck for the player and the opponent")
    @GetMapping("/play")
    public String redirectToGame(@RequestParam("gameId") String gameId, Model model) {

        GameState game = gameStateService.find(gameId);
        AuthUser currentUser = userService.getCurrentUser();

        // Is user authorized to play the game?
        if (!game.getUser().getEmail().equals(currentUser.getUsername())) {
            return "redirect:/game/start?errorMessage=You are not authorized to play this game!";
        }

        Deck currentDeck = game.getDeck();
        deckService.shuffleDeck(currentDeck.getDeck_id()); // Shuffle the deck

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
        sortCards(opponent_hand);

        model.addAttribute("deck", currentDeck);
        model.addAttribute("gamestate", game);
        model.addAttribute("player_hand", player_hand);
        model.addAttribute("opponent_hand", opponent_hand);
        model.addAttribute("exchanged", false);

        return "play";
    }

    @Operation(summary = "After the player has selected the cards to exchange, this endpoint will exchange the cards and draw new cards from the deck and reload the play page")
    @GetMapping("/play/exchangeCards")
    public String exchangeCards(@RequestParam("gameId") String gameId, @RequestParam("cardCodes") String cards,
            Model model) {

        GameState gameState = gameStateService.find(gameId);
        Deck currentDeck = gameState.getDeck();
        Deck deckInfo = deckService.returnCardsToDeck(currentDeck.getDeck_id(), cards);

        if (!cards.equals("")) {
            // Split the cards to exchange
            String[] cardsToExchange = cards.split(",");
            int numberOfCardsToExchange = cardsToExchange.length;

            // Draw new cards from deck
            DrawCards drawCards = deckService.drawCardsFromDeck(deckInfo.getDeck_id(), numberOfCardsToExchange);
            List<Card> newCards = drawCards.getCards();

            List<Card> player_hand = gameState.getPlayer_hand();

            // Remove Cards to exchange from player hand
            for (int i = 0; i < numberOfCardsToExchange; i++) {
                for (int j = 0; j < player_hand.size(); j++) {
                    if (cardsToExchange[i].equals(player_hand.get(j).getCode())) {
                        player_hand.remove(j);
                        break;
                    }
                }
            }

            // Add new cards to player hand and update game state
            player_hand.addAll(newCards);
            sortCards(player_hand);

            for (int i = 0; i < player_hand.size(); i++) {
                player_hand.get(i).setHandPositionId(i);
            }

            gameState.setPlayer_hand(player_hand);
            currentDeck.setRemaining(deckInfo.getRemaining());
            gameStateService.save(gameState);
        }

        List<Card> oppnent_hand = gameState.getOpponent_hand();
        sortCards(oppnent_hand);

        model.addAttribute("gamestate", gameState);
        model.addAttribute("deck", currentDeck);
        model.addAttribute("player_hand", gameState.getPlayer_hand());
        model.addAttribute("opponent_hand", oppnent_hand);
        model.addAttribute("exchanged", true);
        return "play";
    }

    @Operation(summary = "Redirects to the start page")
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

            // Transforms named values to int
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

    @Operation(summary = "Draws cards from the deck and reloads the play page. But not in use currently")
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