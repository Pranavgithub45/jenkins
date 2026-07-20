package com.billdesk.forjenkins.controllers;

import com.billdesk.forjenkins.models.player;
import com.billdesk.forjenkins.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<player> createPlayer(@RequestBody player player) {
        player savedPlayer = playerRepository.save(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<player>> getAllPlayers() {
        List<player> players = playerRepository.findAll();
        return ResponseEntity.ok(players);
    }

    // READ BY ID
    @GetMapping("/{jerseyNo}")
    public ResponseEntity<?> getPlayerById(@PathVariable int jerseyNo) {

        Optional<player> player = playerRepository.findById(jerseyNo);

        if (player.isPresent()) {
            return ResponseEntity.ok(player.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Player with jersey number " + jerseyNo + " not found.");
    }

    // UPDATE
    @PutMapping("/{jerseyNo}")
    public ResponseEntity<?> updatePlayer(@PathVariable int jerseyNo,
                                          @RequestBody player updatedPlayer) {

        Optional<player> optionalPlayer = playerRepository.findById(jerseyNo);

        if (optionalPlayer.isPresent()) {

            player existingPlayer = optionalPlayer.get();

            existingPlayer.setName(updatedPlayer.getName());
            existingPlayer.setAge(updatedPlayer.getAge());

            // Usually jerseyNo (Primary Key) should not be updated.

            player savedPlayer = playerRepository.save(existingPlayer);

            return ResponseEntity.ok(savedPlayer);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Player with jersey number " + jerseyNo + " not found.");
    }

    // DELETE
    @DeleteMapping("/{jerseyNo}")
    public ResponseEntity<String> deletePlayer(@PathVariable int jerseyNo) {

        if (!playerRepository.existsById(jerseyNo)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Player not found.");
        }

        playerRepository.deleteById(jerseyNo);

        return ResponseEntity.ok("Player deleted successfully.");
    }
}