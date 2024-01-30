package de.hskl.cloudnative.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String register(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("user", new AuthUser());
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "registration";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute AuthUser user, BindingResult result) {

        System.out.println(user);

        if (result.hasErrors()) {
            System.out.println("Error binding user: " + result.getAllErrors());
            return "redirect:/register?error=Fehler beim Registrieren";
        }
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                System.out.println("Email already exists");
                return "redirect:/register?error=Email wird bereits verwendet";
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.addRole("ROLE_USER");
            userRepository.save(user);
            return "redirect:/game/start";
        } catch (Exception e) {
            return "redirect:/register?error=true";
        }

    }

    @PostMapping("/user/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthUser user) {
        System.out.println(user);
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already Exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Controller
    public static class initController {
        @GetMapping("/")
        public String init() {
            return "redirect:/game/start";
        }
    }
}
