'use client'

import React, {useEffect, useRef, useState} from "react";
import {Cell, Graph} from '@antv/x6'

import '../lib/css/index.css'


Graph.registerNode(
    'event',
    {
        inherit: 'circle',
        attrs: {
            body: {
                strokeWidth: 2,
                stroke: '#5F95FF',
                fill: '#FFF',
            },
        },
    },
    true,
)

Graph.registerNode(
    'activity',
    {
        inherit: 'rect',
        ports: {
            groups: {
                top: {
                    position: 'top',
                    attrs: {
                        circle: {
                            r: 4,
                            magnet: true,
                            stroke: '#C2C8D5',
                            strokeWidth: 1,
                            fill: '#fff',
                        },
                    },
                },
                bottom: {
                    position: 'bottom',
                    attrs: {
                        circle: {
                            r: 4,
                            magnet: true,
                            stroke: '#C2C8D5',
                            strokeWidth: 1,
                            fill: '#fff',
                        },
                    },
                },
            },
        },
        markup: [
            {
                tagName: 'rect',
                selector: 'body',
            },
            {
                tagName: 'image',
                selector: 'img',
            },
            {
                tagName: 'text',
                selector: 'label',
            },
        ],
        attrs: {
            body: {
                rx: 6,
                ry: 6,
                stroke: '#5F95FF',
                fill: '#EFF4FF',
                strokeWidth: 1,
            },
            // img: {
            //     x: 6,
            //     y: 6,
            //     width: 16,
            //     height: 16,
            //     'xlink:href':
            //         'https://gw.alipayobjects.com/mdn/rms_43231b/afts/img/A*pwLpRr7QPGwAAAAAAAAAAAAAARQnAQ',
            // },
            label: {
                fontSize: 12,
                fill: '#262626',
            },
        },
    },
    true,
)

Graph.registerNode(
    'gateway',
    {
        inherit: 'polygon',
        attrs: {
            body: {
                refPoints: '0,10 10,0 20,10 10,20',
                strokeWidth: 2,
                stroke: '#5F95FF',
                fill: '#615',
            },
            label: {
                text: '+',
                fontSize: 40,
                fill: '#5F95FF',
            },
        },
    },

    true,
)

Graph.registerEdge(
    'bpmn-edge',
    {
        inherit: 'edge',
        attrs: {
            line: {
                stroke: '#A2B1C3',
                strokeWidth: 2,
            },
        },
    },
    true,
)

export default function TraceDemo({data}) {

    console.log("data", data)
    const graphRef = useRef(null);


    const adapterData = [...data.nodes.map((node, index) => {
        return {
            // ...node,
            label: node.label,
            shape: node.type == "NODE" ? 'activity' : 'event',
            "width": 40,
            "height": 40,
            "position": {
                "x": 56 * index,
                "y": (index % 2) * 100
            },
            id: node.id.toString()
            // port: {
            //     position: 'left',
            // }
        }
    }), ...data.edges.map(edge => {
        return {
            // ...edge,
            shape: 'bpmn-edge',
            label: edge.label,
            id: edge.id.toString(),
            source: edge.sourceId.toString(),
            target: edge.targetId.toString(),
        }
    })]

    const [graph, setGraph] =  useState<Graph>()

    useEffect(() => {
        if (graphRef.current && !graph) {
            // console.log("abbc")
            const graph = new Graph({
                container: graphRef.current,
                connecting: {
                    router: 'orth',
                },
            })


            // @ts-ignore
            setGraph(graph)
        }
    }, [graphRef, graph])


    console.log("adapter", adapterData)
    useEffect(() => {
        if (graph) {
            // console.log("dddd", data)
            const cells: Cell[] = []

            adapterData.forEach((item: any) => {
                if (item.shape === 'bpmn-edge') {
                    cells.push(graph.createEdge(item))
                } else {
                    cells.push(graph.createNode(item))
                }
            })
            // graph.fromJSON(data)
            // graph.centerContent()
            graph.resetCells(cells)
            graph.zoomToFit({ padding: 10, maxScale: 1 })
        }
    }, [data, graph]);


    return (
        <>
            {/*<div style={{"width": 500, height: 500}}>*/}
            {/*    111*/}
                <div className="backgournd-grid-app">
                    <div className="app-content" ref={graphRef}/>
                </div>
                {/*<div ref={graphRef}>*/}

                {/*</div>*/}
            {/*</div>*/}
        </>
    )
}
