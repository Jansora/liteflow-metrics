package com.jansora.liteflow.insight;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.flow.element.Condition;
import com.yomahub.liteflow.flow.element.Executable;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: jansora 
 * @date: 2024-04-19 10:27:46
 */
@Data
public class TraceGraph implements Serializable {

    @JsonIgnore
    private transient Map<Integer, TraceNode> nodeCache;

    @JsonIgnore
    private transient Map<Integer, TraceEdge> edgeCache;

    private List<TraceNode> nodes;
    private List<TraceEdge> edges;

    public TraceGraph() {
        this.edges = new ArrayList<>();
        this.nodes = new ArrayList<>();
        nodeCache = new HashMap<>();
        edgeCache = new HashMap<>();
    }


    /**
     * 添加目标图到本图中
     * @param otherGraph
     */
    public void addGraph(TraceGraph otherGraph) {

        // 添加 node
        this.nodes.addAll(otherGraph.nodes);
        otherGraph.nodes.forEach(node -> this.nodeCache.put(node.getId(), node));

        // 添加 edges
        this.edges.addAll(otherGraph.edges);
        otherGraph.edges.forEach(edge -> this.edgeCache.put(edge.getId(), edge));

    }

    /**
     * 添加节点
     *
     * @param node
     */
    public void addNode(TraceNode node) {

        // 缓存中已存在该节点, return
        if (nodeCache.containsKey(node.getId())) {
            return;
        }

        this.nodes.add(node);
        this.nodeCache.put(node.getId(), node);
    }

    /**
     * 建立连接
     * 注意: linkNode 的 id 由 source 和 target id 生成,
     * 且 source 和 target id不变的情况下,  linkNode 的 id 保持不变
     * @param source
     * @param target
     */
    public void link(TraceNode source, TraceNode target) {

        // 生成 id
        int id = (source.getId() >> 1) * target.getId();
        // 放入缓存
        if (this.edgeCache.containsKey(id)) {
            return;
        }
        // 构建 对象
        TraceEdge edge = new TraceEdge();
        edge.setSourceId(source.getId());
        edge.setTargetId(target.getId());
        edge.setId(id);

        // 放入缓存
        this.edges.add(edge);
        this.edgeCache.put(id, edge);

    }


    public static TraceGraph valueOf(String chainId) {

        Condition condition = FlowBus.getChain(chainId).getConditionList().get(0);

        return valueOf(condition.getExecutableList(), linkNode("start", "[开始]"), linkNode("end", "[结束]"), true);
    }


    /**
     *
     * 在 Graph 的定义中, 每个图只有两种情况, 串行和并行
     * 串行和并行是处理情况是不同的
     * 本方法的功能是:
     * 将Liteflow一个可执行列表转换为一个图
     * @param executables Liteflow 含义下的图的信息来源 可执行列表
     * @param prefixNode 前直接点
     * @param suffixNode 后值点
     * @param isSerial 是否是串行的 @see isSerial()
     * @return
     */
    private static TraceGraph valueOf(List<? extends Executable> executables, TraceNode prefixNode, TraceNode suffixNode, boolean isSerial) {

        TraceGraph graph = new TraceGraph();

        // 添加前置节点
        graph.addNode(prefixNode);


        // 连接图
        TraceNode pointer = connectGraph(graph, executables, prefixNode, suffixNode, isSerial);

        // 如果是串行化, 连接最后一个节点与 suffixNode
        if (isSerial) {
            // pass
            // 连接suffix节点
            graph.link(pointer, suffixNode);
        }
        // 如果是并行, 则不需要了, 因为每个node已经主动连接过 suffix 节点了
        else {
            // pass
        }

        // 无论任何 graph / condition (串行/并行/其他), 后置节点作为本子图的收尾节点, 必须最后添加, 便于父图链接
        // 添加后置节点
        graph.addNode(suffixNode);

        return graph;
    }


