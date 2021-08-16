package org.web.vexamine.dao.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleVo {
    
    private Long id;

    private String type;

    private String description;
}
