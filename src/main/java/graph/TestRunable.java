package graph;

public class TestRunable implements Runnable {
    private JudgeRuleGraph judgeRuleGraph;

    public void setJudgeRuleGraph(JudgeRuleGraph judgeRuleGraph) {
        this.judgeRuleGraph = judgeRuleGraph;
    }

    public void run() {
        while (true) {
            String temp = judgeRuleGraph.getOneElefromQueue();
            if (temp == null) {
                if (!judgeRuleGraph.isGraphEmpty()) {
                    try {
                        System.out.println(Thread.currentThread().getName() + ":当前队列为空，等待两秒后再次获取");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                } else {
                    System.out.println(Thread.currentThread().getName() + ":所有节点已全部消费完毕，即将退出");
                    break;
                }
            }
            try {
                System.out.println(Thread.currentThread().getName() + ": consume " + temp);
                System.out.println(Thread.currentThread().getName() + ": is running");
                Thread.sleep((int) (1 + Math.random() * 10) * 1000);
                System.out.println(Thread.currentThread().getName() + ": consume " + temp + "finish");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            judgeRuleGraph.finishConsume(temp);

        }
    }

}