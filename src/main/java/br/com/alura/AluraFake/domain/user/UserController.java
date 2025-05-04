package br.com.alura.AluraFake.domain.user;

import br.com.alura.AluraFake.domain.user.dto.AuthenticationRequestDTO;
import br.com.alura.AluraFake.domain.user.dto.AuthenticationResponseDTO;
import br.com.alura.AluraFake.domain.user.dto.NewUserDTO;
import br.com.alura.AluraFake.domain.user.dto.UserListItemDTO;
import br.com.alura.AluraFake.domain.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserRepository userRepository, UserService userService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/user/login")
    public AuthenticationResponseDTO authenticate(@RequestBody AuthenticationRequestDTO request) {
        return userService.authenticate(request, authenticationManager);
    }

    @Transactional
    @PostMapping("/user/new")
    public ResponseEntity newStudent(@RequestBody @Valid NewUserDTO newUser) {
        if(userRepository.existsByEmail(newUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "Email já cadastrado no sistema"));
        }
        User user = newUser.toModel();
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/user/all")
    public List<UserListItemDTO> listAllUsers() {
        return userRepository.findAll().stream().map(UserListItemDTO::new).toList();
    }



}