    /**
     * @return 返回 pointer 节点
     */
    private static TraceNode connectGraph(TraceGraph graph, List<? extends Executable> executables, TraceNode prefixNode, TraceNode suffixNode, boolean isSerial) {

        TraceNode pointer = prefixNode;

        for (Executable executable: executables) {

            switch (executable.getExecuteType()) {
                case NODE:
                    /**
                     * 从功能角度分析 Node, 主要分为两个情况 串行/并行
                     * 串行:
                     * 1. 将本节点加入到图的节点中.
                     * 2. 与本节点与前置节点连接.
                     * 3. 更新 pointer 节点为当前节点 (便于再次循环时链接前置节点)
                     * 4. 无需将本节点与后置节点连接 (如果是串行化, 不知道下个节点的值, 此步骤不好处理, 在本图的最后一步链接即可)
                     * 并行:
                     * 1. 将本节点加入到图的节点中.
                     * 2. 与本节点与本图的前置节点连接.
                     * 3. 无需更新 pointer 信息 (从图的角度看, 每个节点都是并行的, 无需更新 pointer 信息)
                     * 4. 将本节点与后置节点连接 (并行时, 前置和后置节点都是可知的)
                     */

                    TraceNode current = TraceNode.valueOf(executable);
                    // 1. 将本节点加入到图的节点中.
                    graph.addNode(current);
                    // 2. 与本节点与前置节点连接.
                    graph.link(pointer, current);

                    if (isSerial) {
                        // 3. 更新 pointer 节点为当前节点
                        pointer = current;
                    }
                    // 如果是并行, 每个节点与尾节点相连
                    else {
                        graph.link(current, suffixNode);
                    }

                    break;
                case CONDITION: {
                    /**
                     * 从功能角度分析 Condition
                     * 每个 Condition 其实都是一个子图, 需要进入下次递归,
                     * 子图必须有前置连接点和后置连接点, 因为
                     * 如果没有前置连接点, 就会丢失并行所有数据同时开始的信息 (其实也并不是完全同时开始, 因为并行是JVM执行时, 线程是按照定义的顺序启动执行的, 但是从图的角度看, 是同时开始的)
                     * 如果没有后置连接点, 就会丢失并行等待所有数据到达的信息 (计算并行结束的耗时时就会失真)
                     * 1. 构建前置连接点和后置连接点
                     * 2. 连接 pointer 与前置连接点
                     * 3. 递归调用, 构建子图, 并添加到本图中
                     * 串行/并行
                     * 串行:
                     * 4. 更新 pointer 节点为后置连接点 (便于再次循环时链接前置节点)
                     * 5. 无需将本节点与后置节点连接 (如果是串行化, 不知道下个节点的值, 此步骤不好处理, 在本图的最后一步链接即可)
                     * 并行:
                     * 4. 无需更新 pointer 信息 (从图的角度看, 每个节点都是并行的, 无需更新 pointer 信息)
                     * 5. 将本节点与后置节点连接 (并行时, 前置和后置节点都是可知的)
                     */


                    Condition condition = (Condition) executable;

                    // 1. 构建前置连接点和后置连接点
                    TraceNode prefix = prefixLinkNode(condition);
                    TraceNode suffix = suffixLinkNode(condition);

                    // 2. 连接 pointer 与前置连接点
                    graph.link(pointer, prefix);


                    // 3. 递归调用, 构建子图, 并添加到本图中
                    graph.addGraph(TraceGraph.valueOf(condition.getExecutableList(), prefix, suffix, isSerial(condition)));



                    if (isSerial) {
                        // 串行:
                        // 4. 更新 pointer 节点为后置连接点 (便于再次循环时链接前置节点)
                        // 5. 无需将本节点与后置节点连接 (如果是串行化, 不知道下个节点的值, 此步骤不好处理, 在本图的最后一步链接即可)
                        pointer = suffix;
                    }
                    else {
                        // 并行:
                        // 4. 无需更新 pointer 信息 (从图的角度看, 每个节点都是并行的, 无需更新 pointer 信息)
                        // 5. 将本节点与后置节点连接 (并行时, 前置和后置节点都是可知的)
                        graph.link(suffix, suffixNode);
                    }


                }
                    break;
                case CHAIN: {
                    /**
                     * 从功能角度分析 Chain, Chain 其实只是将子图的内容直接搬运到本上下文中, 因此
                     * 1. 不需要额外的节点连接.
                     * 2. 不需要新建/更新上下文
                     * 3. 但由于子图的上下文与当前的上下文共享, 需要传入和返回指针信息
                     */

                    Chain chain = (Chain) executable;

                    // 3. 但由于子图的上下文与当前的上下文共享, 需要传入和返回指针信息
                    pointer = connectGraph(graph, chain.getConditionList(), pointer, suffixNode, isSerial);

                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected executable type value: " + executable.getExecuteType());
            }


        }

        return pointer;
    }


    /**
     * 构建一个 condition 子图的前置节点
     * 需要注意的是, 内存中同一个 condition 构建出的 TraceNode 的 id 最好是唯一的
     * @param condition
     * @return
     */
    private static TraceNode prefixLinkNode(Condition condition) {
        String id = String.valueOf(condition.hashCode());
        return linkNode(id + "-start",  "(" + condition.getConditionType().getType() + ")开始");
//        return linkNode(id + "-start", condition.getConditionType().getType() + "(" + id + ")开始");
    }


    /**
     * 构建一个 condition 子图 的后置节点
     * 需要注意的是, 内存中同一个 condition 构建出的 TraceNode 的 id 最好是唯一的
     * @param condition
     * @return
     */
    private static TraceNode suffixLinkNode(Condition condition) {
        String id = String.valueOf(condition.hashCode());
//        return linkNode(id + "-end", condition.getConditionType().getType() + "(" + id + ")结束");
        return linkNode(id + "-end", "(" + condition.getConditionType().getType() + ")结束");
    }


    /**
     * 创建一个 link 节点
     * 同一个 JVM 内存中同一个 id 构建出的 TraceNode 的 id 最好是唯一的
     * @param id
     * @param label
     * @return
     */
    private static TraceNode linkNode(String id, String label) {

        TraceNode node = new TraceNode();
        node.setId(id.hashCode());
        node.setLabel(label);
        node.setType(TraceNode.NodeType.LINK.name());

        return node;
    }


    /**
     * 子图是否是串行执行,
     * 请根据 com.yomahub.liteflow.flow.element.Condition 的子类来判断, 按需扩充
     * @see com.yomahub.liteflow.flow.element.condition
     * @param subGraph
     * @return
     */
    //
    private static boolean isSerial(Condition subGraph) {

        return switch (subGraph.getConditionType()) {
            case TYPE_WHEN -> // 并行
                    false;
            case TYPE_THEN -> // 串行
                    true;
            case TYPE_IF -> // 并行
                    false;
            case TYPE_SWITCH -> // 并行
                    false;
            case TYPE_FOR -> // 不确定, 先按照串行计算
                    true;
            case TYPE_WHILE -> // 不确定, 先按照串行计算
                    true;
            default -> // 默认串行
                    false;
        };
    }
}
