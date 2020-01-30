package com.minhhung.controller;

import com.alibaba.fastjson.JSON;
import com.minhhung.connector.HyperledgerAPI;
import com.minhhung.model.PageModel;
import com.minhhung.model.certificate.CertificateTemplate;
import com.minhhung.model.certificate.CertsInfo;
import com.minhhung.model.certificate.RecipientInfo;
import com.minhhung.model.hyperledger.AddRoster;
import com.minhhung.service.CheckerService;
import com.minhhung.service.IssuerService;
import com.minhhung.utils.Constant;
import com.minhhung.utils.CryptoUtil;
import com.minhhung.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.IDENTITY_H;

@RestController
@RequestMapping("/issuer")
public class IssuerController {

    private static Logger logger = LoggerFactory.getLogger(IssuerController.class);

    @Autowired
    private IssuerService issuerService;

    @Autowired
    private CheckerService checkerService;

    @Autowired
    private HyperledgerAPI hyperledgerAPI;

    @Autowired
    private TemplateEngine templateEngine;

    @RequestMapping(value = "postCertsInfoList", method = RequestMethod.POST)
    public String postCertsInfoList(@RequestBody String queryCondition) {
        PageModel<CertsInfo> pm = JSON.parseObject(queryCondition, PageModel.class);
        PageModel<CertsInfo> pms = issuerService.getCertsInfoPage(pm);
        String jsonString = JSON.toJSONString(pms);
        return jsonString;
    }

    @RequestMapping(value = "getCertsInfo/{id}", method = RequestMethod.GET)
    public String getCertsInfo(@PathVariable String id) {
        id = CryptoUtil.base64Decode(id);
        CertsInfo CertsInfo = checkerService.getCertsInfo(id);
        String jsonString = JSON.toJSONString(CertsInfo);
        return jsonString;
    }

    @RequestMapping(value = "broadcastCertificate", method = RequestMethod.POST)
    public String broadcastCertificate(HttpServletRequest httpServletRequest) {
        String jsonString = "{\"success\":\"" + 0 + "\"}";

        String certsId = httpServletRequest.getParameter("certsId");
        List<CertsInfo> list = checkerService.getCertsInfoByIds(certsId);

        for (CertsInfo c : list) {
            try {
                AddRoster addRoster = new AddRoster();
                addRoster.setTemplateId("resource:org.degree.CertificateTemplate#" + c.getCertTemplateId());
                Random random = new Random();
                String certId = String.valueOf(random.nextInt(10000000));
                addRoster.setRecipientsInfo(Arrays.asList(new RecipientInfo(certId, c.getRecipient(), c.getRecipientProfile(), c.getClassification())));

                AddRoster addRosterRes =  hyperledgerAPI.addRoster(addRoster);

                if(addRosterRes != null && !StringUtils.isEmpty(addRosterRes.getTransactionId())) {
                    System.out.println(addRosterRes.getTransactionId());
                    c.setCstate("valid");
                    c.setTransactionId(addRosterRes.getTransactionId());
                    int success = issuerService.updateCertsInfo(c);
                    jsonString = "{\"success\":\"" + success + "\"}";
                }
            } catch (Exception e) {
               logger.error("[IssuerController] Error while broadcastCertificate", e);
            }
        }
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

    //https://github.com/tuhrig/Flying_Saucer_PDF_Generation
    /*
    @GetMapping("/generatePDF/{id}")
    public Response generatePDF(@PathVariable String id, HttpServletResponse response) {
        CertsInfo certsInfo = issuerService.getCertsInfo(id);

        Context context = new Context();
        context.setVariable("cert", certsInfo);

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

            String path = Constant.UPLOADED_FOLDER + "/certificate-" + CryptoUtil.getUUID() + ".pdf";
            OutputStream outputStream = new FileOutputStream(path);
            renderer.createPDF(outputStream);
            outputStream.close();

            byte[] data = FileUtils.readFileToByteArray(new File(path));
            String base64 = Base64.getEncoder().encodeToString(data);;
            return new Response(1, base64);

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }
    */

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

    // http://learningprogramming.net/java/spring-mvc/qrcode-in-spring-mvc-framework-and-spring-data-jpa/
    // https://www.callicoder.com/generate-qr-code-in-java-using-zxing/
    // https://www.callicoder.com/qr-code-reader-scanner-in-java-using-zxing/
    // https://stackoverflow.com/questions/52540790/linking-to-an-image-dynamically-with-thymeleaf-and-spring
    // https://stackoverflow.com/questions/40888883/spring-boot-thymeleaf-images
    // https://stackoverflow.com/questions/30129486/set-img-src-from-byte-array
    // https://www.roytuts.com/convert-pdf-to-image-file-using-java/
    // https://www.baeldung.com/convert-byte-array-to-input-stream
    // https://www.bootply.com/tGF9MEGbLM
//    @GetMapping("/qrcode/{id}")
//    public void qrcode(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
//        CertsInfo certsInfo = issuerService.getCertsInfo(id);
//        String content = JSON.toJSONString(certsInfo);
//        response.setContentType("image/png");
//        OutputStream outputStream = response.getOutputStream();
//        outputStream.write(Utils.getQRCodeImage(content, 200, 200));
//        outputStream.flush();
//        outputStream.close();
//    }

}
