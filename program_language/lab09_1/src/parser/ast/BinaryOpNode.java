package parser.ast;

import lexer.TokenType;

import java.util.HashMap;
import java.util.Map;

public class BinaryOpNode implements ValueNode {
    public enum BinType {
        MINUS {
            TokenType tokenType() {
                return TokenType.MINUS;
            }
        },
        PLUS {
            TokenType tokenType() {
                return TokenType.PLUS;
            }
        },
        TIMES {
            TokenType tokenType() {
                return TokenType.TIMES;
            }
        },
        DIV {
            TokenType tokenType() {
                return TokenType.DIV;
            }
        },
        LT {
            TokenType tokenType() {
                return TokenType.LT;
            }
        },
        GT {
            TokenType tokenType() {
                return TokenType.GT;
            }
        },
        EQ {
            TokenType tokenType() {
                return TokenType.EQ;
            }
        };

        private static Map<TokenType, BinType> fromTokenType = new HashMap<TokenType, BinType>();

        static {//모든 BinType에 대한 정보를 map에 저장
            for (BinType bType : BinType.values()) {
                fromTokenType.put(bType.tokenType(), bType);
            }
        }

        static BinType getBinType(TokenType tType) {//BinType에 맞는 Enum형 type을 return
            return fromTokenType.get(tType);
        }

        abstract TokenType tokenType();
    }

    public BinType binType;//BinType을 value로 가짐

    public void setBinType(TokenType tType) {//Scanner의 TokenType으로 BinType을 지정
        BinType bType = BinType.getBinType(tType);
        binType = bType;
    }

    @Override
    public String toString() {//출력문
        return binType.name();
    }
}
