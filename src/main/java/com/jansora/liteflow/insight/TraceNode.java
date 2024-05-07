package com.jansora.liteflow.insight;

import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: jansora 
 * @date: 2024-04-19 10:10:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
// 节点类
public class TraceNode implements Serializable {


    // 取自 node / condition hashcode
    private Integer id;

    /**
     * 名称
     */
    private String name;

    private String label;

    private String type;

    private Map<String, Statistic> statistics = new HashMap<>();

    public enum NodeType {
        CONDITION,
        LINK,
        NODE,
        NULL,
    }



    public void addStatistic(Statistic statistic) {
        this.statistics.put(statistic.getId(), statistic);
    }
    public void updateStatistic(String id, JsonNode output) {
        Statistic statistic = this.statistics.get(id);
        if (null == statistic) return;
        statistic.setOutput(output);
    }



    /**
     * 从 Liteflow 可执行节点元素中 生成节点
     * @param executable
     * @return
     */
    public static TraceNode valueOf(Executable executable) {

        TraceNode node = new TraceNode();
        node.setId(executable.hashCode());
        node.setLabel(executable.getId());

        switch (executable.getExecuteType()) {
            case NODE:
                node.setType(NodeType.NODE.name());
                node.setName(((Node) executable).getInstance().getNodeId());
                node.setLabel(node.name);
                break;
            case CONDITION:
                node.setType(NodeType.CONDITION.name());
                break;
            default:
                node.setType(NodeType.NULL.name());
        }

        return node;
    }


    @Override
    public String toString() {
        return "TraceNode{" +
                "id='" + id + '\'' +
                "type='" + type + '\'' +
                "label='" + label + '\'' +
                '}';
    }
}