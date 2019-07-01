package parser.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

import parser.ast.*;
import parser.ast.BinaryOpNode.BinType;
import lexer.Scanner;
import lexer.ScannerMain;
import lexer.Token;
import lexer.TokenType;

public class CuteParser {
    private Iterator<Token> tokens;

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
                IdNode idNode = new IdNode();//객체 생성
                idNode.value = tLexeme;//value에 ID값 넣기
                return idNode;
            case INT://INT노드 생성
                IntNode intNode = new IntNode();
                if (tLexeme == null)
                    System.out.println("???");//error 표시
                intNode.value = new Integer(tLexeme);
                return intNode;

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
                binaryOpNode.setValue(tType);//해당 type 에 맞는 Value를 지정
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
                functionNode.setValue(tType);//value 지정
                return functionNode;// 해당 FunctionType의 type인 Node를 return
			/* -> 체움
			여기 채우시오.
			*/
            // BooleanNode에 대하여 작성
            case FALSE://boolean에 대한 type
                BooleanNode falseNode = new BooleanNode();
                falseNode.value = false;
                return falseNode;
            case TRUE://boolean 에 대한 type
                BooleanNode trueNode = new BooleanNode();
                trueNode.value = true;
                return trueNode;
            // case L_PAREN일 경우와 case R_PAREN일 경우에 대해서 작성
            // L_PAREN일 경우 parseExprList()를 호출하여 처리
            case L_PAREN://좌측 괄호에 대한 Node생성 -> .list 노드
			/*
			여기 채우시오.
			*/
                ListNode listNode = new ListNode();//객체 생성
                listNode.value = parseExprList();//value를 parseExprList를 호출 해서 해당 value 의 list를 자동으로 호출
                return listNode;//해당 객체 리턴
            case R_PAREN:
                return null;

            default:
                // head의 next를 만들고 head를 반환하도록 작성
                System.out.println("Parsing Error!");
                return null;
        }
    }

    // List의 value를 생성하는 메소드
    private Node parseExprList() {
        Node head = parseExpr();
        // head의 next 노드를 set하시오.
        if (head == null) // if next token is RPAREN
            return null;
        head.setNext(parseExprList());
        return head;
    }
}
