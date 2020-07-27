package com.luxf.leetcode.algorithm.graph;


import java.util.*;
import java.util.stream.Collectors;

/**
 * 有向图、
 * 一个图G由顶点的集合V和边的集合E组成：G = (V,E)
 *
 * @author 小66
 * @date 2020-07-21 18:34
 **/
public class DirectedGraph {
    /**
     * 顶点的集合V
     */
    private List<Vertex> vertexList;
    /**
     * 边的集合E
     */
    private List<Edge> edgeList;

    /**
     * 统计入度为0的点的数量、用于判断是否出现有圈图、
     */
    private int count;

    /**
     * TODO: 顶点、需要有个入度的属性(拓扑排序)、
     * 从入度为0的顶点开始、将之标记删除，然后将与该顶点相邻接的顶点的入度减1，再继续寻找入度为0的顶点，直至所有的顶点都已经标记删除或者图中有环。
     */
    private static class Vertex {
        // 入度、
        private int inDegree = 0;
        // 顶点的标识、name
        private String vertexName;

        // 是否被访问过的标记、用于BFS和DFS、
        private boolean visited = false;

        // 该点与目标终点的距离、用于BFS和DFS、
        private int dist = Integer.MAX_VALUE;

        // 具体的访问路径,该点指向当前对象、(当前点的上一个点) 用于BFS和DFS、
        private Vertex preVertex;

        Vertex(String vertexName) {
            this.vertexName = vertexName;
        }

        /**
         * 换种方式构建图, Graph中不需要维护Edge、
         */
        private List<Vertex> adjacentList = new LinkedList<>();
        // 点的名称为Key, 权重为Value
        private Map<String, Integer> adjacentWeightMap = new HashMap<>();

        // 无权、
        void addAdjacentVertex(Vertex vertex) {
            adjacentList.add(vertex);
        }

        // 赋权、
        public void addAdjacentVertex(Vertex vertex, int weight) {
            adjacentList.add(vertex);
            adjacentWeightMap.put(vertex.vertexName, weight);
        }
    }

    /**
     * 边：由2个顶点和权重组成、
     */
    private static class Edge {
        // 边的起点(有向图中, 起点不太需要)
        private Vertex startVertex;
        // 边的终点
        private Vertex endVertex;
        // 边的权重、
        private double weight;

        Edge(Vertex startVertex, Vertex endVertex) {
            this.startVertex = startVertex;
            this.endVertex = endVertex;
            endVertex.inDegree++;
        }
    }

    public static void main(String[] args) {
        List<Vertex> vertexList = new ArrayList<>();
        List<Edge> edgeList = new ArrayList<>();
        Vertex v1 = new Vertex("V1");
        Vertex v2 = new Vertex("V2");
        Vertex v3 = new Vertex("V3");
        Vertex v4 = new Vertex("V4");
        Vertex v5 = new Vertex("V5");
        Vertex v6 = new Vertex("V6");
        Vertex v7 = new Vertex("V7");
        Edge e12 = new Edge(v1, v2);
        Edge e13 = new Edge(v1, v3);
        Edge e24 = new Edge(v2, v4);
        Edge e25 = new Edge(v2, v5);
        Edge e32 = new Edge(v3, v2);
        Edge e34 = new Edge(v3, v4);
        Edge e53 = new Edge(v5, v3);

        // 利用邻接点集合构建图、不需要使用边构建图、
        v1.addAdjacentVertex(v4, 1);
        v1.addAdjacentVertex(v2, 2);
        v2.addAdjacentVertex(v4, 3);
        v2.addAdjacentVertex(v5, 10);
        v3.addAdjacentVertex(v1, 4);
        v3.addAdjacentVertex(v6, 5);
        v4.addAdjacentVertex(v3, 2);
        v4.addAdjacentVertex(v5, 2);
        v4.addAdjacentVertex(v6, 8);
        v4.addAdjacentVertex(v7, 4);
        v5.addAdjacentVertex(v7, 6);
        v7.addAdjacentVertex(v6, 1);

        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);
        vertexList.add(v5);
        vertexList.add(v6);
        vertexList.add(v7);

        edgeList.add(e12);
        edgeList.add(e13);
        edgeList.add(e24);
        edgeList.add(e34);
        // 构成有圈图、
        edgeList.add(e32);
        edgeList.add(e25);
        edgeList.add(e53);

//        DirectedGraph graph = DirectedGraph.buildGraph(vertexList, edgeList);
//        String path = unWeightMinDist(graph, v4, v2);
//        System.out.println("path = " + path);

