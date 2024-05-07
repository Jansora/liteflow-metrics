package com.jansora.liteflow.controller.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: jansora 
 * @date: 2024-04-19 14:35:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request implements Serializable {

    /**
     * 授权码
     * 根据授权码获取
     * 调用权限, chainId 等数据
     *  等数据
     */
    String code;

    JsonNode data;



}
