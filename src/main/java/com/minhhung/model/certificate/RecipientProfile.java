package com.minhhung.model.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RecipientProfile {
    private String typen = "RecipientProfile,Extension";
    private String name;
}
