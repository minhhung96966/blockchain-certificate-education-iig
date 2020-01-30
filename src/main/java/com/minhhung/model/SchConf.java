package com.minhhung.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sch_conf")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SchConf {
	@Id
	private String id;
	private String type;
	private String name;
	private String email;
	private String url;
	private String image;
}
