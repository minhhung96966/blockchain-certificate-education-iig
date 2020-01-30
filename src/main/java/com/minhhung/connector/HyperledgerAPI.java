package com.minhhung.connector;

import com.minhhung.model.hyperledger.AddRoster;
import com.minhhung.model.certificate.CertificateTemplate;

import java.util.List;

public interface HyperledgerAPI {
    public String createCertTemplate(CertificateTemplate certificateTemplate);
    public AddRoster addRoster(AddRoster addRoster);
    public List<CertificateTemplate> getAllCertificateTemplate();
    public CertificateTemplate getCertificateTemplateById(String id);
    public List<AddRoster> getAllAddRosterTransaction();
}
