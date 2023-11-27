package com.ryan.taskManager;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // This means that this class is a Controller
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(path = "/signin")
public class RequestController {

    @Autowired // This means to get the bean called userRepository
               // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @PostMapping(path = "/register") // Map ONLY POST Requests
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String email,
            @RequestParam String password) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        User user = new User();
        try {
            user.setUsername(name);
            user.setEmail(email);
            user.setPassword(password);
            userRepository.save(user);
            return "{'message': 'User " + name + " saved!'}";
        } catch(DataIntegrityViolationException e) {
            return "{'message': 'User already exists!'}";
        }
    }

    @GetMapping(path = "/get/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping(path = "/get/id")
    public @ResponseBody Optional<User> getUserById(@RequestParam int id) {
        return userRepository.findById(id);
    }

    @GetMapping(path = "/get/email")
    public @ResponseBody Optional<User> getUserByEmail(@RequestParam String email) {
        return userRepository.findByEmail(email);
    }

    @GetMapping(path = "/get/username")
    public @ResponseBody Optional<User> getUserByUsername(@RequestParam String username) {
        return userRepository.findByUsername(username);
    }

    // AUTHENTICATE (LOGIN)
    // @GetMapping(path = "/check/credentials")
    // public @ResponseBody boolean checkCredentials(@RequestParam String email, @RequestParam String password) {
    //     try {
    //         if(userRepository.findByEmail(email) != null) {

    //         }
    //     } catch(Exception e) {

    //     }
    // }

}


