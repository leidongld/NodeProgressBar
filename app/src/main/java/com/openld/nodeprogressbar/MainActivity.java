package com.openld.nodeprogressbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.openld.nodeprogressbar.view.NodeProgressBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NodeProgressBar mNodeProgressBar1;
    private NodeProgressBar mNodeProgressBar2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNodeProgressBar1 = findViewById(R.id.node_progress_bar1);
        mNodeProgressBar2 = findViewById(R.id.node_progress_bar2);

        initNodeProgressBar1();
        initNodeProgressBar2();
    }

    private void initNodeProgressBar2() {
        List<NodeProgressBar.Node> nodeList = new ArrayList<>();

        NodeProgressBar.Node node1 = new NodeProgressBar.Node();
        node1.nodeState = NodeProgressBar.Node.NodeState.REACHED;
        node1.nodeAfterLineState = NodeProgressBar.Node.LineState.REACHED;
        node1.topTxt = "青铜";
        node1.bottomTxt = "入门";
        nodeList.add(node1);

        NodeProgressBar.Node node2 = new NodeProgressBar.Node();
        node2.nodeState = NodeProgressBar.Node.NodeState.REACHED;
        node2.nodeAfterLineState = NodeProgressBar.Node.LineState.REACHED;
        node2.topTxt = "白银";
        node2.bottomTxt = "初级";
        nodeList.add(node2);

        NodeProgressBar.Node node3 = new NodeProgressBar.Node();
        node3.nodeState = NodeProgressBar.Node.NodeState.REACHED;
        node3.nodeAfterLineState = NodeProgressBar.Node.LineState.REACHED;
        node3.topTxt = "黄金";
        node3.bottomTxt = "中级";
        nodeList.add(node3);

        NodeProgressBar.Node node4 = new NodeProgressBar.Node();
        node4.nodeState = NodeProgressBar.Node.NodeState.REACHED;
        node4.nodeAfterLineState = NodeProgressBar.Node.LineState.REACHED;
        node4.topTxt = "钻石";
        node4.bottomTxt = "高级";
        nodeList.add(node4);

        NodeProgressBar.Node node5 = new NodeProgressBar.Node();
        node5.nodeState = NodeProgressBar.Node.NodeState.FINISHED;
        node5.nodeAfterLineState = NodeProgressBar.Node.LineState.REACHED;
        node5.topTxt = "星耀";
        node5.bottomTxt = "专家";
        nodeList.add(node5);

        mNodeProgressBar2.setNodeList(nodeList);

    }

    private void initNodeProgressBar1() {
        List<NodeProgressBar.Node> nodeList = new ArrayList<>();

        NodeProgressBar.Node node1 = new NodeProgressBar.Node();
        node1.nodeState = NodeProgressBar.Node.NodeState.REACHED;
        node1.nodeAfterLineState = NodeProgressBar.Node.LineState.REACHED;
        node1.topTxt = "青铜";
        node1.bottomTxt = "入门";
        nodeList.add(node1);

        NodeProgressBar.Node node2 = new NodeProgressBar.Node();
        node2.nodeState = NodeProgressBar.Node.NodeState.REACHED;
        node2.nodeAfterLineState = NodeProgressBar.Node.LineState.REACHED;
        node2.topTxt = "白银";
        node2.bottomTxt = "初级";
        nodeList.add(node2);

        NodeProgressBar.Node node3 = new NodeProgressBar.Node();
        node3.nodeState = NodeProgressBar.Node.NodeState.FAILED;
        node3.nodeAfterLineState = NodeProgressBar.Node.LineState.REACHED;
        node3.topTxt = "黄金";
        node3.bottomTxt = "中级";
        nodeList.add(node3);

        NodeProgressBar.Node node4 = new NodeProgressBar.Node();
        node4.nodeState = NodeProgressBar.Node.NodeState.UNREACHED;
        node4.nodeAfterLineState = NodeProgressBar.Node.LineState.UNREACHED;
        node4.topTxt = "钻石";
        node4.bottomTxt = "高级";
        nodeList.add(node4);

        NodeProgressBar.Node node5 = new NodeProgressBar.Node();
        node5.nodeState = NodeProgressBar.Node.NodeState.UNREACHED;
        node5.nodeAfterLineState = NodeProgressBar.Node.LineState.UNREACHED;
        node5.topTxt = "星耀";
        node5.bottomTxt = "专家";
        nodeList.add(node5);

        mNodeProgressBar1.setNodeList(nodeList);
    }
}