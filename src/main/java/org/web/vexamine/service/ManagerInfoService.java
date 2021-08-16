package org.web.vexamine.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.ManagerInfo;
import org.web.vexamine.dao.entity.QuestionBank;
import org.web.vexamine.dao.entity.UserAuthorityInfo;
import org.web.vexamine.dao.repository.ManagerCreditRepository.ManagerCreditProjection;
import org.web.vexamine.dao.repository.ManagerInfoRepository;
import org.web.vexamine.dao.repository.ManagerInfoRepository.ManagerDetails;
import org.web.vexamine.dao.repository.specification.ManagerInfoSpecification;
import org.web.vexamine.dao.vo.ManagerVo;
import org.web.vexamine.dao.vo.UserRegistrationForm;
import org.web.vexamine.utils.storage.CookieSessionStorage;

/**
 * The Class ManagerInfoService.
 */
@Service
public class ManagerInfoService {

	/** The manager info repository. */
	@Autowired
	private ManagerInfoRepository managerInfoRepository;

	/** The question bank service. */
	@Autowired
	private QuestionBankService questionBankService;

	/** The user authority service. */
	@Autowired
	private UserAuthorityService userAuthorityService;

	@Autowired
	private ManagerCreditService managerCreditService;


	/**
	 * Register manger info.
	 *
	 * @param registrationForm the registration form
	 * @param userAuthorityInfo the user authority info
	 * @return the list
	 */
	public List<ManagerInfo> registerMangerInfo(UserRegistrationForm registrationForm,UserAuthorityInfo userAuthorityInfo) {
		String sessionUser  = CookieSessionStorage.get().getUserName(); 

		List<QuestionBank> questionBankList = questionBankService.getQuestionBankList(registrationForm.getQuestionBankList());
		if(CollectionUtils.isEmpty(questionBankList)) {
			return managerInfoRepository.saveAll(Arrays.asList(
					ManagerInfo.builder()
					.createUser(sessionUser)
					.updateUser(sessionUser)
					.userAuthorityInfo(userAuthorityInfo)
					.company(registrationForm.getCompany())
					.build()));
		}
		List<ManagerInfo> managerInfoList = questionBankList.stream().map(result ->
		ManagerInfo.builder()
		.createUser(sessionUser)
		.updateUser(sessionUser)
		.questionBank(result)
		.userAuthorityInfo(userAuthorityInfo)
		.company(registrationForm.getCompany())
		.build()).collect(Collectors.toList());
		return managerInfoRepository.saveAll(managerInfoList);
	}

	/**
	 * Adds the all manger info.
	 *
	 * @param managerVo the manager vo
	 * @return the list
	 */
	public List<ManagerInfo> addAllMangerInfo(ManagerVo managerVo) {
		UserAuthorityInfo userAuthorityInfo = userAuthorityService.getUserByMailId(managerVo.getMailId());
		String sessionUser  = !ObjectUtils.isEmpty(CookieSessionStorage.get()) ? 
				CookieSessionStorage.get().getUserName() : StringUtils.EMPTY;	
				List<QuestionBank> questionBankList = questionBankService.getQuestionBankList(managerVo.getQuestionBankList());
				List<ManagerInfo> managerInfoList = questionBankList.stream().map(result ->
				ManagerInfo.builder()
				.createUser(sessionUser)
				.updateUser(sessionUser)
				.questionBank(result)
				.userAuthorityInfo(userAuthorityInfo)
				.company(managerVo.getCompany())
				.build()).collect(Collectors.toList());
				return managerInfoRepository.saveAll(managerInfoList);
	}


	/**
	 * Find all manager.
	 *
	 * @return the list
	 */
	public List<ManagerInfo> findAllManager() {
		return managerInfoRepository.findAll();
	}



	/**
	 * Delete by manger by id.
	 *
	 * @param id the id
	 */
	public void deleteByMangerById(Long id) {
		managerInfoRepository.deleteById(id);
	}

	/**
	 * Delete by manger mail id.
	 *
	 * @param mailId the mail id
	 */
	public void deleteByMangerMailId(String mailId) {
		managerInfoRepository.deleteByUserAuthorityInfoUserCredentialsMailId(mailId);
	}

	/**
	 * Gets the manager info count.
	 *
	 * @return the manager info count
	 */
	public Long getManagerInfoCount() {
		return managerInfoRepository.count();
	}

	/**
	 * Gets the count by mail id.
	 *
	 * @param mailId the mail id
	 * @return the count by mail id
	 */
	public Long getCountByMailId(String mailId) {
		return managerInfoRepository.countByUserAuthorityInfoUserCredentialsMailId(mailId);
	}


	/**
	 * Find by mail id.
	 *
	 * @param mailId the mail id
	 * @return the list
	 */
	public List<String> findByMailId(String mailId) {
		return managerInfoRepository.findByUserAuthorityInfoUserCredentialsMailId(mailId)
				.stream().filter(questionbank -> !ObjectUtils.isEmpty(questionbank.getQuestionBank()))
				.map(questionbank -> questionbank.getQuestionBank().getQuestionBankName()).collect(Collectors.toList());
	}