        DirectedGraph onlyVertexGraph = buildGraph(vertexList);
        dijkstraAlgorithm(onlyVertexGraph, v1, v6);
    }

    /**
     * 贪心算法的精髓之处, 便是使用优先队列获取最小元素、
     *
     * @param graph
     * @param startVertex
     * @param endVertex
     * @return
     */
    private static String dijkstraAlgorithm(DirectedGraph graph, Vertex startVertex, Vertex endVertex) {
        // 起点距离初始化为0、
        startVertex.dist = 0;
        // TODO: 使用优先队列, 获取最小距离的顶点、
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparing(v -> v.dist, Comparator.nullsLast(Integer::compareTo)));
        priorityQueue.addAll(graph.vertexList);
        // 直到所有的顶点都被标记为visited = true, 就会跳出循环、
        while (priorityQueue.size() > 0) {
            // 获取所有 visited = false的顶点, 并且 dist 最小的顶点、
            Vertex vertex = priorityQueue.peek();
            vertex.visited = true;
            System.out.println("vertex.vertexName = " + vertex.vertexName);
            vertex.adjacentList.forEach(v -> {
                if (!v.visited) {
                    int dist = vertex.dist + getEdgeWeight(vertex, v);
                    // 调整最小值, 更新实际路径点、
                    if (dist < v.dist) {
                        /**
                         * TODO: 通过更改前移除,更改后再添加的方式, 对优先队列的顺序进行重排, 否则无法保证优先队列的第一个元素是最小/最大的！
                         */
                        priorityQueue.remove(v); // TODO: 如果优先队列中的元素被更改,但是不先移除, 再添加、优先队列的顺序,无法保证第一个是最小/最大的、
                        v.dist = dist;
                        v.preVertex = vertex;
                        priorityQueue.offer(v);
                    }
                }
            });
            // 移除并重新排序、因为优先队列执行poll()后, 就会重新排序、 为啥优先队列移除V1后(此时v2.dist > v4.dist), V2排在V4前面?
            priorityQueue.poll();
        }

        // TODO: 经过贪心算法后, 起点到每个点的最短路径都已求出、
        graph.vertexList.forEach(vertex -> System.out.println(vertex.vertexName + " dist = " + vertex.dist));


//        // 此处数据量较大,filter()耗时严重、 可以更换为优先队列、 --> TODO: 因此贪心算法常配合优先队列使用。
//        List<Vertex> unvisitedVertex = filter(graph.vertexList);
//        while (unvisitedVertex.size() > 0) {
//            Vertex vertex = unvisitedVertex.get(0);
//            vertex.visited = true;
//            unvisitedVertex = filter(unvisitedVertex);
//            vertex.adjacentList.forEach(v -> {
//                if (!v.visited) {
//                    int dist = vertex.dist + getEdgeWeight(vertex, v);
//                    // 调整最小值, 更新实际路径点、
//                    if (dist < v.dist) {
//                        v.dist = dist;
//                        v.preVertex = vertex;
//                    }
//                }
//            });
//        }

