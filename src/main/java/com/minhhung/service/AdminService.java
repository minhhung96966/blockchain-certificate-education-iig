package com.minhhung.service;

import com.minhhung.model.PageModel;
import com.minhhung.model.SchConf;
import com.minhhung.model.Users;
import com.minhhung.persistence.BaseMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @Title: AdminService.java
 * @Package com.minhhung.service
 * @Description: TODO AdminService
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */

@Service
public class AdminService {

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	/**
	 * getUsersPage
	 * @param page
	 * @return
	 */
	public PageModel<Users> getUsersPage(PageModel<Users> page) {
		page = baseMongoTemplate.getListPage(page, Users.class);
		return page;
	}

	/**
	 * getUsers
	 * @param id
	 * @return
	 */
	public Users getUsers(String id) {
		return (Users) baseMongoTemplate.getEntity("id", id, Users.class);
	}

	/**
	 * updateUsers
	 * @param users
	 * @return
	 */
	public int updateUsers(Users users) {
		String id = users.getId();
		return baseMongoTemplate.updateEntity(id, users, Users.class);
	}

	/**
	 * saveUsers
	 * @param schConf
	 * @return
	 */
	public int saveUsers(Users schConf) {
		return baseMongoTemplate.saveEntity(schConf);
	}

	/**
	 * getSchConfPage
	 * @param page
	 * @return
	 */
	public PageModel<SchConf> getSchConfPage(PageModel<SchConf> page) {
		page = baseMongoTemplate.getListPage(page, SchConf.class);
		return page;
	}

	/**
	 * findAllSchConf
	 * @return
	 */
	public List<SchConf> findAllSchConf() {
		return baseMongoTemplate.findAll(SchConf.class);
	}

	/**
	 * getSchConf
	 * @param id
	 * @return
	 */
	public SchConf getSchConf(String id) {
		return (SchConf) baseMongoTemplate.getEntity("id", id, SchConf.class);
	}

	/**
	 * updateSchConf
	 * @param schConf
	 * @return
	 */
	public int updateSchConf(SchConf schConf) {
		String id = schConf.getId();
		return baseMongoTemplate.updateEntity(id, schConf, SchConf.class);
	}

	/**
	 * saveSchConf
	 * @param schConf
	 * @return
	 */
	public int saveSchConf(SchConf schConf) {
		return baseMongoTemplate.saveEntity(schConf);
	}

}
