package com.minhhung.model.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignatureLines {
    private String $class = "composer.blockcerts.SignatureLines";
    private String typen = "SignatureLine,Extension";
    private String name;
    private String image;
    private String jobtitle;
}
