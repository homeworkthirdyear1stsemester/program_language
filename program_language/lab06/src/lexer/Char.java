package lexer;


class Char {
    private final char value;
    private final CharacterType type;

    enum CharacterType {//문자, 숫자, 특수문자, 공백, 문장의 끝을 하나의 char형의 type을 지정 해 주는 enum
        LETTER, DIGIT, SPECIAL_CHAR, WS, END_OF_STREAM,
    }

    static Char of(char ch) {
        return new Char(ch, getType(ch));
    }

    static Char end() {
        return new Char(Character.MIN_VALUE, CharacterType.END_OF_STREAM);
    }

    private Char(char ch, CharacterType type) {
        this.value = ch;
        this.type = type;
    }

    char value() {//값을 return
        return this.value;
    }

    CharacterType type() {
        return this.type;
    }

    private static CharacterType getType(char ch) {
        int code = (int) ch;
        if (('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z') || ch == '?') { //letter가 되는 조건식을 알맞게 채우기
            return CharacterType.LETTER;
        }//?는 특수문자이지만 null?와 같은 특수한 letter를 포함 하므로 추가 해 주었다

        if (Character.isDigit(ch)) {//해당 위치가 숫자일 경우
            return CharacterType.DIGIT;
        }

        switch (ch) {//specail char인 경우를 판별 해주는 역할을 한다.
            case '-':
            case '+':
            case '*':
            case '/':
            case '(':
            case ')':
            case '<':
            case '=':
            case '>':
            case '#':
            case '\'':
                return CharacterType.SPECIAL_CHAR;
        }

        if (Character.isWhitespace(ch)) {
            return CharacterType.WS;
        }
        throw new IllegalArgumentException("input=" + ch);
    }
}
