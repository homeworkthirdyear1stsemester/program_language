package lexer;

class ScanContext {//한 글자씩 입력 받고 추가 하거나 제거 하는 역할
    private final CharStream input;//file에서 읽어 오기 위한 stream객체
    private StringBuilder builder;//lexime를 넣는 곳

    ScanContext(String data){//생성자
        this.input = CharStream.from(data);//static 함수 -> charstream 객체로 입력받음 -> file을 읽고 charater 객체를 null로 지정
        this.builder = new StringBuilder();//StringBuilder로 해당 String의 객체 생성
    }

    CharStream getCharStream() {
        return input;
    }

    String getLexime() {//입력된 결과 return -> builder를 다시 아무 것도 없는 것처럼 초기화
        String str = builder.toString();
        builder.setLength(0);
        return str;
    }

    void append(char ch) {//builder에 추가
        builder.append(ch);
    }
}
