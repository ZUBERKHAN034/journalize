package com.journalize.journalize.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Config {

    @Id
    private String id;

    @NonNull
    private String key;

    @NonNull
    private String value;
}