package com.ryan.taskManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller // This means that this class is a Controller
@RequestMapping(path="/test") // This means URL's start with /demo (after Application path)
public class RequestController {

    @Autowired // This means to get the bean called userRepository
            // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewUser (@RequestParam String name
        , @RequestParam String email) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request

    User u = new User();
    u.setUsername(name);
    u.setEmail(email);
    userRepository.save(u);
    return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
    // This returns a JSON or XML with the users
    return userRepository.findAll();
    }

    // @PostMapping("/process")
    // public String processFormData(@RequestBody FormData formData) {
    //     // Process the data or perform any backend tasks
    //     // ...

    //     // Return a response back to the frontend
    //     return "{\"status\": \"success\", \"message\": \"Data received successfully!\"}";
    // }
}
