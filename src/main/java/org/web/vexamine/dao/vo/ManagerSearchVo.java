package org.web.vexamine.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new manager search vo.
 *
 * @param id the id
 * @param company the company
 * @param mailId the mail id
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerSearchVo {

  private Long id = 2L;
  
  private String company;
  
  private String mailId;
  
}
