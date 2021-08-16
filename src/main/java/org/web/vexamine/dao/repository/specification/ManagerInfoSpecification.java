package org.web.vexamine.dao.repository.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.ManagerInfo;
import org.web.vexamine.dao.vo.ManagerVo;

/**
 * The Class ManagerInfoSpecification.
 */
@Transactional
public class ManagerInfoSpecification  implements Specification<ManagerInfo> {

	private ManagerVo managerVo;

	/**
	 * Instantiates a new manager info specification.
	 *
	 * @param managerVo the manager vo
	 */
	public ManagerInfoSpecification(ManagerVo managerVo) {
		super();
		this.managerVo = managerVo;
	}

	@Override
	public Predicate toPredicate(Root<ManagerInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder critbuilder) {
		Predicate predicate = critbuilder.conjunction();
		List<Expression<Boolean>> expressions = predicate.getExpressions();
		Expression<String> mailRoot = root.get("userAuthorityInfo").get("userCredentials").get("mailId");
		Expression<String> companyRoot = root.get("company");
	
		if(StringUtils.isNotEmpty(managerVo.getMailId())) {
			expressions.add(critbuilder.equal(mailRoot, managerVo.getMailId()));
		}
		if(StringUtils.isNotEmpty(managerVo.getCompany())) {
			expressions.add(critbuilder.equal(companyRoot, managerVo.getCompany()));
		}		
		expressions.add(critbuilder.equal(root.get("userAuthorityInfo").get("userRole").get("type"), VexamineTestConstants.MANAGER));
		criteriaQuery.distinct(true);
		return predicate;
	}

}
