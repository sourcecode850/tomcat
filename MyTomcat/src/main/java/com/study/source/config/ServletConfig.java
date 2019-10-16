package com.study.source.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @date:2019/10/15 23:23
 **/
@Data
@AllArgsConstructor
public class ServletConfig {

    private String urlMapping;
    private String name;
    private String clazz;

}
