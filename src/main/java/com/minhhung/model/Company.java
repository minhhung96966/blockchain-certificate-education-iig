package com.minhhung.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "companies")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    private String id;
    private String hr_email;
    private String name;
    private String size;
    private String location;
    private String logo;
    private String description;
}
