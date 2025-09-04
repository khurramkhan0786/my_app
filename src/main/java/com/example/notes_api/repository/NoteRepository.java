package com.example.notes_api.repository;

import com.example.notes_api.model.Note;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note,Long> {
    List<Note> findByUserId(Long userId, Sort sort);
}
