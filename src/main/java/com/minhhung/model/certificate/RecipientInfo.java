package com.minhhung.model.certificate;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RecipientInfo {
    private String certId;
    private Recipient recipient;
    private RecipientProfile recipientProfile;
    private String classification;
}
