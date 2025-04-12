package com.zbinyds.localmessage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zbinyds@126.com
 * @since 2025-04-04 19:13
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocalMessageDTO {
    private String className;

    private String methodName;

    private String paramNames;

    private String args;
}
