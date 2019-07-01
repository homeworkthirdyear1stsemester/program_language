package parser.parse;

import parser.ast.ListNode;
import parser.ast.Node;
import parser.ast.QuoteNode;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class NodePrinter {
    private StringBuffer sb = new StringBuffer();//읽어들일 Buffer객체 생성
    private Node root;//root노드

    public NodePrinter(Node root) {
        this.root = root;
    }

    private void printList(ListNode listNode) {//List를 출력
        if (listNode == ListNode.EMPTYLIST) {//예외 일경우
            return;
        }
        if (listNode.car() instanceof QuoteNode) {
            printNode((QuoteNode) listNode.car());//위치 노드가 Quote일 경우 '을 추가해서 넣어준다
        } else if (listNode.car() != null) {
            printNode(listNode.car());
        }//출력을 위한 부분

        if (listNode.cdr() != null) {// null이 아닐 경우 다시 list 노드를 호출 하도록 한다
            printList(listNode.cdr());
        }
    }

    private void printNode(QuoteNode quoteNode) {
        if (quoteNode.nodeInside() == null)
            return;
        sb.append("'");
        printNode(quoteNode.nodeInside());
    }

    private void printNode(Node node) {//하나의 node 출력
        if (node == null) {
            return;
        }
        if (node instanceof ListNode) {
            ListNode listNode = (ListNode) node;//list node의 car이 Quote node인지 아닌지를 판별 해 준다
            if (listNode.car() instanceof QuoteNode) {
                printList(listNode);
            } else {//괄호 추가
                sb.append("( ");
                printList(listNode);
                sb.append(") ");
            }
        } else if (node instanceof QuoteNode) {//Quote node일 경우 해당 node를 출력하는 Node print 함수로 간다
            printNode((QuoteNode) node);
        } else {//출력 문
            sb.append(node.toString() + " ");
        }
    }

    public void prettyPrint() {//해당 root 노드를 기점으로 file에 작성하는 코드
        printNode(root);//호출 된 sb에 추가 된 모든 것들을 추가
        System.out.println("... " + sb.toString());
    }
}