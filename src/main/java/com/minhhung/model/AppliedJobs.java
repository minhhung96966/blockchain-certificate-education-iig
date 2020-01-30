package com.minhhung.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applied_jobs")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppliedJobs {
    @Id
    private String id;
    private String job_id;
    private String candidate_email;
    private Binary file_cv;
    private Binary file_uit;
    private Binary file_iig;
    private String apply_time;
    private String note;
    private String state; //new, passed, rejected
}
