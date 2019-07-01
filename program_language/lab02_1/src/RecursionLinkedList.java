public class RecursionLinkedList {
    private Node head;
    private static char UNDEF = Character.MIN_VALUE;

    private void linkFirst(char element) {
        head = new Node(element, head);
    }//처음 위치에 노드를 추가

    /**
     * 과제 (1) 주어진 Node x의 마지막으로 연결된 Node의 다음으로 새롭게 생성된 노드를 연결
     *
     * @param element 데이터
     * @param x       노드
     */

    private void linkLast(char element, Node x) {
        if (x.next != null) {//제일 마지막 node까지 가야 하므로 마지막 노드로 이동하는 재귀를 돌린다
            this.linkLast(element, x.next);
        } else {//제일 마지막 노드에 새로운 노드가 추가 되야 하므로 else를 사용 -> 만약 else 가 없을 경우 계속해서 추가 되는 불상사가 생김
            x.next = new Node(element, null);
        }
    }

    /**
     * 이전 Node의 다음 Node로 새롭게 생성된 노드를 연결
     *
     * @param element 원소
     * @param pred    이전노드
     */

    private void linkNext(char element, Node pred) {
        Node next = pred.next;
        pred.next = new Node(element, next);
    }

    /**
     * 리스트의 첫번째 원소 해제(삭제)
     *
     * @return 첫번째 원소의 데이터
     */

    private char unlinkFirst() {
        Node x = head;
        char element = x.item;
        head = head.next;
        x.item = UNDEF;
        x.next = null;
        return element;
    }

    /**
     * 이전Node의 다음 Node연결 해제(삭제)
     *
     * @param pred 이전노드
     * @return 다음노드의 데이터
     */

    private char unlinkNext(Node pred) {
        Node x = pred.next;
        Node next = x.next;
        char element = x.item;
        x.item = UNDEF;
        x.next = null;
        pred.next = next;
        return element;
    }

    /**
     * 과제 (2) x노드에서 index만큼 떨어진 Node 반환
     */

    private Node node(int index, Node x) {
        if (index == 0) {//원하는 index에서 거리를 나타낸다고 생각 했을 경우 0일 경우 원하는 위치를 지정 하는 것이다.
            return x;
        } else {//해당 index랑 거리가 더 멀 경우기 때문에 재귀로 깊이로 들어가 는 것이다
            return this.node(index - 1, x.next);
        }
    }

    /**
     * 과제 (3) 노드로부터  끝까지의 리스트의 노드 갯수 반환
     */

    private int length(Node x) {
        // 채워서 사용, recursion 사용
        if (x.next == null) {//각 노드의 길이가 1이므로 마지막 노드의 길이를 1로 return
            return 1;
        }
        return 1 + this.length(x.next);
    }//이 전에 node의 길이에서 현재 노드의 길이를 추가 하면 +1을 해 주면 되기 때문

    /**
     * 과제 (4) 노드로부터 시작하는 리스트의 내용 반환
     */

    private String toString(Node x) {
        // 채워서 사용, recursion 사용
        if (x.next == null) {
            return " " + x.item;
        }//현재 노드의 data를 string 형태로 반환
        return " " + x.item + this.toString(x.next);//이저에 모든 노드의 string형태로 반환 받는다
    }//마지막으로 앞에 현재위치의 data를 반환 하면 원하는 모든 data를 반환 가능

    /**
     * 추가 과제
     * (5) 현재 노드의 이전 노드부터 리스트의 끝까지를 거꾸로 만듬
     * ex)노드가 [s]->[t]->[r]일 때, reverse 실행 후 [r]->[t]->[s]
     *
     * @param x    현재 노드
     * @param pred 현재노드의 이전 노드
     */
    private void reverse(Node x, Node pred) {//지금 위치와 다음위치를 지정 해야 한다
        // 채워서 사용, recursion 사용
        if (x.next != null) {//만약 x가 마지막에 null이 아닌 경우 list의 마지막으로 가도록 간다
            this.reverse(x.next, x);
        } else {//만약 마지막으로 갔을 경우 x를 head로 지정 한다.
            head = x;
        }
        x.next = pred;//현재 x에 next주소에 pred의 주소를 넣어 준다 그리고 하나의 stack을 빠져 나왔을 경우 pred가 x 가되고 pred가 x의 pred가 된다
    }//그러므로 x의 next를 pred를 넣어주면 뒤집혀 지는 현상을 받는다

    /**
     * 리스트를 거꾸로 만듬
     */

    public void reverse() {
        reverse(head, null);
    }

    /**
     * 추가 과제 (6) 두 리스트를 합침 ( A + B )   * ex ) list1 =[l]->[o]->[v]->[e] , list2=[p]->[l] 일 때,
     * list1.addAll(list2) 실행 후 [l]->[o]->[v]->[e]-> [p]->[l]
     *
     * @param x list1의 노드
     * @param y list2 의 head
     */
    private void addAll(Node x, Node y) {//초기에는 x는 list1의 head, y는 list2의 head를 나타 낸다
        // 채워서 사용, recursion 사용
        if (x == null) {//만약 x가 null일 경우 head가 없을 경우 x에 y를 그냥 넣어주면된다
            x = y;
        } else if (x.next == null && y != null) {//x가 list1의 제일 마지막 까지 갔을 경우 이므로 x의 next에 y(list2의 head)를 연결 해 주면 주소가 연결되므로 성공한다.
            x.next = new Node(y.item, null);
            this.addAll(x.next, y.next);
        } else {//만약 x의 꼬리 부분이 아닐경우 계속해서 next로 이동
            this.addAll(x.next, y);
        }
    }

    /**
     * 두 리스트를 합침 ( this + B )
     */
    public void addAll(RecursionLinkedList list) {
        addAll(this.head, list.head);
    }

    /**
     * 원소를 리스트의 마지막에 추가
     */

    public boolean add(char element) {
        if (head == null) {
            linkFirst(element);
        } else {
            linkLast(element, head);
        }
        return true;
    }

    /**
     * 원소를 주어진 index 위치에 추가
     *
     * @param index   리스트에서 추가될 위치
     * @param element 추가될 데이터
     */

    public void add(int index, char element) {
        if (!(index >= 0 && index <= size())) throw new IndexOutOfBoundsException("" + index);
        if (index == 0) linkFirst(element);
        else linkNext(element, node(index - 1, head));
    }

    /**
     * 리스트에서 index 위치의 원소 반환
     */

    public char get(int index) {
        if (!(index >= 0 && index < size())) {
            throw new IndexOutOfBoundsException("" + index);
        }
        return node(index, head).item;
    }

    /**
     * 리스트에서 index 위치의 원소 삭제
     */
    public char remove(int index) {
        if (!(index >= 0 && index < size())) {
            throw new IndexOutOfBoundsException("" + index);
        }
        if (index == 0) {
            return unlinkFirst();
        }
        return unlinkNext(node(index - 1, head));
    }

    /**
     * 리스트의 원소 갯수 반환
     */
    public int size() {
        return length(head);
    }

    @Override
    public String toString() {
        if (head == null) return "[]";
        return "[ " + toString(head) + "]";
    }

    /**
     * 리스트에 사용될 자료구조
     */
    private static class Node {
        char item;
        Node next;

        Node(char element, Node next) {
            this.item = element;
            this.next = next;
        }
    }
}