//        利用非优先队列实现、--> 不算是贪心算法
//        Queue<Vertex> vertexQueue = new LinkedList<>();
//        for (Vertex vertex : startVertex.adjacentList) {
//            vertex.dist = getEdgeWeight(startVertex, vertex);
//            vertex.preVertex = startVertex;
//            vertexQueue.offer(vertex);
//        }
//        while (!vertexQueue.isEmpty()) {
//            Vertex poll = vertexQueue.poll();
//            poll.adjacentList.forEach(v -> {
//                // 加入队列、
//                if (v.dist == Integer.MAX_VALUE) {
//                    vertexQueue.offer(v);
//                }
//                int dist = poll.dist + getEdgeWeight(poll, v);
//                // 调整最小值, 更新实际路径点、
//                if (dist < v.dist) {
//                    v.dist = dist;
//                    v.preVertex = poll;
//                }
//            });
//        }

        System.out.println("endVertex.dist = " + endVertex.dist);

        StringBuilder builder = new StringBuilder();
        while (endVertex != null) {
            builder.append(endVertex.vertexName);
            endVertex = endVertex.preVertex;
            if (endVertex != null) {
                builder.append(" -> ");
            }
        }
        System.out.println("path = " + builder.toString());
        return builder.toString();
    }

    private static Integer getEdgeWeight(Vertex vertex, Vertex adjacentVertex) {
        return vertex.adjacentWeightMap.get(adjacentVertex.vertexName);
    }

    private static List<Vertex> filter(List<Vertex> list) {
        return list.stream().filter(v -> !v.visited)
                .sorted(Comparator.comparing(vertex -> vertex.dist)).collect(Collectors.toList());
    }

    /**
     * 拓扑排序、
     *
     * @param graph 有向图、
     * @return
     */
    private static List<String> topologySort(DirectedGraph graph) {
        List<String> result = new ArrayList<>();
        // 用队列来存储入度为0的顶点、
        Queue<Vertex> vertexQueue = new LinkedList<>();
        graph.vertexList.forEach(vertex -> {
            if (vertex.inDegree == 0) {
                vertexQueue.offer(vertex);
            }
        });

        while (!vertexQueue.isEmpty()) {
            Vertex vertex = vertexQueue.poll();
            result.add(vertex.vertexName);
            graph.count++;
            graph.edgeList.forEach(edge -> {
                // 入度为0的顶点出队, 则以该点作为起点的邻接边的终点的入度-1、
                if (edge.startVertex == vertex) {
                    Vertex endVertex = edge.endVertex;
                    endVertex.inDegree--;
                    // 如果终点的入度为0, 则放入队列中
                    if (endVertex.inDegree == 0) {
                        vertexQueue.offer(endVertex);
                    }
                }
            });
        }
        // 队列为空, 返回顶点走向的具体路径、
        if (graph.count != graph.vertexList.size()) {
            throw new RuntimeException("Cycle Graph.");
        }
        return result;
    }

    /**
     * 无权最短路径、 TODO: 广度优先搜索(BFS)：类似树结构的层序遍历, 把每层遍历完, 再继续下一层、直到抵达目标点
     *
     * @return 具体路径指向、
     */
    private static String unWeightMinDist(DirectedGraph graph, Vertex startVertex, Vertex endVertex) {
        // 用队列来存储邻接的顶点、
        Queue<Vertex> vertexQueue = new LinkedList<>();
        startVertex.visited = true;
        startVertex.dist = 0;
        graph.edgeList.forEach(edge -> {
            if (edge.startVertex.equals(startVertex)) {
                edge.endVertex.visited = true;
                edge.endVertex.preVertex = startVertex;
                edge.endVertex.dist = startVertex.dist + 1;
                vertexQueue.offer(edge.endVertex);
            }
        });
        Vertex headerVertex = null;
        while (!vertexQueue.isEmpty()) {
            // 也可以在出队时,更改visited状态
            headerVertex = vertexQueue.poll();
            System.out.println("headerVertex.ve" +
                    "rtexName = " + headerVertex.vertexName);
            if (headerVertex == endVertex) {
                break;
            }
            final Vertex finalVertex = headerVertex;
            // TODO: 不是遍历的邻接点, 造成时间复杂度过大、 --> 可以在Vertex对象中,维护当前点的邻接点的集合属性、
            graph.edgeList.forEach(edge -> {
                // 没有被访问过的点、才加入到队列中、
                if (edge.startVertex.equals(finalVertex) && !edge.endVertex.visited) {
                    edge.endVertex.visited = true;
                    edge.endVertex.preVertex = finalVertex;
                    edge.endVertex.dist = finalVertex.dist + 1;
                    vertexQueue.offer(edge.endVertex);
                }
            });
        }
        assert headerVertex != null;
        List<String> list = new ArrayList<>();
        while (headerVertex != startVertex) {
            list.add(headerVertex.vertexName);
            headerVertex = headerVertex.preVertex;
        }
        list.add(startVertex.vertexName);
        Collections.reverse(list);
        return String.join(" -> ", list);
    }

    private static DirectedGraph buildGraph(List<Vertex> vertexList, List<Edge> edgeList) {
        DirectedGraph graph = new DirectedGraph();
        graph.edgeList = edgeList;
        graph.vertexList = vertexList;
        return graph;
    }

    private static DirectedGraph buildGraph(List<Vertex> vertexList) {
        DirectedGraph graph = new DirectedGraph();
        graph.vertexList = vertexList;
        return graph;
    }
}
