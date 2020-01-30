package com.minhhung.model.hyperledger;

import com.minhhung.model.certificate.RecipientInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddRoster {
    private String $class = "org.degree.AddRoster";
    private String templateId;
    private List<RecipientInfo> recipientsInfo;
    private String transactionId = "";
    private String timestamp = "2019-11-28T03:13:11.996Z";
}
