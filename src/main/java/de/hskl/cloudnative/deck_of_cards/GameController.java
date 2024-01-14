package de.hskl.cloudnative.deck_of_cards;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/start/newGame")
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
    
    @GetMapping("/start/playGame")
    public String redirectToGame(@RequestParam("gameId") String gameId, Model model) {
        GameState gameState = gameStateService.find(gameId);
        model.addAttribute("deck", gameState.getDeck());
        model.addAttribute("gamestate", gameState);
        return "play";
    }
    
    @GetMapping("start/drawCards")
    public String drawCards(@RequestParam("gameId") String gameId, @RequestParam("count") int count, Model model) {
        //TODO: Update gamestate
        GameState gameState = gameStateService.find(gameId);
        Deck currentDeck = gameState.getDeck();
        DrawCards cardsDrawn = deckService.drawCardsFromDeck(currentDeck.getDeck_id(), count);
        List<Card> cards = cardsDrawn.getCards();
        model.addAttribute("gamestate", gameState);
        model.addAttribute("deck", currentDeck);
        model.addAttribute("cards", cards);
        return "play";
    }
}
