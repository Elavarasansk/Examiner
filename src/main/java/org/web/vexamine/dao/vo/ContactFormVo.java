package org.web.vexamine.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new contact form vo
 *
 * @param name
 * @param email
 *            of the sender
 * @param subject
 *            of the message
 * @param message
 *            is actual message
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactFormVo {

	private String name;

	private String email;

	private String subject;

	private String message;

}
