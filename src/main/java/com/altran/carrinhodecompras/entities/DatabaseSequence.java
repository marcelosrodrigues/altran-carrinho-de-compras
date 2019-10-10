package com.altran.carrinhodecompras.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "databaseSequence")
@Getter
@NoArgsConstructor()
public class DatabaseSequence {

    @Id
    private String id;

    private long seq;

}