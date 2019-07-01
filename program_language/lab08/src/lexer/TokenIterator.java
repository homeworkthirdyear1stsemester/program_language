package lexer;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;


class TokenIterator implements Iterator<Token> {//Optional Iterator를 만들어 주는 객체
    private final ScanContext context;
    private Optional<Token> nextToken;

    TokenIterator(ScanContext context) {
        this.context = context;
        nextToken = readToNextToken(context);
    }

    @Override
    public boolean hasNext() {//현재 커서위치에 토큰이 존재 하는지 판별
        return nextToken.isPresent();
    }

    @Override
    public Token next() {//다음 토큰을 찾음
        if (!nextToken.isPresent()) {
            throw new NoSuchElementException();
        }

        Token token = nextToken.get();
        nextToken = readToNextToken(context);

        return token;
    }

    private Optional<Token> readToNextToken(ScanContext context) {
        State current = State.START;
        while (true) {//무한루프를 돌면서 해당 상태가 완료된 상태가 되거나 에러난 상태가 될 때까지 확인을 해준다.
            TransitionOutput output = current.transit(context);//결과를 저장
            if (output.nextState() == State.MATCHED) {//해당 토큰 찾음
                return output.token();
            } else if (output.nextState() == State.FAILED) {//해당 토큰이 실패 했으므로 에러 표시
                throw new ScannerException();
            } else if (output.nextState() == State.EOS) {//문장의 끝표시
                return Optional.empty();
            }
            current = output.nextState();
        }
    }
}
