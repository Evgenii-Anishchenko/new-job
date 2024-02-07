package evgenii.newjob.deepclone.model;


import java.util.ArrayList;
import java.util.List;

public class RecursiveNode {
    private String value;
    private List<RecursiveNode> children;

    public RecursiveNode(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(RecursiveNode child) {
        this.children.add(child);
    }

    public String getValue() {
        return value;
    }

    public List<RecursiveNode> getChildren() {
        return children;
    }
}
