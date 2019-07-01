package parser.parse;

import java.io.File;

public class ParserMain {
    public static final void main(String... args) throws Exception {
        ClassLoader cloader = ParserMain.class.getClassLoader();
        File file = new File(cloader.getResource("parser/as06.txt").getFile());

        CuteParser cuteParser = new CuteParser(file);
        NodePrinter nodePrinter = new NodePrinter(cuteParser.parseExpr());//head를 초기화 해주기위해 객체 선언
        nodePrinter.prettyPrint();//객체를  파일에 작성하기 위한 메소드
    }
}
