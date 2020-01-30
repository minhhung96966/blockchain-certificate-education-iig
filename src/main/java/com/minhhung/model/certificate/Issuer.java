package com.minhhung.model.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Issuer {
	private String $class = "composer.blockcerts.Issuer";
	private String id;
	private String typen = "Profile";
	private String name;
	private String urln;
	private String email;
	private String description;
	private String image;
	private School school;
	private SignatureLines signatureLines;
}
