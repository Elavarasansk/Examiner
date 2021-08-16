package org.web.vexamine.dao.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.ManagerCredit;

/**
 * The Interface ManagerCreditRepository.
 */
@Transactional
public interface ManagerCreditRepository extends JpaRepository<ManagerCredit, Long>, JpaSpecificationExecutor<ManagerCredit> {


	public ManagerCredit findByUserAuthorityInfoUserCredentialsMailId(String mailId);

	public ManagerCredit findByUserAuthorityInfoId(long authId);

	public List<ManagerCreditProjection> findByUserAuthorityInfoUserCredentialsMailIdIn(List<String> mailIdList);	

	interface ManagerCreditProjection {
		
		Long getId();

		Long getPurchasedCredits();

		Long getUsedCredits();	

		UserAuthorityInfo getUserAuthorityInfo();    	

		interface UserAuthorityInfo{
			UserCredentials getUserCredentials();
			interface UserCredentials {
				String getMailId();
			}
		}
	}

}