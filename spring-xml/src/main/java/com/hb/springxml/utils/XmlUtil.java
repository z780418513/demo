package com.hb.springxml.utils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/25
 */
@Slf4j
public class XmlUtil {
    //XML文件头
    public static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";

    public static String javaBeanToXml(Object obj) {
        String xml = "";
        if (Objects.isNull(obj)) {
            return xml;
        }
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xml = xmlMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("javaBeanToXml error, obj={}, xml={}", obj, xml, e);
            return "";
        }
        // 添加xml文件头
        return XML_HEAD + xml;
    }
}
