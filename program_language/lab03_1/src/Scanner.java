import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Scanner {
    public enum TokenType {
        ID(3), INT(2);

        private final int finalState;

        TokenType(int finalState) {
            this.finalState = finalState;
        }
    }

    public static class Token {
        public final TokenType type;
        public final String lexme;

        Token(TokenType type, String lexme) {
            this.type = type;
            this.lexme = lexme;
        }

        @Override
        public String toString() {
            return String.format("[%s: %s]", type.toString(), lexme);
        }
    }

    private int transM[][];
    private String source;
    private int pos;
    private StringTokenizer st;

    public Scanner(String source) {
        this.transM = new int[4][128];
        this.source = source == null ? "" : source;
        st = new StringTokenizer(source, " ");
        initTM();
    }

    private void initTM() {
        for (int indexOfCol = 0; indexOfCol < 128; ++indexOfCol) {
            if ('0' <= indexOfCol && indexOfCol <= '9') {
                transM[0][indexOfCol] = 2;
                transM[1][indexOfCol] = 2;
                transM[2][indexOfCol] = 2;
                transM[3][indexOfCol] = 3;
            } else if (indexOfCol == '-') {
                transM[0][indexOfCol] = 1;
                transM[1][indexOfCol] = -1;
                transM[2][indexOfCol] = -1;
                transM[3][indexOfCol] = -1;
            } else if (('a' <= indexOfCol && indexOfCol <= 'z') ||
                    ('A' <= indexOfCol && indexOfCol <= 'Z')) {
                transM[0][indexOfCol] = 3;
                transM[1][indexOfCol] = -1;
                transM[2][indexOfCol] = -1;
                transM[3][indexOfCol] = 3;
            } else {
                transM[0][indexOfCol] = -1;
                transM[1][indexOfCol] = -1;
                transM[2][indexOfCol] = -1;
                transM[3][indexOfCol] = -1;
            }
        }
    }

    private Token nextToken() {
        int stateOld = 0, stateNew;
        //토큰이 더 있는지 검사
        if (!st.hasMoreTokens()) {
            return null;
        }

        //그 다음 토큰을 받음
        String temp = st.nextToken();

        Token result = null;

        char[] arrayOfStr = temp.toCharArray();
        for (int i = 0; i < temp.length(); i++) {
            //문자열의 문자를 하나씩 가져와 현재상태와 TransM를 이용하여 다음 상태를 판별
            //만약 입력된 문자의 상태가 reject 이면 에러메세지 출력 후 return함
            //새로 얻은 상태를 현재 상태로 저장
            stateNew = this.transM[stateOld][arrayOfStr[i]];
            if (stateNew == -1) {
                return new Token(null, "Eorror at : " + temp);
            }
            stateOld = stateNew;
        }

        for (TokenType t : TokenType.values()) {
            if (t.finalState == stateOld) {
                result = new Token(t, temp);
                break;
            }
        }
        return result;
    }

    public List<Token> tokenize() {
        //Token 리스트반환, nextToken()이용..
        List<Token> addTheTokenList = new LinkedList<>();
        Token nowToken = this.nextToken();
        while (nowToken != null) {
            if (nowToken.type == null) {
                System.out.println(nowToken.lexme);
                nowToken = this.nextToken();
                continue;
            }
            addTheTokenList.add(nowToken);
            nowToken = this.nextToken();
        }
        return addTheTokenList;
    }

    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("as03.txt");
        BufferedReader br = new BufferedReader(fr);
        String source = br.readLine();
        StringTokenizer st_of = new StringTokenizer(source);
        String temp = st_of.nextToken();
        Scanner s = new Scanner(source);
        List<Token> tokens = s.tokenize();
        System.out.println(tokens);
    }
}