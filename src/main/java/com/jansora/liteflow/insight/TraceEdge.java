package com.jansora.liteflow.insight;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: jansora 
 * @date: 2024-04-19 10:10:50
 */
// 边类
@Data
public class TraceEdge implements Serializable {

    Integer id;

    String label;

    Integer sourceId;

    Integer targetId;

}
