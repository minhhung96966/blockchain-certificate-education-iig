package com.minhhung.mapper;

import com.minhhung.dto.AppliedJobsDTO;
import com.minhhung.model.AppliedJobs;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AppliedJobsMapper {

    public AppliedJobs converToModel(AppliedJobsDTO appliedJobsDTO) throws IOException {
        AppliedJobs appliedJobs = new AppliedJobs();
        appliedJobs.setApply_time(appliedJobsDTO.getApply_time());
        appliedJobs.setCandidate_email(appliedJobsDTO.getCandidate_email());
        appliedJobs.setFile_cv(new Binary(BsonBinarySubType.BINARY, appliedJobsDTO.getFile_cv().getBytes()));
        appliedJobs.setFile_iig(new Binary(BsonBinarySubType.BINARY, appliedJobsDTO.getFile_iig().getBytes()));
        appliedJobs.setFile_uit(new Binary(BsonBinarySubType.BINARY, appliedJobsDTO.getFile_uit().getBytes()));
        appliedJobs.setId(appliedJobsDTO.getId());
        appliedJobs.setJob_id(appliedJobsDTO.getJob_id());
        appliedJobs.setNote(appliedJobsDTO.getNote());
        appliedJobs.setState(appliedJobsDTO.getState());

        return appliedJobs;
    }
}
