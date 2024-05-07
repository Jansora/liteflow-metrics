package com.jansora.liteflow.insight;

import com.jansora.liteflow.context.FlowContext;
import com.yomahub.liteflow.aop.ICmpAroundAspect;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: jansora 
 * @date: 2024-04-18 16:17:35
 */
@Slf4j
@Component
public class NodeAspect implements ICmpAroundAspect {

    @Override
    public void beforeProcess(NodeComponent component) {
        FlowContext.addStatistic(component);
    }

    @Override
    public void afterProcess(NodeComponent component) {
        FlowContext.updateStatistic(component);

    }

    @Override
    public void onSuccess(NodeComponent cmp) {
        //do sth
    }

    @Override
    public void onError(NodeComponent cmp, Exception e) {
        //do sth
    }
}

