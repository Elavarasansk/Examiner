package org.web.vexamine.dao.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerCreditVo {

	private Long id;

	private String company;

	private Long userAuthId;

	private String mailId;

	private List<Integer> candidateList;

	private Long usedCredits;

	private Long purchasedCredits;

	private Integer offset;

	private Integer limit;



}