package parser.ast;

import lexer.TokenType;

import java.util.HashMap;
import java.util.Map;


// 여기를 작성하게 해도 좋을 듯 18.4.10
public class FunctionNode implements ValueNode {//각각의 상태의 FunctionType이라는 새로운 TokenType생성

    public enum FunctionType {//enum 형태로 지정
        ATOM_Q {
            TokenType tokenType() {
                return TokenType.ATOM_Q;
            }
        },
        CAR {
            TokenType tokenType() {
                return TokenType.CAR;
            }
        },
        CDR {
            TokenType tokenType() {
                return TokenType.CDR;
            }
        },
        COND {
            TokenType tokenType() {
                return TokenType.COND;
            }
        },
        CONS {
            TokenType tokenType() {
                return TokenType.CONS;
            }
        },
        DEFINE {
            TokenType tokenType() {
                return TokenType.DEFINE;
            }
        },
        EQ_Q {
            TokenType tokenType() {
                return TokenType.EQ_Q;
            }
        },
        LAMBDA {
            TokenType tokenType() {
                return TokenType.LAMBDA;
            }
        },
        NOT {
            TokenType tokenType() {
                return TokenType.NOT;
            }
        },
        NULL_Q {
            TokenType tokenType() {
                return TokenType.NULL_Q;
            }
        };

        private static Map<TokenType, FunctionType> fromeTokenType = new HashMap<>();

        static {
            for (FunctionType fType : FunctionType.values()) {
                fromeTokenType.put(fType.tokenType(), fType);
            }
        }

        static FunctionType getFunctionType(TokenType fType) {
            return fromeTokenType.get(fType);
        }

        abstract TokenType tokenType();
    }

    public FunctionType value;

    @Override
    public String toString() {
        return value.name();
    }

    public void setValue(TokenType tType) {
        FunctionType fType = FunctionType.getFunctionType(tType);
        value = fType;
    }
}
