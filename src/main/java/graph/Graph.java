package graph;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 有向带权图
 *
 */
public class Graph implements Serializable, Cloneable {

    private  ConcurrentHashMap<String, Vertex> vertexsMap; // 存储所有的顶点

    private class Vertex implements Serializable {
        private String name; // 顶点名称
        private int data;
        private Edge next; // 下一段弧

        Vertex(String name, Edge next) {
            this.name = name;
            this.data = 0;
            this.next = next;
        }
    }

    private class Edge implements Serializable {
        private String name; // 被指向顶点名称
        private int weight; // 弧的权值
        private Edge next; // 下一段弧

        Edge(String name, int weight, Edge next) {
            this.name = name;
            this.weight = weight;
            this.next = next;
        }
    }

    Graph() {
        this.vertexsMap = new ConcurrentHashMap<>();
    }

    public synchronized Map<String, Vertex> getVertexsMap() {
        return this.vertexsMap;
    }

    public void insertVertex(String vertexName) { // 添加顶点
        Vertex vertex = new Vertex(vertexName, null);
        vertexsMap.put(vertexName, vertex);
    }

    public void insertEdge(String begin, String end) {
        insertEdge(begin, end, 0);
    }

    public void insertEdge(String begin, String end, int weight) { // 添加弧
        Vertex beginVertex = vertexsMap.get(begin);
        if (beginVertex == null) {
            beginVertex = new Vertex(begin, null);
            vertexsMap.put(begin, beginVertex);
        }
        Edge edge = new Edge(end, weight, null);
        if (beginVertex.next == null) {
            beginVertex.next = edge;
        } else {
            Edge lastEdge = beginVertex.next;
            while (lastEdge.next != null) {
                lastEdge = lastEdge.next;
            }
            lastEdge.next = edge;
        }
    }
    
    //删除某个点
    public  synchronized void removeVertex(String vertexName) {
        if (!vertexsMap.containsKey(vertexName)) {
            return;
        }
        // 删除其他节点和此节点的关系
        for (Entry<String, Vertex> entry : vertexsMap.entrySet()) {
            if (entry.getKey() != vertexName) {
                Vertex vertex = entry.getValue();
                // 获得第一条边
                Edge lastEdge = vertex.next;
                if (null == lastEdge) {
                    continue;
                } else if (lastEdge.name.equals(vertexName)) {
                    vertex.next = lastEdge.next;
                } else {
                    while (lastEdge.next != null) {
                        if (lastEdge.next.name.equals(vertexName)) {
                            lastEdge.next = lastEdge.next.next;
                            break;
                        }
                        lastEdge = lastEdge.next;
                    }
                }
            }
        }
        vertexsMap.remove(vertexName);
    }

   

    // 获得出度为0的点
    public synchronized List<String> getCanRemoveVertex() {
        List<String> list = new ArrayList<>();
        for (Entry<String, Vertex> entry : vertexsMap.entrySet()) {
            Vertex vertex = entry.getValue();
            if (null == vertex.next) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

    public Graph deepClone() throws IOException, ClassNotFoundException {
        // 将对象写入流中
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        // 从流中取出
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return (Graph) objectInputStream.readObject();

    }

    public void print() { // 打印图
        Set<Entry<String, Vertex>> set = vertexsMap.entrySet();
        Iterator<Entry<String, Vertex>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, Vertex> entry = iterator.next();
            Vertex vertex = entry.getValue();
            Edge edge = vertex.next;
            if (edge == null) {
                System.out.println(vertex.name + " -> " + "null");
            }
            while (edge != null) {
                System.out.println(vertex.name + " -> " + edge.name + " (weight：" + edge.weight + ")");
                edge = edge.next;
            }
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.insertVertex("A");
        graph.insertVertex("B");
        graph.insertVertex("C");
        graph.insertVertex("D");
        graph.insertVertex("E");
        graph.insertVertex("F");

        graph.insertEdge("C", "A", 1);
        graph.insertEdge("F", "C", 2);
        graph.insertEdge("A", "B", 4);
        graph.insertEdge("E", "B", 2);
        graph.insertEdge("A", "D", 5);
        graph.insertEdge("D", "F", 4);
        graph.insertEdge("D", "E", 3);

        graph.print();
    }
}
