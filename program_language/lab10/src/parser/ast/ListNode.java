package parser.ast;


public interface ListNode extends Node {//list node 형태
    static ListNode EMPTYLIST = new ListNode() {//비어 있는 노드
        @Override
        public Node car() {
            return null;
        }//비어있으므로 null

        @Override
        public ListNode cdr() {
            return null;
        }//비어있는 노드므로 null
    };

    static ListNode cons(Node head, ListNode tail) {
        return new ListNode() {
            @Override
            public Node car() {
                return head;
            }//해당 노드의 head값을 반환 -> list의 제일 첫 원소를 리턴

            @Override
            public ListNode cdr() {
                return tail;
            }//해당 노드의 tail값을 반환 첫원소를 재외 한 나머지 모든 것들
        };//리스트가 아니면 에러를 낸다
    }

    Node car();
    ListNode cdr();
}
