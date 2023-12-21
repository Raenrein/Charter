package com.ryan.taskManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.*;

import javax.swing.text.DateFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;


@Controller // This means that this class is a Controller
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(path = "/u")
public class RequestController {

    @Autowired // This means to get the bean called userRepository
               // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @PostMapping(path = "/signin/register") // Map ONLY POST Requests
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String email,
            @RequestParam String password) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        if(!userRepository.findByEmail(email).isPresent()) {
            User user = new User();
            try {
                user.setUsername(name);
                user.setEmail(email);
                user.setPassword(password);
                userRepository.save(user);
                return "{\"status\": \"success\", \"message\": \"User " + name + " saved!\", \"id\": \"" + user.getID() + "\"}";
            } catch(DataIntegrityViolationException e) {
                return "{\"status\": \"failure\", \"message\": \"Failed to create user.\", \"id\": \"" + user.getID() + "\"}"; // user is created but with no information => fix later
            }
        }
        else {
            return "{\"status\": \"failure\", \"message\": \"User with this email already exists!\", \"id\": \"null\"}";
        }
    }

    @GetMapping(path = "/get/users/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping(path = "/get/users/id")
    public @ResponseBody Optional<User> getUserById(@RequestParam int id) {
        return userRepository.findById(id);
    }

    @GetMapping(path = "/get/users/email")
    public @ResponseBody Optional<User> getUserByEmail(@RequestParam String email) {
        return userRepository.findByEmail(email);
    }

    @GetMapping(path = "/get/users/username")
    public @ResponseBody Optional<User> getUserByUsername(@RequestParam String username) {
        return userRepository.findByUsername(username);
    }

    // AUTHENTICATE (LOGIN/LOGOUT)
    @GetMapping(path = "/signin/auth/validate")
    public @ResponseBody String validateLogin(@RequestParam String email, @RequestParam String password) {
        try {
            if(userRepository.findByEmailAndPassword(email, password).isPresent()) {
                return "{\"status\": \"true\", \"id\": \"" + userRepository.findByEmailAndPassword(email, password).get().getID() + "\"}";
            }
        } catch(NoSuchElementException e) {
            e.printStackTrace();
        }
        return "{\"status\": \"false\", \"id\": \"" + userRepository.findByEmailAndPassword(email, password).get().getID() + "\"}";
    }

    @GetMapping(path = "/signin/auth/get/token")
    public @ResponseBody String getToken(@RequestParam int ID) {
        User user;
        try {
            user = userRepository.findById(ID).get();
        } catch(NoSuchElementException e) {
            System.out.println("No user described by such ID");
            e.printStackTrace();
            return null;
        }
        Random random = new Random();
        String tok = "";
        String[] characters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", 
                               "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", 
                               "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", 
                               "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", 
                               "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                            //    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "!", "@", "#", 
                            //    "$", "%", "&", "*", "(", ")", "+", "-", ".", "~", "=", "-", "_"};
        for(int i = 0; i < 255; i++) {
            tok += characters[random.nextInt(characters.length)];
        }
        user.setAccessToken(tok);
        userRepository.flush();
        
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz");  
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        date = calendar.getTime();
        String tokenExpiryDate = dateFormat.format(date);
        return "{\"token\": \"" + user.getAccessToken() + "\", \"expire\": \"" + tokenExpiryDate + "\"}";
    }

    @GetMapping(path = "/signin/auth/check/token")
    public @ResponseBody String checkToken(@RequestParam int userID, @RequestParam String accessToken, @RequestParam String expiryDateString) {

        Date expiryDate = new Date();
        Date currentDate = new Date();
        try{
        expiryDate = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss ZZZ").parse(expiryDateString);
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(expiryDate.compareTo(currentDate) <= 0) {
            return "{\"valid\": \"false\", \"message\": \"Access token expired.\"}";
        }

        User user;
        try {
            user = userRepository.findById(userID).get();
        } catch(NoSuchElementException e) {
            e.printStackTrace();
            return "{\"valid\": \"false\", \"message\": \"No user described by such ID.\"}";
        }

        return "{\"valid\": \"" + user.getAccessToken().equals(accessToken) + "\", \"message\": \"Access token is valid.\"}";
    }

    

    // CREATE WORKSPACE
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @PostMapping(path = "/assets/w/create") // w stands for workspace => when user is actually in workspace, URL will be "/w/{workspace id}"
    public @ResponseBody String createWorkspace(@RequestParam int userID, @RequestParam String workspaceName, @RequestParam boolean isPublic) {
        
        Workspace workspace = new Workspace();
        try {
            workspace.setUserID(userRepository.findById(userID).get());
            workspace.setName(workspaceName);
            workspace.setIsPublic(isPublic);
            
            workspaceRepository.save(workspace);
            return "{\"status\": \"success\", \"message\": \"Workspace successfully created!\", \"id\": \"" + workspace.getID() + "\"}";
        } catch(Exception e) {
            return "{\"status\": \"failure\", \"message\": \"Workspace could not be created.\", \"id\": \"" + workspace.getID() + "\"}";
        }

    }

    // // GET ALL WORKSPACES UNDER USER
    // @GetMapping(path = "/get/workspaces/userid")
    // public @ResponseBody Iterable<Workspace> getAllWorkspacesByUserID(int userID) {
    //     User user = userRepository.findById(userID).get();
    //     return workspaceRepository.findByUserID(user);
    // }



    // CREATE CHART
    @Autowired
    private ChartRepository chartRepository;

    @PostMapping(path = "/assets/c/create") // c stands for chart => when user is actually in chart, URL will be "/c/{chart id}"
    public @ResponseBody String createChart(@RequestParam int workspaceID, @RequestParam String chartName) {
        
        Chart chart = new Chart();
        try {
            chart.setWorkspaceID(workspaceRepository.findById(workspaceID).get());
            chart.setName(chartName);
            chart.setPosition(chart.getID());
            chartRepository.save(chart);
            return "{\"status\": \"success\", \"message\": \"Chart successfully created!\", \"id\": \"" + chart.getID() + "\"}";
        } catch(Exception e) {
            return "{\"status\": \"failure\", \"message\": \"Chart could not be created.\", \"id\": \"" + chart.getID() + "\"}";
        }

    }

    // // GET ALL CHARTS UNDER WORKSPACE
    // @GetMapping(path = "/get/charts/workspaceid")
    // public @ResponseBody Iterable<Chart> getAllChartsByWorkspaceID(int workspaceID) {
    //     Workspace workspace = workspaceRepository.findById(workspaceID).get();
    //     return chartRepository.findByWorkspaceID(workspace);
    // }

    // CREATE ITEM
    @Autowired
    private ItemRepository itemRepository;

    @PostMapping(path = "/assets/i/create")
    public @ResponseBody String createItem(@RequestParam int chartID, @RequestParam String itemName, @RequestParam String description) {
        
        Item item = new Item();
        try {
            item.setChartID(chartRepository.findById(chartID).get());
            item.setName(itemName);
            item.setDescription(description);
            item.setPosition(item.getID());
            itemRepository.save(item);
            return "{\"status\": \"success\", \"message\": \"Item successfully created!\", \"id\": \"" + item.getID() + "\"}";
        } catch(Exception e) {
            return "{\"status\": \"failure\", \"message\": \"Item could not be created.\", \"id\": \"" + item.getID() + "\"}";
        }

    }

    // // GET ALL ITEMS UNDER CHART
    // @GetMapping(path = "/get/items/chartid")
    // public @ResponseBody Iterable<Chart> getAllItemsByChartID(int chartID) {
    //     Chart chart = chartRepository.findById(chartID).get();
    //     return itemRepository.findByChartID(chart);
    // }

}


