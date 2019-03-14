package graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类有两个作用1、判断是否有回路 2、判断是否有可计算的变量指标3、整理出变量计算顺序
 * 
 * @author cuitianyu
 *
 */
public class JudgeRuleGraph {
    private Graph graph = new Graph();
    private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

    public void printGraph() {
        this.graph.print();
    }

    public boolean isGraphEmpty() {
        if (graph.getVertexsMap().size() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized String getOneElefromQueue() {
        String res = queue.poll();
        if (res == null) {
            return null;
        } else {
            return res;
        }

    }

    /**
     * 判断式子中是否包含变量或指标
     */
    private boolean isContainDependency(String rule) {
        if (rule.contains("@")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解析变量或指标规则中包含的变量或指标
     * 
     * @param rule
     * @return
     */
    private List<String> parseDependency(String rule) {
        List<String> result = new ArrayList<>();
        String pattern = "@(.*?)~";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(rule);
        while (m.find()) {
            if (!result.contains(m.group())) {
                result.add(m.group());
            }
        }
        return result;
    }

    /**
     * 创建图，并将初始化可计算的值填入
     * 
     * @param varMeaMap
     * @return false代表有回路无法计算 true代表正常
     */
    public boolean createDependencyGraph(Map<String, String> varMeaMap) {
        if (varMeaMap.size() <= 0) {
            return false;
        }
        for (String var : varMeaMap.keySet()) {
            graph.insertVertex(var);
        }
        for (String var : varMeaMap.keySet()) {
            if (isContainDependency(varMeaMap.get(var))) {
                List<String> parseDependency = parseDependency(varMeaMap.get(var));
                for (String depvar : parseDependency) {
                    graph.insertEdge(var, depvar);
                }
            }
        }
        if (isGraphLoop()) {
            return false;
        }
        List<String> canRemoveVertex = graph.getCanRemoveVertex();
        for (String e : canRemoveVertex) {
            queue.offer(e);
        }
        return true;
    }

    /**
     * 判断是否有环
     * 
     * @return
     */
    private boolean isGraphLoop() {
        if (graph.getVertexsMap().size() <= 0) {
            return false;
        }
        Graph tempGraph;
        try {
            tempGraph = graph.deepClone();
            List<String> canRemoveVertex = new ArrayList<>();
            while (true) {
                canRemoveVertex = tempGraph.getCanRemoveVertex();
                if (canRemoveVertex.size() <= 0) {
                    if (tempGraph.getVertexsMap().size() > 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
                for (String vertexName : canRemoveVertex) {
                    tempGraph.removeVertex(vertexName);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 队列中某变量或指标计算完成后，添加新的可执行变量或指标到队列中
     * 
     * @param vertex
     */
    public synchronized void finishConsume(String vertex) {
        if (graph.getVertexsMap().size() <= 0) {
            return;
        }
        List<String> oldCanRemove = new ArrayList<>();
        List<String> newCanRemove = new ArrayList<>();
        oldCanRemove = graph.getCanRemoveVertex();
        graph.removeVertex(vertex);
        newCanRemove = graph.getCanRemoveVertex();
        newCanRemove.removeAll(oldCanRemove);
        for (String e : newCanRemove) {
            queue.offer(e);
        }
    }

    public static void main(String[] args) {
        JudgeRuleGraph ju = new JudgeRuleGraph();
//        Map varMeaMap = new HashMap<>();
//        varMeaMap.put("@最值比~", "MAX(@社保~/@平均值~)-@社保~");
//        varMeaMap.put("@社保~", "[user.shebao]");
//        varMeaMap.put("@平均值~", "AVG(@社保~+@公积金~+@公积金1~)");
//        varMeaMap.put("@公积金~", "[inspur.aa]");
//        varMeaMap.put("@公积金1~", "[inspur.aaa]");
//        // varMeaMap.put("@平均值~", "@最值比~");//模仿有环路的情况
//        boolean canCompute = ju.createDependencyGraph(varMeaMap);
//        if (canCompute) {
//            ju.printGraph();
//            for (int i = 0; i <= 5; i++) {
//                TestRunable myThread = new TestRunable();
//                myThread.setJudgeRuleGraph(ju);
//                Thread thread = new Thread(myThread);
//                thread.start();
//            }
//        } else {
//            System.out.println("有环路或表为空，无法计算");
//        }
        List<String> parseDependency = ju.parseDependency("@最值比~\", \"MAX(@社保~/@平均值~)-@社保~");
        System.out.println(parseDependency.toString());
    }
}
