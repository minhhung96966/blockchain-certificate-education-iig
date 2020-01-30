package com.minhhung.controller;

import com.alibaba.fastjson.JSON;
import com.minhhung.connector.HyperledgerAPI;
import com.minhhung.dto.AppliedJobsDTO;
import com.minhhung.mapper.AppliedJobsMapper;
import com.minhhung.model.AppliedJobs;
import com.minhhung.model.Job;
import com.minhhung.model.PageModel;
import com.minhhung.model.StuConf;
import com.minhhung.model.certificate.CertificateTemplate;
import com.minhhung.model.certificate.CertsInfo;
import com.minhhung.service.HRService;
import com.minhhung.service.IssuerService;
import com.minhhung.service.StudentService;
import com.minhhung.utils.Constant;
import com.minhhung.utils.CryptoUtil;
import com.minhhung.utils.Utils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.IDENTITY_H;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private IssuerService issuerService;

    @Autowired
    private HRService hrService;

    @Autowired
    private AppliedJobsMapper appliedJobsMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private HyperledgerAPI hyperledgerAPI;
    /**
     * get certificate state
     *
     * @return
     */
    @RequestMapping(value = "getCertState/{certId}", method = RequestMethod.GET)
    public String getCertState(@PathVariable String certId) {
        // certId = CryptoUtil.base64Decode(certId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserName = userDetails.getUsername(); //
        CertsInfo certsInfo = studentService.getCertStateByUserId(currentUserName);
        String jsonString;
        if (certsInfo == null) {
            jsonString = "{\"certState\":\"0\"}";
        } else {
            jsonString = "{\"certState\":\"" + certsInfo.getCstate() + "\"}";
        }
        return jsonString;
    }

    /**
     * get certificate information
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "postCertsInfoList", method = RequestMethod.POST)
    public String postCertsInfoList(@RequestBody String queryCondition) {
        PageModel<CertsInfo> pm = JSON.parseObject(queryCondition, PageModel.class);
        PageModel<CertsInfo> pms = issuerService.getCertsInfoPage(pm);
        String jsonString = JSON.toJSONString(pms);
        return jsonString;
    }

    /**
     * get student configure
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "getStuConfList", method = RequestMethod.POST)
    public String getStuConfList(@RequestBody String queryCondition) {
        // queryCondition = CryptoUtil.base64Decode(queryCondition);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserName = userDetails.getUsername();

        PageModel<StuConf> pm = JSON.parseObject(queryCondition, PageModel.class);

        String QueryObject = (pm.getQueryObject() == null || "".equals(pm.getQueryObject())) ? "{}"
                : pm.getQueryObject();
        Map QueryObjectMap = JSON.parseObject(QueryObject, Map.class);
        QueryObjectMap.put("user_id", currentUserName);
        pm.setQueryObject(JSON.toJSONString(QueryObjectMap));

        PageModel<StuConf> pms = studentService.getStuConfPage(pm);
        String jsonString = JSON.toJSONString(pms);
        return jsonString;
    }

    /**
     * get all the student configure
     *
     * @return
     */
    @RequestMapping(value = "getAllStuConf", method = RequestMethod.GET)
    public String getAllStuConf() {
        List<StuConf> list = studentService.findAllStuConf();
        String jsonString = JSON.toJSONString(list);
        return jsonString;
    }

    /**
     * get student configure
     *
     * @return
     */
    @RequestMapping(value = "getStuConf/{id}", method = RequestMethod.GET)
    public String getStuConf(@PathVariable String id) {
        id = CryptoUtil.base64Decode(id);
        StuConf StuConf = studentService.getStuConf(id);
        String jsonString = JSON.toJSONString(StuConf);
        return jsonString;
    }

    /**
     * delete student configure
     *
     * @return
     */
    @RequestMapping(value = "delStuConf/{delIds}", method = RequestMethod.GET)
    public String delStuConf(@PathVariable String delIds) {
        delIds = CryptoUtil.base64Decode(delIds);
        int success = studentService.delStuConf(delIds);
        String jsonString = "{\"success\":\"" + success + "\"}";
        return jsonString;
    }

    /**
     * save student configure
     *
     * @return
     */
    @RequestMapping(value = "saveStuConf/{data}", method = RequestMethod.GET)
    public String saveStuConf(@PathVariable String data) {
        data = CryptoUtil.base64Decode(data);

        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserName = userDetails.getUsername(); // 获取当前用户

        StuConf StuConf = JSON.parseObject(data, StuConf.class);
        StuConf.setApply_state("new");
        StuConf.setApply_note("");
        StuConf.setApply_time(createTime);
        StuConf.setHandle_time("");
        StuConf.setId(CryptoUtil.getUUID());
        StuConf.setUser_id(currentUserName);

        if(!StringUtils.isEmpty(StuConf.getFile_hash())) {
            byte[] encode = Base64.getDecoder().decode(StuConf.getFile_hash());
            Binary file_binary =  new Binary(BsonBinarySubType.BINARY, encode);
            StuConf.setFile_binary(file_binary);
        }

        int success = studentService.saveStuConf(StuConf);
        String jsonString = "{\"success\":\"" + success + "\"}";
        return jsonString;
    }

    /**
     *
     * update student configure
     * @return
     */
    @RequestMapping(value = "updateStuConf/{stuConfInfo}", method = RequestMethod.GET)
    public String updateStuConf(@PathVariable String stuConfInfo) {
        stuConfInfo = CryptoUtil.base64Decode(stuConfInfo);
        StuConf stuConf = JSON.parseObject(stuConfInfo, StuConf.class);
        int success = studentService.updateStuConf(stuConf);
        String jsonString = "{\"success\":\"" + success + "\"}";
        return jsonString;
    }


    @PostMapping("/getJobsList")
    public String getJobsList(@RequestBody String queryCondition) {
        PageModel<Job> pm = JSON.parseObject(queryCondition, PageModel.class);
        PageModel<Job> pms = hrService.getJobsPage(pm);
        String jsonString = JSON.toJSONString(pms);
        return jsonString;
    }

    @PostMapping("/uploadcv")
    public ResponseEntity<?> uploadcv(@ModelAttribute AppliedJobsDTO dto) {

        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserName = userDetails.getUsername();

        dto.setApply_time(createTime);
        dto.setCandidate_email(currentUserName);
        dto.setId(CryptoUtil.getUUID());
        dto.setState("new");

        try {

            AppliedJobs appliedJobs = appliedJobsMapper.converToModel(dto);
            studentService.saveAppliedJobs(appliedJobs);


        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);
    }


    @GetMapping("/generatePDF/{id}")
    public void generatePDF(@PathVariable String id, HttpServletResponse response) {
        CertsInfo certsInfo = issuerService.getCertsInfo(id);
        CertificateTemplate certificateTemplate = hyperledgerAPI.getCertificateTemplateById(certsInfo.getCertTemplateId());

//        String content = JSON.toJSONString(certsInfo);
        byte[] qrCodeByteArr =  Utils.getQRCodeImage(certsInfo.getTransactionId(), 200, 200);
        String base64 = Base64.getEncoder().encodeToString(qrCodeByteArr);
        System.out.println(base64);

        Context context = new Context();
        context.setVariable("cert", certsInfo);
        context.setVariable("certTemplate", certificateTemplate);
        context.setVariable("qrcode", "data:image/png;base64, " + base64);

        try {
            String renderedHtmlContent = templateEngine.process("/certificate", context);
            String xHtml = Utils.convertToXhtml(renderedHtmlContent);

            ITextRenderer renderer = new ITextRenderer();
            renderer.getFontResolver().addFont("static/fonts/Code39.ttf", IDENTITY_H, EMBEDDED);

            String baseUrl = FileSystems
                    .getDefault()
                    .getPath("src", "main", "resources")
                    .toUri()
                    .toURL()
                    .toString();
            renderer.setDocumentFromString(xHtml, baseUrl);
            renderer.layout();

            String uuid = CryptoUtil.getUUID();
            String fileName = "certificate-" + uuid + ".pdf";
            String path = Constant.UPLOADED_FOLDER + "/" + fileName;
            OutputStream outputStream = new FileOutputStream(path);
            renderer.createPDF(outputStream);
            outputStream.close();

            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename="+fileName);

            Files.copy(Paths.get(path), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * get the certificate template by id
     *
     * @return
     */
    @RequestMapping(value = "getCertificateTemplate/{id}", method = RequestMethod.GET)
    public String getCertificateTemplate(@PathVariable String id) {
        id = CryptoUtil.base64Decode(id);
        CertificateTemplate certificateTemplate = hyperledgerAPI.getCertificateTemplateById(id);
        String jsonString = JSON.toJSONString(certificateTemplate);
        return jsonString;
    }
}
