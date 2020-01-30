package com.minhhung.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageModel<T> {
	private String queryObject;
	private String sortColumn;
	private String sortType; //ASC, DESC;
	private List<T> rows;
	private int total;
	private int pageSize = 20;
	private int pageNumber = 1;
	private int skip = 0;
}
