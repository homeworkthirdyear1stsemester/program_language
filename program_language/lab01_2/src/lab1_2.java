import java.util.Scanner;

public class lab1_2 {

    public boolean solve(int now, StringBuilder sb, int head_son, int head_mom, int tail_son, int tail_mom, int max) {
        if (now == max) {//최대 깊이에 도달
            return false;
        } else if ((head_mom + tail_mom) <= max) {//깊이 안으로 들어가는 코드
            int left_mom = head_mom + tail_mom;
            int left_son = head_son + tail_son;
            if (this.solve(now + 1, sb, head_son, head_mom, left_son, left_mom, max) && now + 1 != max) {//깊이 안에 들어갔을 경우
                return true;
            } else {//깊이에 들어 갔을 떄 back을 했다. 이 떄 back후에 다시 깊이 안으로 들어가는 코드
                sb.append(", ").append(left_son).append("/").append(left_mom);//back 하면서 입력 받음
                this.solve(now + 1, sb, left_son, left_mom, tail_son, tail_mom, max);//back후 다시 깊이로 들어 갈 수 있음
                return false;//back하고 다시 들어 간다 여기서 이제부터 오른 쪽을 추가 해야하므로 계속 false로 지정
            }
        } else {//깊이 안에 못 들어가는 경우 -> 분모가 지금 index를 넘길 경우
            return false;
        }
    }

    public static void main(String[] args) {
        lab1_2 main = new lab1_2();
        Scanner input = new Scanner(System.in);
        System.out.print("input : ");
        int input_number = input.nextInt();
        StringBuilder sb = new StringBuilder();
        System.out.println("output : ");
        for (int index = 1; index <= input_number; index++) {
            sb.append("f").append(index).append(":[");
            sb.append("0/1");
            main.solve(1, sb, 0, 1, 1, 1, index);
            sb.append(", 1/1]\n");
        }

        System.out.println(sb.toString());
    }
}
