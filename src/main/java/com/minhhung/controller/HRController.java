package com.minhhung.controller;

import com.alibaba.fastjson.JSON;
import com.minhhung.model.AppliedJobs;
import com.minhhung.model.Job;
import com.minhhung.model.PageModel;
import com.minhhung.service.HRService;
import com.minhhung.service.StudentService;
import com.minhhung.utils.Constant;
import com.minhhung.utils.CryptoUtil;
import com.minhhung.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/hr")
public class HRController {

    @Autowired
    HRService hrService;

    @Autowired
    StudentService studentService;

    @GetMapping("/createNewJob/{data}")
    public String saveNewJob(@PathVariable String data) {
        data = CryptoUtil.base64Decode(data);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserName = userDetails.getUsername();

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(date);

        Job job = JSON.parseObject(data, Job.class);
        job.setPublished_on(now);
        job.setId(CryptoUtil.getUUID());
        job.setPublisher(currentUserName);
        hrService.saveJob(job);

        int success = hrService.saveJob(job);
        String jsonString = "{\"success\":\"" + success + "\"}";
        return jsonString;
    }

    @PostMapping("/getJobsList")
    public String getJobsList(@RequestBody String queryCondition) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserName = userDetails.getUsername();

        PageModel<Job> pm = JSON.parseObject(queryCondition, PageModel.class);

        String QueryObject = (pm.getQueryObject() == null || "".equals(pm.getQueryObject())) ? "{}"
                : pm.getQueryObject();
        Map QueryObjectMap = JSON.parseObject(QueryObject, Map.class);
        QueryObjectMap.put("publisher", currentUserName);
        pm.setQueryObject(JSON.toJSONString(QueryObjectMap));

        PageModel<Job> pms = hrService.getJobsPage(pm);
        String jsonString = JSON.toJSONString(pms);
        return jsonString;
    }


    @PostMapping("/getAppliedJob")
    public String getAppliedJob(@RequestBody String queryCondition) {
        PageModel<AppliedJobs> pm = JSON.parseObject(queryCondition, PageModel.class);

        PageModel<AppliedJobs> pms = studentService.getAppliedJobsByJobId(pm);

        String jsonString = JSON.toJSONString(pms);
        return jsonString;
    }

    @RequestMapping(value = "getAppliedJobDetail/{id}", method = RequestMethod.GET)
    public String getAppliedJobDetail(@PathVariable String id) {
        id = CryptoUtil.base64Decode(id);
        AppliedJobs appliedJobs = hrService.getAppliedJob(id);
        String jsonString = JSON.toJSONString(appliedJobs);
        return jsonString;
    }


    @RequestMapping(value = "downloadCV/{id}", method = RequestMethod.GET)
    public String downloadCV(@PathVariable String id) {

        AppliedJobs appliedJobs = hrService.getAppliedJob(id);

        String jsonString = "{\"success\":\"" + 1 + "\"}";

        try {
            byte[] bytesData = appliedJobs.getFile_cv().getData();
            Path path = Paths.get(Constant.UPLOADED_FOLDER + "/cv-" + id);
            Files.write(path, bytesData);
        } catch (Exception e) {
            jsonString = "{\"success\":\"" + 0 + "\"}";
        }

        return jsonString;
    }


    @RequestMapping(value = "downloadCertIIG/{id}", method = RequestMethod.GET)
    public String downloadCertIIG(@PathVariable String id) {

        AppliedJobs appliedJobs = hrService.getAppliedJob(id);

        String jsonString = "{\"success\":\"" + 1 + "\"}";

        try {
            byte[] bytesData = appliedJobs.getFile_iig().getData();
            Path path = Paths.get(Constant.UPLOADED_FOLDER + "/CertIIG-" + id);
            Files.write(path, bytesData);
        } catch (Exception e) {
            jsonString = "{\"success\":\"" + 0 + "\"}";
        }

        return jsonString;
    }

    @RequestMapping(value = "downloadCertUIT/{id}", method = RequestMethod.GET)
    public String downloadCertUIT(@PathVariable String id) {

        AppliedJobs appliedJobs = hrService.getAppliedJob(id);

        String jsonString = "{\"success\":\"" + 1 + "\"}";

        try {
            byte[] bytesData = appliedJobs.getFile_uit().getData();
            Path path = Paths.get(Constant.UPLOADED_FOLDER + "/CertUIT-" + id);
            Files.write(path, bytesData);
        } catch (Exception e) {
            jsonString = "{\"success\":\"" + 0 + "\"}";
        }

        return jsonString;
    }

    @RequestMapping(value = "verifyCertUIT/{id}", method = RequestMethod.GET)
    public String verifyCertUIT(@PathVariable String id) {

        AppliedJobs appliedJobs = hrService.getAppliedJob(id);

        try {
            byte[] bytesData = appliedJobs.getFile_uit().getData();

            Utils.convertPdfToImage(bytesData, Constant.UPLOADED_FOLDER, "/CertUIT-" + id);

            File file = new File(Constant.UPLOADED_FOLDER + "/CertUIT-" + id + ".png");

            String decodedText = Utils.decodeQRCode(file);
            if(decodedText == null) {
                System.out.println("No QR Code found in the image");
            } else {
                System.out.println("Decoded text = " + decodedText);
            }

            return decodedText;

        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "verifyCertIIG/{id}", method = RequestMethod.GET)
    public String verifyCertIIG(@PathVariable String id) {

        AppliedJobs appliedJobs = hrService.getAppliedJob(id);

        try {
            byte[] bytesData = appliedJobs.getFile_uit().getData();

            Utils.convertPdfToImage(bytesData, Constant.UPLOADED_FOLDER, "/CertIIG-" + id);

            File file = new File(Constant.UPLOADED_FOLDER + "/CertIIG-" + id + ".png");

            String decodedText = Utils.decodeQRCode(file);
            if(decodedText == null) {
                System.out.println("No QR Code found in the image");
            } else {
                System.out.println("Decoded text = " + decodedText);
            }

            return decodedText;

        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/checkAppliedJob")
    public String checkAppliedJob(HttpServletRequest request) {
        String job_id = "";

        String state = request.getParameter("apply_state");
        String id = request.getParameter("id");
        try {
            AppliedJobs appliedJobs = hrService.getAppliedJob(id);
            appliedJobs.setState(state);

            hrService.updateAppliedJob(appliedJobs);

            job_id = appliedJobs.getJob_id();
        } catch (Exception e) {

        }

        String jsonString = "{\"job_id\":\"" + job_id  + "\"}";

        return jsonString;
    }

}
