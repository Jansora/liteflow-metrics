package com.jansora.liteflow.context;


import com.fasterxml.jackson.databind.JsonNode;
import com.jansora.liteflow.insight.Statistic;
import com.jansora.liteflow.insight.TraceGraph;
import com.jansora.liteflow.insight.TraceNode;
import com.jansora.liteflow.utils.JsonHelper;
import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import com.yomahub.liteflow.context.ContextBean;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.FlowBus;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: jansora 
 * @date: 2024-04-19 11:15:49
 */
@ContextBean("ctx")
public class FlowContext {

    private static final String _ctx_data_key = "_data";

    private final ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    /**
     * 业务流水号 (唯一 id)
     */
    @Getter
    private final  String _businessSerialNumber;

    /**
     * 项目空间,  namespace
     */
    @Getter
    private final  String _namespace;

    /**
     * 调用链 id
     */
    @Getter
    private final  String  _chainId;

    /**
     * 调用链图
     */
    @Getter
    private final TraceGraph _traceGraph;

    public FlowContext(JsonNode ctx, String _businessSerialNumber, String _namespace, String _chainId, TraceGraph _traceGraph) {
        this.data.put(_ctx_data_key, ctx);
        this._businessSerialNumber = _businessSerialNumber;
        this._namespace = _namespace;
        this._chainId = _chainId;
        this._traceGraph = _traceGraph;
    }

    /**
     *
     * @param request
     * @return
     */
    public static FlowContext valueOf(JsonNode request, String chainId) {


        // 临时代码
        if (FlowBus.getChain("chain1") == null) {
            
            LiteFlowChainELBuilder.createChain().setChainId("sub1").setEL(
               "PAR(A, B, C)"
            ).build();
            LiteFlowChainELBuilder.createChain().setChainId("sub2").setEL(
                    "SER(C, B)"
            ).build();
            LiteFlowChainELBuilder.createChain().setChainId("chain1").setEL(
                    "SER(A, " +
                            "PAR(" +
                            "SER(B, " +
                            "sub1" +
                            "), " +
                            "sub2, " +
                            "SER(A, abc)" +
                            "), " +
                            "B" +
                            ")"
            ).build();
        }


        String businessSerialNumber = UUID.randomUUID().toString();
        String namespace = "placeholder";
        TraceGraph traceGraph = TraceGraph.valueOf(chainId);
        return new FlowContext(request, businessSerialNumber, namespace, chainId, traceGraph);
    }


    public Object set(String key, Object value) {
        return this.data.put(key, value);
    }
    public Object get(String key) {
        return this.data.get(key);
    }
    public Object getOrDefault(String key, Object value) {
        return this.data.getOrDefault(key, value);
    }

    public JsonNode ctx() {
        return (JsonNode) this.data.get(_ctx_data_key);
    }

    public JsonNode snapshot() {
        return JsonHelper.valueToTree(this.data);
    }


    /**
     * 规则执行时, 根据运行时的 component 获取到对应的 node 信息
     * @param component
     * @return
     */
    public TraceNode getTraceNode(NodeComponent component) {
        return this._traceGraph.getNodeCache().get(component.getRefNode().hashCode());
    }


    public static void addStatistic(NodeComponent component) {
        FlowContext ctx = component.getContextBean(FlowContext.class);

        Statistic statistic = new Statistic();

        String statisticId = String.valueOf(component.hashCode());
        statistic.setId(statisticId);
        statistic.setInput(ctx.snapshot());
        TraceNode node = ctx.getTraceNode(component);
        node.addStatistic(statistic);
    }

    public static void updateStatistic(NodeComponent component) {
        FlowContext ctx = component.getContextBean(FlowContext.class);
        String statisticId = String.valueOf(component.hashCode());
        TraceNode node = ctx.getTraceNode(component);
        node.updateStatistic(statisticId, ctx.snapshot());
    }
}
