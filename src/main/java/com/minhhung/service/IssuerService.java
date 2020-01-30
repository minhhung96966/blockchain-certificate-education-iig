package com.minhhung.service;

import com.minhhung.model.PageModel;
import com.minhhung.model.certificate.CertificateTemplate;
import com.minhhung.model.certificate.CertsInfo;
import com.minhhung.model.hyperledger.AddRoster;
import com.minhhung.persistence.BaseMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssuerService {

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	/**
	 * updateCertsInfo
	 * @param CertsInfo
	 * @return
	 */
	public int updateCertsInfo(CertsInfo CertsInfo) {
		String id = CertsInfo.getId();
		return baseMongoTemplate.updateEntity(id, CertsInfo, CertsInfo.class);
	}

	/**
	 * getCertsInfoPage
	 * @param page
	 * @return
	 */
	public PageModel<CertsInfo> getCertsInfoPage(PageModel<CertsInfo> page) {
		page = baseMongoTemplate.getListPage(page, CertsInfo.class);
		return page;
	}

	/**
	 * getCertsInfo
	 * @param id
	 * @return
	 */
	public CertsInfo getCertsInfo(String id) {
		return baseMongoTemplate.getEntity("id", id, CertsInfo.class);
	}

	/**
	 * getCertsInfoByRevocationAddress
	 * @param revocationAddress
	 * @return
	 */
	public CertsInfo getCertsInfoByRevocationAddress(String revocationAddress) {
		return baseMongoTemplate.getEntity("openbadges.badge.revocationClaim.revocationAddress", revocationAddress,
				CertsInfo.class);
	}

	/**
	 * getCertsInfoList
	 * @param ids
	 * @return
	 */
	public List<CertsInfo> getCertsInfoList(String ids) {
		return baseMongoTemplate.getEntity(ids, CertsInfo.class);
	}

	/**
	 * getCertsInfoListByTranId
	 * @param transactionId
	 * @return
	 */
	public List<CertsInfo> getCertsInfoListByTranId(String transactionId) {
		return baseMongoTemplate.getEntityList("transactionId", transactionId, CertsInfo.class);
	}

	/**
	 * saveCertsInfoBatch
	 * @param CertsInfoList
	 * @return
	 */
	public int saveCertsInfoBatch(List<CertsInfo> CertsInfoList) {
		return baseMongoTemplate.saveEntityBatch(CertsInfoList);
	}
}
