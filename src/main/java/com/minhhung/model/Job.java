package com.minhhung.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jobs")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    private String id;
    private String job_type;
    private String job_title;
    private String publisher;
    private String city;
    private String country;
    private String salary;
    private String published_on;
    private Integer vacancy;
    private String experience;
    private String gender;
    private String deadline;
    private String job_desc;
    private String responsibilities;
    private String experience_desc;
    private String benefits;
}
