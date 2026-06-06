package com.journalize.journalize.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @Indexed(unique = true)
    @NonNull
    private String email;

    @NonNull
    private String password;

    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Builder.Default
    @DBRef
    private List<Journal> journals = new ArrayList<>();
}