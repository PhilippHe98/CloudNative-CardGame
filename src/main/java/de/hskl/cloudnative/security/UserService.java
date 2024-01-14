package de.hskl.cloudnative.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AuthUser> authUser = userRepository.findByEmail(email);
        if (!authUser.isPresent()) {
            throw new UsernameNotFoundException(email);
        } else {
            return AuthUser.builder()
                    .firstName(authUser.get().getFirstName())
                    .lastName(authUser.get().getLastName())
                    .email(authUser.get().getEmail())
                    .password(authUser.get().getPassword())
                    .roles(authUser.get().getRoles())
                    .build();
        }
    }

    public AuthUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        return (AuthUser) loadUserByUsername(currentUser); // assuming you have a method to fetch user by username
    }
}
