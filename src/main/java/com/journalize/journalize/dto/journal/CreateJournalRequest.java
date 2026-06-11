package com.journalize.journalize.dto.journal;

import com.journalize.journalize.enums.Sentiment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJournalRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 3, max = 5000, message = "Content must be between 3 and 5000 characters")
    private String content;

    private Sentiment sentiment;
}