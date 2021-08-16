package org.web.vexamine.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.dao.repository.UserAuthorityInfoRepository.UserDetails;
import org.web.vexamine.dao.vo.UserDetailsVo;
import org.web.vexamine.service.UserAuthorityService;

/**
 * The Class UserDetailsController.
 */
@RestController
@RequestMapping("/user/details/")
public class UserDetailsController {

	@Autowired
	private UserAuthorityService userAuthorityService;

	/**
	 * Search name by role.
	 *
	 * @param userDetailsVo the user details vo
	 * @return the list
	 */
	@PostMapping("/search/name")
	public List<String> searchNameByRole(@RequestBody UserDetailsVo userDetailsVo) {
		return userAuthorityService.searchNameByRole(userDetailsVo);
	}

	/**
	 * Search mail id by role.
	 *
	 * @param userDetailsVo the user details vo
	 * @return the list
	 */
	@PostMapping("/search/mailid")
	public List<String> searchMailIdByRole(@RequestBody UserDetailsVo userDetailsVo) {
		return userAuthorityService.searchMailIdByRole(userDetailsVo);
	}

	/**
	 * Gets the count by role.
	 *
	 * @param role the role
	 * @return the count by role
	 */
	@GetMapping("/count/{role}")
	public Long getCountByRole(@PathVariable(value="role") String role) {
		return userAuthorityService.getCountByRole(role);
	}
	
	/**
	 * Gets the all name by role.
	 *
	 * @param userDetailsVo the user details vo
	 * @return the all name by role
	 */
	@PostMapping("/find/all/name")
	public Map<String, Object> getAllNameByRole(@RequestBody UserDetailsVo userDetailsVo){
		return userAuthorityService.getAllNameByRole(userDetailsVo);
	}
	
	/**
	 * Gets the all mail id by role.
	 *
	 * @param userDetailsVo the user details vo
	 * @return the all mail id by role
	 */
	@PostMapping("/find/all/mailid")
	public Page<UserDetails> getAllMailIdByRole(@RequestBody UserDetailsVo userDetailsVo){
		return userAuthorityService.getAllQuestionAnswer(userDetailsVo);
	}


}
