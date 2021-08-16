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
import org.web.vexamine.dao.entity.QuestionAnswer;
import org.web.vexamine.dao.vo.QaFilterVo;

/**
 * The Class QuestionAnswerSpecification.
 */
@Transactional
public class QuestionAnswerSpecification implements Specification<QuestionAnswer> {

	private QuestionAnswer filter;

	private QaFilterVo qaFilter;

	/**
	 * Instantiates a new question answer specification.
	 *
	 * @param qaFilterVo the qa filter vo
	 */
	public QuestionAnswerSpecification(QaFilterVo qaFilterVo) {
		super();
		this.qaFilter = qaFilterVo;
	}

	@Override
	public Predicate toPredicate(Root<QuestionAnswer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder critbuilder) {
		Predicate predicate = critbuilder.conjunction();
		List<Expression<Boolean>> expressions = predicate.getExpressions();
		
		Path<Object> qbRoot = root.get("questionBank");
		Path<Object> categoryRoot = qbRoot.get("testCategory");
		 
		if(!StringUtils.isEmpty(qaFilter.getCategory())) {
			expressions.add(critbuilder.equal(critbuilder.upper(categoryRoot.get("category")), qaFilter.getCategory()));
		}
		if(!StringUtils.isEmpty(qaFilter.getSubCategory())) {
			expressions.add(critbuilder.equal(critbuilder.upper(categoryRoot.get("subCategory")), qaFilter.getSubCategory()));
		}
		if(!StringUtils.isEmpty(qaFilter.getQuestionBankName())) {
			expressions.add(critbuilder.equal(critbuilder.upper(qbRoot.get("questionBankName")), qaFilter.getQuestionBankName()));
		}
		if(qaFilter.getQuestionType() != null) {
			expressions.add(critbuilder.equal(root.get("questionType"), qaFilter.getQuestionType()));
		}
		if(!StringUtils.isEmpty(qaFilter.getQuestion())) {
			expressions.add(critbuilder.like(critbuilder.lower(root.get("question")), containsJoiner(qaFilter.getQuestion())));
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
		return String.format("%%%s%%", value.toLowerCase());
	}

}