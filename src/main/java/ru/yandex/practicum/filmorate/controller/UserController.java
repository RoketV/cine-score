package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto dto) {
        return ResponseEntity.ok(userService.postUser(dto));
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody @Valid UserDto dto) {
        return ResponseEntity.ok(userService.updateUser(dto));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
        return ResponseEntity.ok("friend added");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFriend(id, friendId);
        return ResponseEntity.ok("Friend deleted");
    }

    @GetMapping("{id}/friends")
    public ResponseEntity<List<UserDto>> getFriends(@PathVariable long id) {
        return ResponseEntity.ok(userService.getFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<UserDto>> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return ResponseEntity.ok(userService.getMutualFriends(id, otherId));
    }
}

