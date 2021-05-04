package tripway.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripway.server.models.User;
import tripway.server.service.users.UserServiceImpl;

import java.util.List;

/**
 * @author Nikita Burtelov
 */
@RestController
@RequestMapping("api/v1")
public class UserController {

    private UserServiceImpl userService;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/user/test")
    public String testMapping() {
        return "/index.html";
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserId(@PathVariable Long id) { //TODO token
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.getUserId(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUser() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> removeUser(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else if (userService.getUserId(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userService.removeUser(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.saveUser(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
