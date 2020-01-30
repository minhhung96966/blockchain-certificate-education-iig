package com.minhhung.model.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class School {
    private String id;
    private String $class = "composer.blockcerts.School";
    private String typen = "Shool,Extension";
    private String name;
    private String urln;
    private String email;
    private String image;
}
