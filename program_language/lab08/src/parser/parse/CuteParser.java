package parser.parse;

import lexer.Scanner;
import lexer.Token;
import lexer.TokenType;
import parser.ast.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class CuteParser {
    private Iterator<Token> tokens;
    private static Node END_OF_LIST = new Node() {
    };

    public CuteParser(File file) {
        try {
            tokens = Scanner.scan(file);//Iterator 형식으로 가져 올 수 있음
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Token getNextToken() {
        if (!tokens.hasNext())
            return null;
        return tokens.next();
    }

    public Node parseExpr() {//해당 token에 맞는 노드 생성
        Token t = getNextToken();//다음 토큰을 받아옴
        if (t == null) {//문장의 끝
            System.out.println("No more token");
            return null;
        }
        TokenType tType = t.type();//tokenType지정
        String tLexeme = t.lexme();//token string 값 가져옴

        switch (tType) {
            case ID://ID 노드 생성
                return new IdNode(tLexeme);
            case INT://INT노드 생성
                if (tLexeme == null)
                    System.out.println("???");//error 표시
                return new IntNode(tLexeme);
            // BinaryOpNode에 대하여 작성
            // +, -, /, *가 해당
            case DIV:
            case EQ:
            case MINUS:
            case GT:
            case PLUS:
            case TIMES:
            case LT:
                BinaryOpNode binaryOpNode = new BinaryOpNode();//해당 객체 생성
                if (tLexeme == null) {
                    System.out.println("It's fail to make Node");
                }
                binaryOpNode.setBinType(tType);//해당 type 에 맞는 Value를 지정
                return binaryOpNode;
            //->체움
			/*
			여기 채우시오.
			*/

            // FunctionNode에 대하여 작성
            // 키워드가 FunctionNode에 해당
            case ATOM_Q:
            case CAR:
            case CDR:
            case COND:
            case CONS:
            case DEFINE:
            case EQ_Q:
            case LAMBDA:
            case NOT:
            case NULL_Q:
                FunctionNode functionNode = new FunctionNode();
                if (tLexeme == null) {//해당 tLexeme가 null이면 사용할 type이 없으므로 에러를 리턴
                    System.out.println("It's fail to make Node");
                }
                functionNode.setFunctionType(tType);//value 지정
                return functionNode;// 해당 FunctionType의 type인 Node를 return
			/* -> 체움
			여기 채우시오.
			*/
            // BooleanNode에 대하여 작성
            case FALSE://boolean에 대한 type
                return BooleanNode.FALSE_NODE;
            case TRUE://boolean 에 대한 type
                return BooleanNode.TRUE_NODE;
            // case L_PAREN일 경우와 case R_PAREN일 경우에 대해서 작성
            // L_PAREN일 경우 parseExprList()를 호출하여 처리
            case L_PAREN://좌측 괄호에 대한 Node생성 -> .list 노드
			/*
			여기 채우시오.
			*/
                return parseExprList();//parse 해서 List형성 하는 메소드 호출
            case R_PAREN:
                return END_OF_LIST;
            case APOSTROPHE:
                QuoteNode quoteNode = new QuoteNode(parseExpr());
                ListNode listNode = ListNode.cons(quoteNode, ListNode.EMPTYLIST);
                return listNode;
            case QUOTE://apostrophe를 추가
                return new QuoteNode(parseExpr());
            default:
                // head의 next를 만들고 head를 반환하도록 작성
                System.out.println("Parsing Error!");
                return null;
        }
    }

    // List의 value를 생성하는 메소드
    private ListNode parseExprList() {
        Node head = parseExpr();//해당 단에 맞는 노드를 만들어서 head node로 지정
        // 지금 위치에 맞는 Node를 가짐 -> car로서 진짜 data가 저장됨
        if (head == null) // if next token is RPAREN
            return null;
        if (head == END_OF_LIST) {
            return ListNode.EMPTYLIST;
        }//예외 경우 제외
        ListNode tail = parseExprList();//나머지 next를 나타내는 노드이다
        if (tail == null) {
            return null;
        }
        return ListNode.cons(head, tail);//이 노들끼리 결합 하나의 list처럼 보이도록한다
    }
}