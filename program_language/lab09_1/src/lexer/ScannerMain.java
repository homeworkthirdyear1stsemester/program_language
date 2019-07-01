package lexer;

import java.io.*;
import java.util.stream.Stream;

public class ScannerMain {

    // use tokens as a Stream
    private static void testTokenStream(String data) throws FileNotFoundException {
        Stream<Token> tokens = Scanner.stream(data);

        try (FileWriter writer = new FileWriter("output.txt");//작성할 파일 객체를 생성
             PrintWriter pw = new PrintWriter(writer)) {
            tokens.map(ScannerMain::toString).forEach(pw::println);//stream에 있는 모든 data를 출력해 주는 형식
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String toString(Token token) {
        return String.format("%-3s: %s", token.type().name(), token.lexme());
    }
}
