package lexer;

class CharStream {
    private final String data;
    private Character cache;
    private int index;

    static CharStream from(String data){
        return new CharStream(data);
    }

    CharStream(String data) {
        this.data = data;//읽어 들이는 부분
        this.cache = null;
        this.index = 0;
    }

    Char nextChar() {//다음 char를 입력 받는 곳
        if (cache != null) {
            char ch = cache;
            cache = null;

            return Char.of(ch);
        } else {
            if (index >= this.data.length()) {
                return Char.end();
            }
            int ch = this.data.charAt(this.index);
            this.index++;
            return Char.of((char) ch);
        }
    }

    void pushBack(char ch) {
        cache = ch;
    }
}
