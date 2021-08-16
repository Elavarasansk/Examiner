package org.web.vexamine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.ManagerCredit;
import org.web.vexamine.dao.entity.UserAuthorityInfo;
import org.web.vexamine.dao.entity.UserCredentials;
import org.web.vexamine.dao.repository.ManagerCreditRepository;
import org.web.vexamine.dao.repository.ManagerCreditRepository.ManagerCreditProjection;
import org.web.vexamine.dao.repository.UserAuthorityInfoRepository;
import org.web.vexamine.dao.vo.ManagerCreditVo;
import org.web.vexamine.utils.storage.CookieSessionStorage;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ManagerCreditService {

	@Autowired
	private ManagerCreditRepository managerCreditRepository;

	@Autowired
	private UserAuthorityInfoRepository userAuthorityInfoRepo;

	@Autowired
	private UserAuthorityService userAuthorityService;

	public ManagerCredit registerManagerCredit(UserAuthorityInfo userAuthorityInfo,Long purchasedCedit) {
		String sessionUser = CookieSessionStorage.get().getUserName();
		ManagerCredit managerCredit  = ManagerCredit.builder().build();
		managerCredit.setPurchasedCredits(purchasedCedit);
		managerCredit.setUserAuthorityInfo(userAuthorityInfo);
		managerCredit.setCreateUser(sessionUser);
		return managerCreditRepository.save(managerCredit);
	}

	public ManagerCredit addManagerCredit(ManagerCreditVo managerCreditVo) {
		String sessionUser = CookieSessionStorage.get().getUserName();
		UserAuthorityInfo userAuthorityInfo = userAuthorityService.getUserByMailId(managerCreditVo.getMailId());
		ManagerCredit managerCredit  = ManagerCredit.builder().build();
		BeanUtils.copyProperties(managerCreditVo, managerCredit);
		managerCredit.setUserAuthorityInfo(userAuthorityInfo);
		managerCredit.setCreateUser(sessionUser);
		return managerCreditRepository.save(managerCredit);
	}

	public ManagerCredit updateCredit(ManagerCreditVo managerCreditVo) {	
		String sessionUser = CookieSessionStorage.get().getUserName();
		ManagerCredit managerCredit= findByCreditByMailId(sessionUser);
		long credits = Objects.isNull(managerCredit.getUsedCredits()) ?  0 : managerCredit.getUsedCredits();
		managerCredit.setUsedCredits(Math.addExact(credits,managerCreditVo.getUsedCredits()));
		managerCredit.setUpdateUser(sessionUser);
		return managerCreditRepository.save(managerCredit);
	}

	public ManagerCredit addCredit(ManagerCreditVo managerCreditVo) {	
		String sessionUser = CookieSessionStorage.get().getUserName();
		ManagerCredit managerCredit= findByCreditByMailId(sessionUser);
		long credits = Objects.isNull(managerCredit.getUsedCredits()) ?  0 : managerCredit.getUsedCredits();
		managerCredit.setUsedCredits(Math.subtractExact(credits,managerCreditVo.getUsedCredits()));
		managerCredit.setUpdateUser(sessionUser);
		return managerCreditRepository.save(managerCredit);
	}

	public List<ManagerCredit> addCredit(Map<String, Long> dataMap) {	
		Set<String> keysSet = dataMap.keySet();
		List<ManagerCredit> creditsList = findAllByCreditByMailId(new ArrayList(keysSet));		
		creditsList.stream().forEach(managerCredit -> { 
			long credits = Objects.isNull(managerCredit.getUsedCredits()) ?  0 : managerCredit.getUsedCredits();
			if(credits == 0) {
				return ;
			}
			String managerMailId = managerCredit.getUserAuthorityInfo().getUserCredentials().getMailId();
			long count = dataMap.get(managerMailId);
			managerCredit.setUsedCredits(Math.subtractExact(credits,count));
		});
		return managerCreditRepository.saveAll(creditsList);
	}

	public ManagerCredit findByCreditByMailId(String mailId) {
		return managerCreditRepository.findByUserAuthorityInfoUserCredentialsMailId(mailId);
	}

	public ManagerCredit findByCreditByMailId() {
		String sessionUser = CookieSessionStorage.get().getUserName();
		ManagerCredit managerCredit = findByCreditByMailId(sessionUser);
		if(Objects.isNull(managerCredit)) {
			return ManagerCredit.builder().purchasedCredits(0L).usedCredits(0L).build();
		}
		return ManagerCredit.builder()
				.purchasedCredits(managerCredit.getPurchasedCredits())
				.usedCredits(managerCredit.getUsedCredits() == null ?  0 : managerCredit.getUsedCredits()).build();
	}

	public List<ManagerCreditProjection> findAllByCreditByMailId(List<String> mailIdList) {
		return managerCreditRepository.findByUserAuthorityInfoUserCredentialsMailIdIn(mailIdList);
	}

	public ManagerCredit updatePurchasedCredits(ManagerCreditVo managerCreditVo) throws Exception {	
		String sessionUser = CookieSessionStorage.get().getUserName();
		ManagerCredit managerCredit= findByCreditByMailId(managerCreditVo.getMailId());		
		long usedCreditsCredits = Objects.isNull(managerCredit.getUsedCredits()) ?  0 : managerCredit.getUsedCredits();
		long newPurchasedCredit = managerCreditVo.getPurchasedCredits();
		if(usedCreditsCredits > newPurchasedCredit  ) {
			log.error("  Purchased Credits should be higher then the Used Credits !!! ");
			throw new Exception(" Purchased Credits should be higher then the Used Credits !!! ");
		}		
		managerCredit.setPurchasedCredits(newPurchasedCredit);
		managerCredit.setUpdateUser(sessionUser);
		return managerCreditRepository.save(managerCredit);
	}


	public void mapCandidateToManager(UserAuthorityInfo candidateAuthInfo) {
		if(!candidateAuthInfo.getUserRole().getType().equals("CANDIDATE")) {
			return;
		}
		String managerMailId = CookieSessionStorage.get().getUserName();
		ManagerCredit managerCredit = managerCreditRepository.findByUserAuthorityInfoUserCredentialsMailId(managerMailId);

		Set<UserAuthorityInfo> candidateList = managerCredit.getCandidateList();
		candidateList.add(candidateAuthInfo);
		managerCredit.setCandidateList(candidateList);
		managerCreditRepository.save(managerCredit);
	}

	public void mapCandidateToManagerBatch(List<UserAuthorityInfo> candidateAuthInfoList) {
		candidateAuthInfoList = candidateAuthInfoList.stream().filter(data -> data.getUserRole().getType().equals("CANDIDATE")).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(candidateAuthInfoList)) {
			return;
		}
		String managerMailId = CookieSessionStorage.get().getUserName();
		ManagerCredit managerCredit = managerCreditRepository.findByUserAuthorityInfoUserCredentialsMailId(managerMailId);

		Set<UserAuthorityInfo> candidateList = managerCredit.getCandidateList();
		candidateList.addAll(candidateAuthInfoList);
		managerCredit.setCandidateList(candidateList);
		managerCreditRepository.save(managerCredit);
	}

	public List<String> searchCandidate() {
		String managerMailId = CookieSessionStorage.get().getUserName();
		ManagerCredit managerCredit = findByCreditByMailId(managerMailId);
		Set<UserAuthorityInfo> resultSet =  managerCredit.getCandidateList();
		return resultSet.stream().map(UserAuthorityInfo::getUserCredentials)
				.map(UserCredentials::getMailId).collect(Collectors.toList());
	}

	public Map<String, Object>  getAllCandidate() {
		String managerMailId = CookieSessionStorage.get().getUserName();
		ManagerCredit managerCredit = findByCreditByMailId(managerMailId);
		Map<String,Object> resultMap = new HashMap<>();
		Set<UserAuthorityInfo> resultSet =  managerCredit.getCandidateList();
		resultMap.put(VexamineTestConstants.VALUE, resultSet);
		resultMap.put(VexamineTestConstants.COUNT, resultSet.size());
		return resultMap;
	}

}