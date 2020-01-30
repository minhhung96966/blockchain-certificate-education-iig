package com.minhhung.service;

import com.minhhung.model.*;
import com.minhhung.persistence.BaseMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HRService {

    @Autowired
    private BaseMongoTemplate baseMongoTemplate;

    public int saveCompany(Company company) {
        return baseMongoTemplate.saveEntity(company);
    }

    public int saveJob(Job job) {
        return baseMongoTemplate.saveEntity(job);
    }

    public PageModel<Job> getJobsPage(PageModel<Job> page) {
        page = baseMongoTemplate.getListPage(page, Job.class);
        return page;
    }

    public Job getJobById(String id) {
        return baseMongoTemplate.getEntity("id", id, Job.class);
    }

    public AppliedJobs getAppliedJob(String id) {
        return baseMongoTemplate.getEntity("id", id, AppliedJobs.class);
    }

    public int updateAppliedJob(AppliedJobs appliedJobs) {
        String id = appliedJobs.getId();
        return baseMongoTemplate.updateEntity(id, appliedJobs, AppliedJobs.class);
    }
}
