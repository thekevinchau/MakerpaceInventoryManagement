package com.Makerspace.MakerspaceInventoryApp.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
@CrossOrigin(origins = "*") // Allow all origins for this controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Register users, which will be protected by the Admin, as they should be the only ones creating users.
    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody User user){
        return userService.register(user);
    }


    //Unrestricted endpoint. Anyone can try to log in.
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        return userService.login(user);
    }


}
