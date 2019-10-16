package com.study.source.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @date:2019/10/15 23:24
 **/
public class ServletConfigMapping {

    private static List<ServletConfig> configs = new ArrayList<ServletConfig>();

    static {
        configs.add(new ServletConfig("/luban", "luban", "com.study.source.servlet.LuBanServlet"));
    }


}
