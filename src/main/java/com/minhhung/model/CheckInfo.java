package com.minhhung.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CheckInfo {
	private String id;
	private String certTemplateId;
	private String standard;
	private String badgeClass;
	private String badgeId;
	private String badgeType;
	private String badgeName;
	private String badgeDescription;
	private String classification;
	private String badgeImage;
	private String created;
	private String expires;
}
