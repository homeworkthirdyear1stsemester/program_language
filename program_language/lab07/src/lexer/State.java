package lexer;

import static lexer.TokenType.*;
import static lexer.TransitionOutput.*;


enum State {
    START {//시작 상태이다

        @Override
        public TransitionOutput transit(ScanContext context) {
            Char ch = context.getCharStream().nextChar();//stream에서 하나씩읽어 들이는 역할을 한다.
            char v = ch.value();
            switch (ch.type()) {//type을 불러옴
                case LETTER:
                    context.append(v);
                    return GOTO_ACCEPT_ID;//문자열을 입력받은경우
                case DIGIT:
                    context.append(v);
                    return GOTO_ACCEPT_INT;//정수형이 입력될 경우
                case SPECIAL_CHAR: //special charactor가 들어온 경우
                    if (v == '-' || v == '+') { //부호인경우 상태반환
                        context.append(v);
                        return GOTO_SIGN;//부호인지 아니면 operator인지를 판별 하는 state로 이동
                    } else if (v == '#') {  //boolean인 경우 상태반환
                        context.append(v);
                        return GOTO_SHARP;//boolean인지 아니면 오타인지 판별 해 주는 역할을 하는 state로 이동
                    } else { //그외에는 type을 알아내서 알맞은 상태로 반환
                        context.append(v);
                        return GOTO_MATCHED(TokenType.fromSpecialCharactor(v), context.getLexime());
                    }
                case WS:
                    return GOTO_START;
                case END_OF_STREAM:
                    return GOTO_EOS;
                default:
                    throw new AssertionError();
            }
        }
    },
    ACCEPT_ID {//ID형태의 모습을 가지고 있는 State

        @Override
        public TransitionOutput transit(ScanContext context) {
            Char ch = context.getCharStream().nextChar();
            char v = ch.value();
            switch (ch.type()) {
                case LETTER:
                case DIGIT:
                    context.append(v);
                    return GOTO_ACCEPT_ID;//변수이름
                case SPECIAL_CHAR:
                    return GOTO_FAILED;//실패 -> 오류를 나타냄
                case WS:
                case END_OF_STREAM://공백이나, 문장의 끝을 확인 했을 경우
                    return GOTO_MATCHED(Token.ofName(context.getLexime()));
                default:
                    throw new AssertionError();
            }
        }
    },
    ACCEPT_INT {//정수형을 나타냄

        @Override
        public TransitionOutput transit(ScanContext context) {
            Char ch = context.getCharStream().nextChar();
            switch (ch.type()) {
                case LETTER:
                    return GOTO_FAILED;//문자가 입력되었으므로 오류
                case DIGIT://숫자 입력
                    context.append(ch.value());
                    return GOTO_ACCEPT_INT;
                case SPECIAL_CHAR://특수문자로서 오류 검출
                    return GOTO_FAILED;
                case WS://공백이므로 해당 내용을 Token 형성
                    return GOTO_MATCHED(INT, context.getLexime());
                case END_OF_STREAM:
                    return GOTO_MATCHED(INT, context.getLexime());
                default://그외의 모든 상태 -> error표시
                    throw new AssertionError();
            }
        }
    },
    SHARP {//해당 문자가 F인지 T인지 파별 그외에 모든 것은 오류

        @Override
        public TransitionOutput transit(ScanContext context) {
            Char ch = context.getCharStream().nextChar();
            char v = ch.value();
            switch (ch.type()) {
                case LETTER://Letter일경우 가능 ->T,F만 가능
                    switch (v) {
                        case 'T'://true
                            context.append(v);
                            return GOTO_MATCHED(TRUE, context.getLexime());
                        case 'F'://false
                            context.append(v);
                            return GOTO_MATCHED(FALSE, context.getLexime());
                        default://모든 문장들이 모두 오류를 표시
                            return GOTO_FAILED;
                    }
                default://T,F외에 모든 경우 예외
                    return GOTO_FAILED;
            }
        }
    },
    SIGN {//해당 값이 부호의 +,-인지 아니면 연산의 정보인지 판별

        @Override
        public TransitionOutput transit(ScanContext context) {
            Char ch = context.getCharStream().nextChar();
            char v = ch.value();
            switch (ch.type()) {
                case LETTER://특수문자 ID이므로 오류
                    return GOTO_FAILED;
                case DIGIT://부호의 값인 걸로 판별
                    context.append(v);
                    return GOTO_ACCEPT_INT;
                case SPECIAL_CHAR://오류
                    return GOTO_FAILED;
                case WS://연산의 digit이므로 token 형성 해준다
                    String lexme = context.getLexime();
                    switch (lexme) {//연산자로 판별
                        case "+":
                            return GOTO_MATCHED(PLUS, lexme);
                        case "-":
                            return GOTO_MATCHED(MINUS, lexme);
                        default://해당 경우 외의 경우이므로 예외 처리
                            throw new AssertionError();
                    }
                case END_OF_STREAM:
                    return GOTO_FAILED;
                default:
                    throw new AssertionError();
            }
        }
    },
    //그외에 모두 예외 처리
    MATCHED {//Matchted인데 state에서 다시 판별 하므로 에러 표시

        @Override
        public TransitionOutput transit(ScanContext context) {
            throw new IllegalStateException("at final state");
        }
    },
    FAILED {
        @Override
        public TransitionOutput transit(ScanContext context) {
            throw new IllegalStateException("at final state");
        }
    },
    EOS {
        @Override
        public TransitionOutput transit(ScanContext context) {
            return GOTO_EOS;
        }
    };

    abstract TransitionOutput transit(ScanContext context);
}
