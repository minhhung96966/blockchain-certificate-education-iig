package com.minhhung.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    private String id;
    private String u_name;
    private String u_passwd;
    private String role;
    private String state;
}
