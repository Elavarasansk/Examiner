package org.web.vexamine.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.UserAuthorityInfo;
import org.web.vexamine.dao.repository.UserAuthorityInfoRepository;
import org.web.vexamine.dao.repository.UserAuthorityInfoRepository.UserDetails;
import org.web.vexamine.dao.vo.UserDetailsVo;

/**
 * The Class UserAuthorityService.
 */
@Service
public class UserAuthorityService {

	/** The user authority info repo. */
	@Autowired
	private UserAuthorityInfoRepository userAuthorityInfoRepo;

	/**
	 * Search name by role.
	 *
	 * @param userDetailsVo the user details vo
	 * @return the list
	 */
	public List<String> searchNameByRole(UserDetailsVo userDetailsVo) {
		Pageable pageable = PageRequest.of(VexamineTestConstants.INITIAL, userDetailsVo.getSuggestLimit());
		Page<UserAuthorityInfo> userList = userAuthorityInfoRepo.findByUserCredentialsUserNameContainingIgnoreCaseAndUserRoleType(userDetailsVo.getMailId(),userDetailsVo.getRole(),pageable);
		return userList.hasContent() ? userList.getContent().stream()
				.map(user -> user.getUserCredentials().getUserName())
				.collect(Collectors.toList()) 
				: Collections.emptyList() ;
	}

	/**
	 * Search mail id by role.
	 *
	 * @param userDetailsVo the user details vo
	 * @return the list
	 */
	public List<String> searchMailIdByRole(UserDetailsVo userDetailsVo) {
		Pageable pageable = PageRequest.of(VexamineTestConstants.INITIAL, userDetailsVo.getSuggestLimit());
		Page<UserDetails> userList = userAuthorityInfoRepo.findByUserCredentialsMailIdContainingIgnoreCaseAndUserRoleType(userDetailsVo.getMailId(),userDetailsVo.getRole(),pageable);
		return userList.hasContent() ? userList.getContent()
				.stream()
				.map(user -> user.getUserCredentials().getMailId())
				.collect(Collectors.toList()) 
				: Collections.emptyList() ;

	}
	
	/**
	 * Gets the user by mail id.
	 *
	 * @param mailId the mail id
	 * @return the user by mail id
	 */
	public UserAuthorityInfo getUserByMailId(String mailId) {
		return userAuthorityInfoRepo.findByUserCredentialsMailId(mailId);
	}

	/**
	 * Gets the user authority by user name list.
	 *
	 * @param userNameList the user name list
	 * @param role the role
	 * @return the user authority by user name list
	 */
	public List<UserAuthorityInfo> getUserAuthorityByUserNameList(List<String> userNameList,String role) {
		Optional<List<UserAuthorityInfo>> nameList = userAuthorityInfoRepo.findByUserCredentialsUserNameInAndUserRoleType(userNameList,role);
		return nameList.isPresent()  ? nameList.get() : Collections.emptyList() ; 
	}

	/**
	 * Gets the user authority by mail id list.
	 *
	 * @param mailIdList the mail id list
	 * @param role the role
	 * @return the user authority by mail id list
	 */
	public List<UserAuthorityInfo> getUserAuthorityByMailIdList(List<String> mailIdList,String role) {
		Optional<List<UserAuthorityInfo>> mailList = userAuthorityInfoRepo.findByUserCredentialsMailIdInAndUserRoleType(mailIdList,role);
		return mailList.isPresent()  ? mailList.get() : Collections.emptyList() ; 

	}

	/**
	 * Gets the count by role.
	 *
	 * @param role the role
	 * @return the count by role
	 */
	public Long getCountByRole(String role) {
		return userAuthorityInfoRepo.getCountByUserRoleType(role);
	}

	/**
	 * Gets the all name by role.
	 *
	 * @param userDetailsVo the user details vo
	 * @return the all name by role
	 */
	public Map<String, Object> getAllNameByRole(UserDetailsVo userDetailsVo) {
		Pageable pageable = PageRequest.of(userDetailsVo.getOffset(), userDetailsVo.getLimit());
		Page<UserAuthorityInfo> userList = userAuthorityInfoRepo.findByUserCredentialsUserNameContainingIgnoreCaseAndUserRoleType(userDetailsVo.getMailId(),userDetailsVo.getRole(),pageable);
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put(VexamineTestConstants.VALUE, userList.getContent());
		resultMap.put(VexamineTestConstants.COUNT, userList.getTotalElements());
		return resultMap;
	}

	/**
	 * Gets the all question answer.
	 *
	 * @param userDetailsVo the user details vo
	 * @return the all question answer
	 */
	public Page<UserDetails> getAllQuestionAnswer(UserDetailsVo userDetailsVo) {
		Pageable pageable = PageRequest.of(userDetailsVo.getOffset() , userDetailsVo.getLimit() );
		Page<UserDetails> userList = userAuthorityInfoRepo.findByUserCredentialsMailIdContainingIgnoreCaseAndUserRoleType(userDetailsVo.getMailId(),userDetailsVo.getRole(),pageable);
		return userList;
	}

}
