package parser.parse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import parser.ast.*;

public class NodePrinter {
    private final String OUTPUT_FILENAME = "output06.txt";//결과 file 생성
    private StringBuffer sb = new StringBuffer();//읽어들일 Buffer객체 생성
    private Node root;

    public NodePrinter(Node root) {
        this.root = root;
    }

    private void printList(Node head) {//List를 출력
        if (head == null) {
            sb.append("( )");
            return;
        }

        sb.append("(");//해당 list의 시작을 알림
        printNode(head);//head를 node를 지정
        sb.append(")");
    }

    private void printNode(Node head) {//하나의 node 출력
        if (head == null)
            return;

        if (head instanceof ListNode) {//list Node인지 판별
            ListNode ln = (ListNode) head;
            printList(ln.value);//List 다시 호출
        } else {
            sb.append("[" + head + "] ");
        }//바로 출력

        if (head.getNext() != null) {
            sb.append(" ");
        }

        printNode(head.getNext());
    }

    public void prettyPrint() {//해당 root 노드를 기점으로 file에 작성하는 코드
        printNode(root);

        try (FileWriter fw = new FileWriter(OUTPUT_FILENAME);//filwriter 객체 생성
             PrintWriter pw = new PrintWriter(fw)) {
            pw.write(sb.toString());//작성
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
