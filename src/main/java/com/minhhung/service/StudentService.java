package com.minhhung.service;

import com.minhhung.model.AppliedJobs;
import com.minhhung.model.PageModel;
import com.minhhung.model.StuConf;
import com.minhhung.model.certificate.CertsInfo;
import com.minhhung.persistence.BaseMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private BaseMongoTemplate baseMongoTemplate;

    /**
     * getStuConfPage
     * @param page
     * @return
     */
    public PageModel<StuConf> getStuConfPage(PageModel<StuConf> page) {
        page = baseMongoTemplate.getListPage(page, StuConf.class);
        return page;
    }

    /**
     * getStuConf
     * @param id
     * @return
     */
    public StuConf getStuConf(String id) {
        return baseMongoTemplate.getEntity("id", id, StuConf.class);
    }

    /**
     * findAllStuConf
     * @return
     */
    public List<StuConf> findAllStuConf() {
        return baseMongoTemplate.findAll(StuConf.class);
    }

    /**
     * getCertStateByUserId
     * @param userId
     * @return
     */
    public CertsInfo getCertStateByUserId(String userId) {
        return baseMongoTemplate.getEntity("recipient.email", userId, CertsInfo.class);
    }

    /**
     * delStuConf
     * @param delIds
     * @return
     */
    public int delStuConf(String delIds) {
        return baseMongoTemplate.delEntity(delIds, StuConf.class);
    }

    /**
     * saveStuConfBatch
     * @param StuConfList
     * @return
     */
    public int saveStuConfBatch(List<StuConf> StuConfList) {
        return baseMongoTemplate.saveEntityBatch(StuConfList);
    }

    /**
     * saveStuConf
     * @param StuConf
     * @return
     */
    public int saveStuConf(StuConf StuConf) {
        return baseMongoTemplate.saveEntity(StuConf);
    }

    /**
     * updateStuConf
     * @param stuConf
     * @return
     */
    public int updateStuConf(StuConf stuConf) {
        String id = stuConf.getId();
        return baseMongoTemplate.updateEntity(id, stuConf, StuConf.class);
    }

    public int saveAppliedJobs(AppliedJobs appliedJobs) {
        return baseMongoTemplate.saveEntity(appliedJobs);
    }

    public PageModel<AppliedJobs> getAppliedJobsByJobId(PageModel<AppliedJobs> page) {
        page = baseMongoTemplate.getListPage(page, AppliedJobs.class);
        return page;
    }

}
