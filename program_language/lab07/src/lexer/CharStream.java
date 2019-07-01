package lexer;

import java.io.*;

class CharStream {
    private final Reader reader;
    private Character cache;

    static CharStream from(File file) throws FileNotFoundException {
        return new CharStream(new FileReader(file));
    }

    CharStream(Reader reader) {
        this.reader = reader;//읽어 들이는 부분
        this.cache = null;
    }

    Char nextChar() {//다음 char를 입력 받는 곳
        if (cache != null) {
            char ch = cache;
            cache = null;

            return Char.of(ch);
        } else {
            try {
                int ch = reader.read();
                if (ch == -1) {
                    return Char.end();
                } else {
                    return Char.of((char) ch);
                }
            } catch (IOException e) {
                throw new ScannerException("" + e);
            }
        }
    }

    void pushBack(char ch) {
        cache = ch;
    }
}
