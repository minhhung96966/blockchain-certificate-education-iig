package com.minhhung.controller;

import com.alibaba.fastjson.JSON;
import com.minhhung.connector.HyperledgerAPI;
import com.minhhung.model.*;
import com.minhhung.model.certificate.*;
import com.minhhung.service.AdminService;
import com.minhhung.service.CheckerService;
import com.minhhung.service.IssuerService;
import com.minhhung.service.StudentService;
import com.minhhung.utils.Constant;
import com.minhhung.utils.CryptoUtil;
import com.minhhung.utils.Utils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/checker")
public class CheckerController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CheckerService checkerService;

    @Autowired
    private IssuerService issuerService;


    @Autowired
    private HyperledgerAPI hyperledgerAPI;

    /**
     * get student configure by pages
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "postStuConfList", method = RequestMethod.POST)
    public String getStuConfList(@RequestBody String queryCondition) {
        PageModel<StuConf> pm = JSON.parseObject(queryCondition, PageModel.class);
        PageModel<StuConf> pms = studentService.getStuConfPage(pm);
        String jsonString = JSON.toJSONString(pms);
        return jsonString;
    }


    /**
     * get student configure by id
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
     * check student configure (when click on 'approve' or 'reject' student config - checker-form)
     *
     * @return
     */
    @RequestMapping(value = "checkStuConf", method = RequestMethod.POST)
    public String checkStuConf(HttpServletRequest request) {

        String stuConfStr = request.getParameter("stuConfStr");
        stuConfStr = CryptoUtil.base64Decode(stuConfStr);
        StuConf stuConf = JSON.parseObject(stuConfStr, StuConf.class);
        String applyState = stuConf.getApply_state();

        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String handle_time = format.format(date);
        stuConf.setHandle_time(handle_time); // Set processing time

        int success = 0;

        if ("passed".equalsIgnoreCase(applyState)) { //If you pass, start to form a certificate

            String checkInfoStr = request.getParameter("checkInfoStr");
            checkInfoStr = CryptoUtil.base64Decode(checkInfoStr);
            CheckInfo checkInfo = JSON.parseObject(checkInfoStr, CheckInfo.class);

            /*
            List<SchConf> schlist = adminService.findAllSchConf();
            SchConf schConf = schlist.get(0); //Get school configuration information

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            String currentUserName = userDetails.getUsername(); // Get current user

            // String user_id = stuConf.getUser_id();

            */

            CertsInfo certsInfo = assembleCertificate(stuConf, checkInfo);

            int insertFlag = checkerService.saveCertsInfo(certsInfo); // Save the latest certificate

            stuConf.setCerts_id(certsInfo.getId());
            int updateFlag = studentService.updateStuConf(stuConf); // Update application form

            success = insertFlag * updateFlag;

        } else if ("rejected".equalsIgnoreCase(applyState)) { // If rejected
            success = studentService.updateStuConf(stuConf); // Update application form
        }

        String jsonString = "{\"success\":\"" + success + "\"}";
        return jsonString;

    }

    private CertsInfo assembleCertificate(StuConf stuConf, CheckInfo checkInfo) {

        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);

        CertsInfo certsInfo = new CertsInfo();

        String id = CryptoUtil.getUUID();
        certsInfo.setCstate("new");
        certsInfo.setId(id);
        certsInfo.setTransactionId("");
        certsInfo.setClassification(checkInfo.getClassification());
        certsInfo.setCertTemplateId(checkInfo.getCertTemplateId());

        Recipient recipient = new Recipient();
        recipient.setEmail(stuConf.getIdentity());

        RecipientProfile recipientProfile = new RecipientProfile();
        recipientProfile.setName(stuConf.getGiven_name() + " " + stuConf.getFamily_name());

        certsInfo.setRecipient(recipient);
        certsInfo.setRecipientProfile(recipientProfile);

        return certsInfo;
    }


    /**
     * get the certificate information by pages by id (when click on certificate detail - checked history)
     *
     * @return
     */
    @RequestMapping(value = "getCertsInfo/{id}", method = RequestMethod.GET)
    public String getCertsInfo(@PathVariable String id) {
        id = CryptoUtil.base64Decode(id);
        CertsInfo CertsInfo = checkerService.getCertsInfo(id);
        String jsonString = JSON.toJSONString(CertsInfo);
        return jsonString;
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

    /**
     * get certificate information by pages (ex: on checker-unmerged-certificate-list, ...)
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
     * merge the certificate
     *
     * @return
     */
    @RequestMapping(value = "mergeCertificate", method = RequestMethod.POST)
    public String mergeCertificate(HttpServletRequest request) {

        String certsId = request.getParameter("certsId");

        List<CertsInfo> list = new ArrayList<CertsInfo>();
        if (certsId == null || "".equals(certsId)) {
            list = checkerService.getCertsInfoByCstate("new");
        } else {
            list = checkerService.getCertsInfoByIds(certsId);
        }

        int success = 0;

        for (CertsInfo c : list) {
            c.setCstate("merged");
            success = checkerService.updateCertsInfo(c);
        }

        String jsonString = "{\"success\":\"" + success + "\"}";
        return jsonString;
    }


    @RequestMapping(value = "/downloadCertIIG/{id}", method = RequestMethod.GET)
    public void downloadCertIIG(@PathVariable String id, HttpServletResponse response) {
        try {
            id = CryptoUtil.base64Decode(id);
            StuConf StuConf = studentService.getStuConf(id);

            String uuid = CryptoUtil.getUUID();
            String fileName = "certificate-" + uuid + ".pdf";
            String path = Constant.UPLOADED_FOLDER + "/" + fileName;
            byte[] bytesData = StuConf.getFile_binary().getData();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytesData);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename="+fileName);

            IOUtils.copy(byteArrayInputStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "verifyCertIIG/{id}", method = RequestMethod.GET)
    public String verifyCertIIG(@PathVariable String id) {

        StuConf StuConf = studentService.getStuConf(id);

        try {
            byte[] bytesData = StuConf.getFile_binary().getData();

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
}
