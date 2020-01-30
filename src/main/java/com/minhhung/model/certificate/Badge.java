package com.minhhung.model.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Badge {
	private String id;
	private String $class = "composer.blockcerts.Badge";
	private String typen = "BadgeClass";
	private String name;
	private String description;
	private String image;
	private String criteria;
	private Issuer issuer;
}
