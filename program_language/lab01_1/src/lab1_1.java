import java.math.BigInteger;
import java.util.Scanner;

public class lab1_1 {

    private BigInteger solve(int number_of_input) {
        if (number_of_input <= 1) {//1이하일 경우에는 모두 1로 해도 곱셈이기 때문에 무방하다
            return new BigInteger("1");
        } else {//각 재귀에서 factorial을 해줄떄 -2를 고려해서 한다
            return this.solve(number_of_input - 2).multiply(new BigInteger(Integer.toString(number_of_input)));
        }
    }//만약 100을 넘기게 된다면 long ,형태를 넘기기 떄문에 BIgInteger를 사용한다.

    public static void main(String[] args) {
        lab1_1 main = new lab1_1();
        Scanner input = new Scanner(System.in);
        System.out.print("input : ");
        int number_of_input = input.nextInt();
        System.out.println("output : " + main.solve(number_of_input));
    }
}
