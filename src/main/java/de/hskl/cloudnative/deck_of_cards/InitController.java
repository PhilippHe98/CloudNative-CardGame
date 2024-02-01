package de.hskl.cloudnative.deck_of_cards;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;

@Controller
public class InitController {
    @Operation(summary = "Redirects to starting page")
    @GetMapping("/")
    public String init() {
        return "redirect:/game/start";
    }
}
