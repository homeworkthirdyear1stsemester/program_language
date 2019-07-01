import ast.IntNode;
import ast.ListNode;
import ast.Node;
import compile.TreeFactory;

public class Main {
    public static int max(Node node) {
        //최대값을 리턴하도록 작성
        // value와 next 값 중 큰 값을 리턴
        //value -> 내부, next가 int값
        if (node == null) {//아무 것도 없는 경우 -> null인 경우 value의 최소 값을 return 한다
            return Integer.MIN_VALUE;
        } else if (node.getClass() == ListNode.class) {//Class의 객체를 확인 한다
            return Math.max(Main.max(((ListNode) node).value), Main.max(node.getNext()));//개 결과 중 큰 값을 return 한다.
        } else {//Int 타입의 class일 경우
            return Math.max(Main.max(node.getNext()), ((IntNode) node).value);
        }//자신의 값과 next의 max 값 두 값중 최대 값을 return하여 결과 를 낸다 ->무조건 한개는 있어야지 MIN_VALUE가 안 나타난다.
    }

    public static int sum(Node node) {//합을 나타내는 메소드
        //노드 value의 총합을 반환
        // value와 next의 총 합을 리턴하면됨
        if (node == null) {//null인 경우 0이므로 합을 0이 된다.
            return 0;
        } else if (node.getClass() == ListNode.class) {//List이 경우 value 노드가 가지고 있는 합과 next가 가지고 있는 합의 합을 return 함
            return Main.sum(((ListNode) node).value) + Main.sum(node.getNext());
        } else {//Int형일 경우 next와 자신의 값의 합을 리턴함
            return Main.sum(node.getNext()) + ((IntNode) node).value;
        }//value값이랑 재귀 값이랑 더해서 결과를 return
    }

    public static void main(String... args) {
        Node node = TreeFactory.createtTree("( ( 3 ( ( 10 ) ) 6 ) 4 1 ( ) -2 ( ) )");
        //이하 결과를 출력하도록 작성
        System.out.println("max : " + Main.max(node));
        System.out.println("sum : " + Main.sum(node));
    }
}