	/**
	 * Find all question bank.
	 *
	 * @return the list
	 */
	public List<String> findAllQuestionBank() {
		String mailId  = !ObjectUtils.isEmpty(CookieSessionStorage.get()) ? 
				CookieSessionStorage.get().getUserName() : StringUtils.EMPTY;
				if(StringUtils.isEmpty(mailId)) {
					return Collections.emptyList();
				}
				return managerInfoRepository.findByUserAuthorityInfoUserCredentialsMailId(mailId)
						.stream().filter(data ->  Objects.nonNull(data.getQuestionBank()))
						.map(data ->  data.getQuestionBank().getQuestionBankName() )
						.collect(Collectors.toList());
	}


	/**
	 * Find question bank by manager in.
	 *
	 * @param mailIdList the mail id list
	 * @return the map
	 */
	public Map<String, String> findQuestionBankByManagerIn(List<String> mailIdList) {
		List<ManagerInfo> managerList = managerInfoRepository.findDistinctByUserAuthorityInfoUserCredentialsMailIdIn(mailIdList);
		Map<String,String> dataMap = new HashMap<>();
		managerList.stream().forEach(result -> dataMap.put(result.getUserAuthorityInfo().getUserCredentials().getMailId(), result.getCompany()) );
		return dataMap;
	}


	/**
	 * Search question bank.
	 *
	 * @param managerVo the manager vo
	 * @return the list
	 */
	public List<String> searchQuestionBank(ManagerVo managerVo) {
		Pageable pageable = PageRequest.of(VexamineTestConstants.INITIAL, managerVo.getSuggestLimit());
		List<ManagerDetails>  managerList = managerInfoRepository.findByQuestionBankQuestionBankNameContainingIgnoreCase(managerVo.getQuestionBankName(), pageable);
		return managerList.stream().map(result -> result.getQuestionBank().getQuestionBankName()).collect(Collectors.toList());
	}

	/**
	 * Search question bank by manager.
	 *
	 * @param managerVo the manager vo
	 * @return the list
	 */
	public List<String> searchQuestionBankByManager(ManagerVo managerVo) {
		Pageable pageable = PageRequest.of(VexamineTestConstants.INITIAL, managerVo.getSuggestLimit());
		List<ManagerDetails>  managerList = 
				managerInfoRepository.findByUserAuthorityInfoUserCredentialsMailIdAndQuestionBankQuestionBankNameContainingIgnoreCase(managerVo.getMailId(),managerVo.getQuestionBankName(), pageable);
		return managerList.stream().map(result -> result.getQuestionBank().getQuestionBankName()).collect(Collectors.toList());
	}

	/**
	 * Do all manager details search.
	 *
	 * @param managerVo the manager vo
	 * @return the map
	 */
	public Map<String, Object> doAllManagerDetailsSearch(ManagerVo managerVo) {    
		ManagerInfoSpecification spec = new ManagerInfoSpecification(managerVo);
		List<ManagerInfo> returnList = managerInfoRepository.findAll(spec);
		List<Map<String, Object>> dataList = returnList.stream().map(summary->{	
			Map<String,Object> resultMap = new HashMap<>(); 
			resultMap.put("mailId", summary.getUserAuthorityInfo().getUserCredentials().getMailId());
			resultMap.put("company", summary.getCompany());
			return resultMap;
		}).collect(Collectors.toList());	
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put(VexamineTestConstants.VALUE, dataList);
		resultMap.put(VexamineTestConstants.COUNT,dataList.size());
		return resultMap;


	}

	/**
	 * Gets the manager summary.
	 *
	 * @param managerVo the manager vo
	 * @return the manager summary
	 */
	public List<Map<String, Object>> getManagerSummary(ManagerVo managerVo) {
		ManagerInfoSpecification managerInfoSpecification= new ManagerInfoSpecification(managerVo);
		List<ManagerInfo> dataList = managerInfoRepository.findAll(managerInfoSpecification);
	    dataList = dataList.stream().filter(distinctByKey(key->key.getUserAuthorityInfo().getUserCredentials().getMailId())).collect(Collectors.toList());
		List<String> mailList = dataList.stream().map(key->key.getUserAuthorityInfo().getUserCredentials().getMailId()).collect(Collectors.toList());
		List<ManagerCreditProjection> creditList = managerCreditService.findAllByCreditByMailId(mailList);
		Map<String, ManagerCreditProjection> creditMap = creditList.stream().collect(Collectors.toMap(key->key.getUserAuthorityInfo().getUserCredentials().getMailId(), key->key));
		List<Map<String, Object>> finalList = new ArrayList<>();
		dataList.stream().forEach(record->{
			Map<String, Object> dataMap = new HashMap<>();
			String mailId = record.getUserAuthorityInfo().getUserCredentials().getMailId();
			dataMap.put("mailId", mailId);
			dataMap.put("company", record.getCompany());
			if(creditMap.containsKey(mailId)) {
				ManagerCreditProjection credit = creditMap.get(mailId);
				dataMap.put("purchasedCredits", credit.getPurchasedCredits());
				dataMap.put("usedCredits",credit.getUsedCredits());
				dataMap.put("remainingCredits",  Math.subtractExact(Objects.isNull(credit.getPurchasedCredits()) ? 0 : credit.getPurchasedCredits(), Objects.isNull(credit.getUsedCredits()) ? 0 : credit.getUsedCredits()));
			}
			finalList.add(dataMap);
		});
		return finalList;
	}
	
  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
  {
      Map<Object, Boolean> map = new ConcurrentHashMap<>();
      return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }

}
