package com.minhhung.model.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "certs_info")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CertsInfo {

	@Id
	private String id;
	private String transactionId;
	private String certTemplateId;
	private Recipient recipient;
	private RecipientProfile recipientProfile;
	private String classification;
	private String cstate; //new, checked,merged,valid,revoked
}
