package org.web.vexamine.dao.repository.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.QuestionBank;
import org.web.vexamine.dao.vo.QuestionBankFilterVo;

/**
 * The Class QuestionBankSpecification.
 */
@Transactional
public class QuestionBankSpecification implements Specification<QuestionBank> {

	private QuestionBankFilterVo qbFilter;

	/**
	 * Instantiates a new question bank specification.
	 *
	 * @param qbFilter the qb filter
	 */
	public QuestionBankSpecification(QuestionBankFilterVo qbFilter) {
		super();
		this.qbFilter = qbFilter;
	}

	@Override
	public Predicate toPredicate(Root<QuestionBank> qbRoot, CriteriaQuery<?> criteriaQuery, CriteriaBuilder critbuilder) {
		Predicate predicate = critbuilder.conjunction();
		List<Expression<Boolean>> expressions = predicate.getExpressions();
		
		Path<Object> categoryRoot = qbRoot.get("testCategory");
		
		if(!StringUtils.isEmpty(qbFilter.getCategory())) {
			expressions.add(critbuilder.like(critbuilder.upper(categoryRoot.get("category")), containsJoiner(qbFilter.getCategory())));
		}
		if(!StringUtils.isEmpty(qbFilter.getSubCategory())) {
			expressions.add(critbuilder.like(critbuilder.upper(categoryRoot.get("subCategory")), containsJoiner(qbFilter.getSubCategory())));
		}
		if(!StringUtils.isEmpty(qbFilter.getQuestionBankName())) {
			expressions.add(critbuilder.like(critbuilder.upper(qbRoot.get("questionBankName")), containsJoiner(qbFilter.getQuestionBankName())));
		}
		return predicate;
	}
	
	/**
	 * Contains joiner.
	 *
	 * @param value the value
	 * @return the string
	 */
	private String containsJoiner(String value) {
		return String.format("%%%s%%", value.toUpperCase());
	}

}