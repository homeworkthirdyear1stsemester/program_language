package parser.ast;

public class QuoteNode implements Node {
    Node quoted;

    public QuoteNode(Node quoted) {
        this.quoted = quoted;
    }//노드 형태가 quote인 형태

    @Override
    public String toString() {
        return quoted.toString();
    }//해당 노드에 대한 출력을 나타내는 것

    public Node nodeInside() {//내부가 quote값을 가지고 있음
        return quoted;
    }
}
