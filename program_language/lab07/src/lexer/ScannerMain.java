package lexer;

import java.io.*;
import java.util.stream.Stream;

public class ScannerMain {
    public static final void main(String... args) throws Exception {//Main문
        ClassLoader cloader = ScannerMain.class.getClassLoader();//객체 생성
        File file = new File(cloader.getResource("as04.txt").getFile());//해당 파일을 읽음
        testTokenStream(file);//지금 객체의 testTokenStream메소드 실행
    }

    // use tokens as a Stream
    private static void testTokenStream(File file) throws FileNotFoundException {
        Stream<Token> tokens = Scanner.stream(file);

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
