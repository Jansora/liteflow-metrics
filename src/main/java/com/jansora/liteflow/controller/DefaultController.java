package com.jansora.liteflow.controller;

import com.jansora.liteflow.context.FlowContext;
import com.jansora.liteflow.controller.request.Request;
import com.jansora.liteflow.insight.TraceGraph;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: jansora 
 * @date: 2024-04-19 14:37:20
 */
@Slf4j
@AllArgsConstructor
@RestController
public class DefaultController {

    @Autowired
    FlowExecutor flowExecutor;

    @PutMapping("/test")
    public TraceGraph test(@RequestBody Request request) throws Exception {

        return run(request);

    }


    public TraceGraph run(Request request) {

        log.info("开始执行");
        FlowContext context = FlowContext.valueOf(request.getData(), "chain1");
        LiteflowResponse response = flowExecutor.execute2RespWithRid("chain1", null, context.get_businessSerialNumber(), context);


        if (response.isSuccess()){
            log.info("执行成功");
        }
        else{
            log.info("执行失败");
        }


        return context.get_traceGraph();

    }

}
