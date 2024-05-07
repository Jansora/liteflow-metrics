package com.jansora.liteflow.insight;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: jansora 
 * @date: 2024-04-19 10:56:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistic implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime endTime;

    String id;

    /**
     * 入参
     */
    JsonNode input;

    /**
     * 返回值
     */
    JsonNode output;

    /**
     * 耗时 (ms)
     */
    double cost;

    public void setInput(JsonNode input) {
        this.input = input;
        this.startTime = LocalDateTime.now();
    }

    public void setOutput(JsonNode output) {
        this.output = output;
        this.endTime = LocalDateTime.now();
        this.cost = Duration.between(this.startTime, this.endTime).toNanos() / 1.0E9;
    }
}
