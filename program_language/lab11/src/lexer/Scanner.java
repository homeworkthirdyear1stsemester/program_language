package lexer;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Scanner {
    // return tokens as an Iterator
    public static Iterator<Token> scan(String data) {//파이을 읽어들이는 역할을 함 파일을 읽고 sacnContext로 이동 해준다
        ScanContext context = new ScanContext(data);//해당 파일을 ScanContext객체로 넘겨줌
        return new TokenIterator(context); //TokenIterator로 context를 넣어줌 -> Iterator형성
    }

    // return tokens as a Stream 
    public static Stream<Token> stream(String data) throws FileNotFoundException {//받은 iterator를 Spliterators로 이동 -> 이거 찾아보기
        Iterator<Token> tokens = scan(data);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false);
    }
}