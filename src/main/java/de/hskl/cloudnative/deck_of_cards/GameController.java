package de.hskl.cloudnative.deck_of_cards;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        Deck currentDeck = deckService.createNewDeck();
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
        if (game.getUser().getEmail().equals(currentUser.getUsername())) {
            model.addAttribute("deck", game.getDeck());
            model.addAttribute("gamestate", game);
            return "play";
        }

        return "redirect:/game/start";

    }

    @GetMapping("/play/drawCards")
    public String drawCards(@RequestParam("gameId") String gameId, @RequestParam("count") int count, Model model) {
        // Get the deck from game
        GameState gameState = gameStateService.find(gameId);
        Deck currentDeck = gameState.getDeck();

        // Draw Cards from current deck
        DrawCards cardsDrawn = deckService.drawCardsFromDeck(currentDeck.getDeck_id(), count);
        List<Card> cards = cardsDrawn.getCards();

        // Update the game state with the drawn cards
        currentDeck.setRemaining(cardsDrawn.getRemaining());
        gameStateService.save(gameState);

        model.addAttribute("gamestate", gameState);
        model.addAttribute("deck", currentDeck);
        model.addAttribute("cards", cards);
        return "play";
    }

    @GetMapping("/play/backToHomepage")
    public String backToHomepage() {
        return "redirect:/game/start";
    }
}