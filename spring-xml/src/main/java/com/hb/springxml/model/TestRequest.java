package com.hb.springxml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/25
 */
@Data
@JacksonXmlRootElement(localName = "req")
public class TestRequest {
    //手机号
    @JacksonXmlProperty(localName = "tel")
    private String tel;

    //活动商品ID
    @JacksonXmlProperty(localName = "activityId")
    private String productId;

    @JacksonXmlProperty(localName = "status")
    private Integer status;
}
