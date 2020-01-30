package com.minhhung.dto;

import org.springframework.web.multipart.MultipartFile;

public class AppliedJobsDTO {

    private String id;
    private String job_id;
    private String candidate_email;
    private MultipartFile file_cv;
    private MultipartFile file_uit;
    private MultipartFile file_iig;
    private String apply_time;
    private String note;
    private String state; //new, passed, rejected

    public AppliedJobsDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getCandidate_email() {
        return candidate_email;
    }

    public void setCandidate_email(String candidate_email) {
        this.candidate_email = candidate_email;
    }

    public MultipartFile getFile_cv() {
        return file_cv;
    }

    public void setFile_cv(MultipartFile file_cv) {
        this.file_cv = file_cv;
    }

    public MultipartFile getFile_uit() {
        return file_uit;
    }

    public void setFile_uit(MultipartFile file_uit) {
        this.file_uit = file_uit;
    }

    public MultipartFile getFile_iig() {
        return file_iig;
    }

    public void setFile_iig(MultipartFile file_iig) {
        this.file_iig = file_iig;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
