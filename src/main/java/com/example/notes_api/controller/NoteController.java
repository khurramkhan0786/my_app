package com.example.notes_api.controller;

import com.example.notes_api.dto.CreateNoteRequest;
import com.example.notes_api.dto.NoteResponse;
import com.example.notes_api.dto.UpdateNoteRequest;
import com.example.notes_api.model.Note;
import com.example.notes_api.model.User;
import com.example.notes_api.repository.NoteRepository;
import com.example.notes_api.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NoteController {
    private final NoteRepository repo;
    private final UserRepository userRepository; // ✅ inject user repo

    // Create note
    @PostMapping("/notes")
    public ResponseEntity<NoteResponse> create(@Valid @RequestBody CreateNoteRequest req,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note n = Note.builder()
                .title(req.title())
                .content(req.content())
                .user(user)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(NoteResponse.from(repo.save(n)));
    }


    // Get all notes
    @GetMapping("/notes")
    public List<NoteResponse> all() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"))
                .stream()
                .map(NoteResponse::from)
                .toList();
    }

    // Get note by id
    @GetMapping("/notes/{id}")
    public ResponseEntity<NoteResponse> one(@PathVariable Long id) {
        return repo.findById(id)
                .map(n -> ResponseEntity.ok(NoteResponse.from(n)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/users/{userId}/notes")
    public ResponseEntity<List<NoteResponse>> getUserNotes(@PathVariable Long userId) {
        List<NoteResponse> notes = repo.findByUserId(userId, Sort.by(Sort.Direction.DESC, "updatedAt"))
                .stream()
                .map(NoteResponse::from)
                .toList();
        return ResponseEntity.ok(notes);
    }
    @GetMapping("/notes/my")
    public List<NoteResponse> myNotes(@AuthenticationPrincipal UserDetails userDetails) {
        // 1. Find the logged-in user
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Fetch only this user's notes
        List<Note> notes = repo.findByUserId(user.getId(), Sort.by(Sort.Direction.DESC, "updatedAt"));

        // 3. Convert to DTO
        return notes.stream()
                .map(NoteResponse::from)
                .toList();
    }


    // Update note
    @PutMapping("/notes/{id}")
    public ResponseEntity<NoteResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateNoteRequest req) {
        return repo.findById(id).map(n -> {
            n.setTitle(req.title());
            n.setContent(req.content());
            return ResponseEntity.ok(NoteResponse.from(repo.save(n)));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete note
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

