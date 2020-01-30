package com.minhhung.connector;

import com.alibaba.fastjson.JSON;
import com.minhhung.model.hyperledger.AddRoster;
import com.minhhung.model.certificate.CertificateTemplate;
import com.minhhung.utils.OkHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HyperledgerAPIImpl implements HyperledgerAPI {

    private static Logger logger = LoggerFactory.getLogger(HyperledgerAPIImpl.class);

    @Value("${hyperledger.url}")
    private String url;

    private static final String ADD_CERTIFICATE_TEMPLATE = "/api/CertificateTemplate";
    private static final String GET_ALL_CERTIFICATE_TEMPLATE = "/api/CertificateTemplate";
    private static final String GET_CERTIFICATE_TEMPLATE_BY_ID = "/api/CertificateTemplate/";
    private static final String ADD_ROSTER = "/api/AddRoster";
    private static final String GET_ALL_TRANSACTION = "/api/queries/myTransactions";

    @Override
    public String createCertTemplate(CertificateTemplate certificateTemplate) {
        OkHttpUtil okHttpUtil = new OkHttpUtil();

        try {
            String postResponse = okHttpUtil.doPostRequest(url + ADD_CERTIFICATE_TEMPLATE, JSON.toJSONString(certificateTemplate));
            return postResponse;
        } catch (Exception e) {
            logger.error("[HyperledgerAPIImpl] createCertTemplate error", e);
        }

        return null;
    }

    @Override
    public AddRoster addRoster(AddRoster addRoster) {
        OkHttpUtil okHttpUtil = new OkHttpUtil();

        try {
            String postResponse = okHttpUtil.doPostRequest(url + ADD_ROSTER, JSON.toJSONString(addRoster));
            return JSON.parseObject(postResponse, AddRoster.class);
        } catch (Exception e) {
            logger.error("[HyperledgerAPIImpl] addRoster error", e);
        }

        return null;
    }

    @Override
    public List<CertificateTemplate> getAllCertificateTemplate() {
        OkHttpUtil okHttpUtil = new OkHttpUtil();

        try {
            String getResponse = okHttpUtil.doGetRequest(url + GET_ALL_CERTIFICATE_TEMPLATE);
            return JSON.parseArray(getResponse, CertificateTemplate.class);
        } catch (Exception e) {
            logger.error("[HyperledgerAPIImpl] getAllCertificateTemplate error", e);
        }

        return null;
    }

    @Override
    public CertificateTemplate getCertificateTemplateById(String id) {
        OkHttpUtil okHttpUtil = new OkHttpUtil();

        try {
            String getResponse = okHttpUtil.doGetRequest(url + GET_CERTIFICATE_TEMPLATE_BY_ID + id);
            return JSON.parseObject(getResponse, CertificateTemplate.class);
        } catch (Exception e) {
            logger.error("[HyperledgerAPIImpl] getCertificateTemplateById error", e);
        }

        return null;
    }

    @Override
    public List<AddRoster> getAllAddRosterTransaction() {
        OkHttpUtil okHttpUtil = new OkHttpUtil();

        try {
            String getResponse = okHttpUtil.doGetRequest(url + GET_ALL_TRANSACTION);
            return JSON.parseArray(getResponse, AddRoster.class);
        } catch (Exception e) {
            logger.error("[HyperledgerAPIImpl] getAllAddRosterTransaction error", e);
        }

        return null;
    }
}
