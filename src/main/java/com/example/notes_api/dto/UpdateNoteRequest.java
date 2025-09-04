package com.example.notes_api.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateNoteRequest(@NotBlank String title,
                                @NotBlank String content) {
}
