package com.minhhung.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stu_conf")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StuConf {
    @Id
    private String id;
    private String user_id;
    private String given_name;
    private String family_name;
    private String identity;
    private String identity_type;
    private String birthday;
    private String file_hash; // not use .... :))
    private Binary file_binary;
    private String apply_type;
    private String apply_state; //new, passed, rejected
    private String apply_note;
    private String apply_time;
    private String handle_time;
    private String certs_id;
}
