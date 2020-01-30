package com.minhhung.model.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CertificateTemplate {
    private String templateId;
    private String $class = "org.degree.CertificateTemplate";
    private String typeC = "Assertion";
    private Badge badge;
    private String context = "https://w3id.org/openbadges/v2,https://w3id.org/blockcerts/v2";
    private boolean revoked = false;
}