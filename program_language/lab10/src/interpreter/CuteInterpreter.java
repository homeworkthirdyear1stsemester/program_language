package interpreter;

import parser.ast.*;
import parser.parse.CuteParser;
import parser.parse.NodePrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CuteInterpreter {
    public static void main(String[] args) throws IOException {
        HashMap<String, Node> archive = new HashMap<>();//id 노드에 대한 값을 저장 해야 하는 곳
        while (true) {
            System.out.print("> ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String readStr = br.readLine();//입력 받음
            CuteParser cuteParser = new CuteParser(readStr);//parser에 해다 String값입력 받음
            CuteInterpreter interpreter = new CuteInterpreter(archive);
            Node parseTree = cuteParser.parseExpr();//파일에서 node로 전부 형성
            Node resultNode = interpreter.runExpr(parseTree);//interpreter형태로 파싱한 노드들을
            NodePrinter nodePrinter = new NodePrinter(resultNode);
            nodePrinter.prettyPrint();
        }
    }

    private HashMap<String, Node> archive;

    public CuteInterpreter(HashMap<String, Node> archive) {
        this.archive = archive;
    }

    private void insertTable(IdNode id, Node value) {
        if (this.archive.containsKey(id.toString())) {
            this.archive.replace(id.toString(), value);
        } else {
            this.archive.put(id.toString(), value);
        }
    }

    private void errorLog(String err) {//error가 발생했을 경우를 나타 내기 위한 부분
        System.out.println(err);
    }

    public Node runExpr(Node rootExpr) {//root node에 대해 에러 검출
        if (rootExpr == null) {
            return null;
        }
        if (rootExpr instanceof IdNode) {
            return rootExpr;
        } else if (rootExpr instanceof IntNode) {
            return rootExpr;
        } else if (rootExpr instanceof BooleanNode) {
            return rootExpr;
        } else if (rootExpr instanceof ListNode) {
            return runList((ListNode) rootExpr);
        } else {
            errorLog("run Expr error");
        }
        return null;
    }

    private Node runList(ListNode list) {//list node에 대해 해당 노드에 대해 runExpr를 호출
        if (list.equals(ListNode.EMPTYLIST)) {
            return list;
        }//문장의 끝일 경우
        if (list.car() instanceof FunctionNode) {
            return runFunction((FunctionNode) list.car(), (ListNode) stripList(list.cdr()));
        }
        if (list.car() instanceof BinaryOpNode) {
            return runBinary(list);
        }
        return list;
    }

    private Node runFunction(FunctionNode operator, ListNode operand) {
        Node innerNode = null;
        switch (operator.funcType) {
            case CAR:
                if (operand == null || operand.car() == null) {
                    break;
                }//처음에 해당 operand가 null인지 판별
                innerNode = this.runExpr(operand);//재귀로 해당 operand에 대해 ListNode의 값을 가져옴

                if (operand.car() instanceof IdNode) {
                    innerNode = this.archive.get(operand.car().toString());
                }//operand car값이 id 노드이 일 경우

                if (((ListNode) innerNode).car() instanceof ListNode) {
                    innerNode = ((ListNode) innerNode).car();
                }//만약 cdr이 null이 아닐 경우
                innerNode = this.runQuote((ListNode) innerNode);
                if (innerNode instanceof ListNode) {
                    if (((ListNode) innerNode).car() instanceof IntNode || ((ListNode) innerNode).car() instanceof BooleanNode) { //value노드일 경우
                        return ((ListNode) innerNode).car();// 바로 리턴
                    } else {//아닐 경우 Quote 노드로 싸서 그 노드를 ListNode에 넣어서 리턴
                        QuoteNode innerQuoteNode = new QuoteNode(((ListNode) innerNode).car());
                        return ListNode.cons(innerQuoteNode, ListNode.EMPTYLIST);
                    }
                } else if (innerNode instanceof QuoteNode) {//일반적으로 QuoteNode만 리턴 될 경우 바로 Quote노드를 ListNode로 생성
                    return ListNode.cons(innerNode, ListNode.EMPTYLIST);
                }
                break;//예외일 경우
            case CDR://ListNode의 next부분
                if (operand == null || operand.cdr() == null) {
                    break;
                }//예외 경우
                if (operand.car() instanceof IdNode) {//id 노드일 경우
                    innerNode = this.archive.get(operand.car().toString());//탐색
                    if (innerNode instanceof ListNode && ((ListNode) innerNode).car() instanceof QuoteNode) {//quote일 경우 가져옴
                        innerNode = this.runQuote((ListNode) this.runExpr(innerNode));//결과에 대해 연산
                    } else {//오류
                        System.out.println("error happen");
                        return null;
                    }
                } else {
                    innerNode = this.runQuote((ListNode) this.runExpr(operand));//ListNode의 cdr에대해 재귀 그리고 QuoteNode가 무조건 나와야 하므로 QuoteNode의 innerNode로 가져옴
                }
                if (innerNode instanceof ListNode && ((ListNode) innerNode).cdr() != null) {
                    innerNode = this.runExpr(((ListNode) innerNode).cdr());
                    return ListNode.cons(new QuoteNode(innerNode), ListNode.EMPTYLIST);//해당 listnode를 Quotenode로 생성하고 return
                }//마지막 부분이 null유무 판별
                break;
            case CONS://head와 tail을 합쳐서 반환
                if (operand == null) {
                    break;
                }
                innerNode = this.runExpr(operand);//operand에서 재귀로 operand에서 EmptyList를 재거한다.
                Node carNode;
                Node cdrNode;
                if (innerNode instanceof ListNode) {//innerNode가 LIstNode인지 판별 해당 노드가 Listnode여야지 car cdr값을 가질 수 있음
                    if (((ListNode) innerNode).car() != null) {
                        carNode = this.runExpr(((ListNode) innerNode).car());
                        if (carNode instanceof ListNode && ((ListNode) carNode).car() instanceof QuoteNode) {//반환 값이 List노드의 QuoteNode일 수 있으므로
                            carNode = this.runQuote((ListNode) carNode);//Quote노드의 innerNode 가져옴
                        } else if (carNode instanceof IdNode) {//id 노드일 경우
                            carNode = this.archive.get(carNode.toString());
                            if (carNode == null) {//오류 발생 정의 되지 않는 것
                                System.out.println("error happen");
                                break;
                            }
                        }
                    } else {// 예외
                        break;
                    }
                    if (((ListNode) innerNode).cdr() != null) {//cdr의 이 null이 아닐 경우
                        if (((ListNode) innerNode).cdr().car() instanceof IdNode) {//Id노드 값인 경우 가져온다
                            cdrNode = this.archive.get(((ListNode) innerNode).cdr().car().toString());
                            if (cdrNode == null) {// 예외 발 생
                                System.out.println("error happen");
                                break;
                            }
                            if (cdrNode instanceof ListNode && ((ListNode) cdrNode).car() instanceof QuoteNode) {
                                cdrNode = this.runQuote((ListNode) cdrNode);
                            }//Quote노드를 가지는 listnode 일 경우
                        } else {//runexpr에 대한 runQoute실행
                            cdrNode = this.runQuote((ListNode) this.runExpr(((ListNode) innerNode).cdr().car()));
                        }
                        if (!(cdrNode instanceof ListNode)) {
                            cdrNode = ListNode.cons(cdrNode, ListNode.EMPTYLIST);//cdr ListNode생성
                        }
                    } else {
                        break;
                    }//결과
                    return ListNode.cons(new QuoteNode(ListNode.cons(carNode, (ListNode) cdrNode)), ListNode.EMPTYLIST);//앞이 ListNode일 경우
                } else {
                    carNode = runExpr(innerNode);
                    if (carNode instanceof IdNode) {// id 노드일 경우
                        carNode = this.archive.get(carNode.toString());
                        if (carNode == null) {// 예외 발 생
                            System.out.println("error happen");
                            break;
                        }
                    }//listnode로 반환
                    return ListNode.cons(carNode, ListNode.EMPTYLIST);//cdr이 없을 경우
                }
            case NULL_Q:
                if (operand == null) {
                    break;
                }
                if (operand.car() instanceof IdNode) {//id 노드 일 경우 값을 가져온다
                    innerNode = this.archive.get(operand.car().toString());
                    if ((innerNode instanceof ListNode && ((ListNode) innerNode).car() instanceof QuoteNode)) {//list node의 Quote노드를 담고 있을 경우
                        innerNode = this.runQuote((ListNode) innerNode);
                    } else if (innerNode instanceof ValueNode) {//data 존재 false반환
                        return BooleanNode.FALSE_NODE;
                    } else {//예외 발생
                        System.out.println("error happen");
                        return null;
                    }
                } else if (operand.car() instanceof QuoteNode) {
                    innerNode = this.runQuote(operand);
                }
                if (innerNode != null && innerNode instanceof ListNode && ((ListNode) innerNode).car() == null) {//뒤에 cdr이 null일 경우
                    return BooleanNode.TRUE_NODE;
                } else if (innerNode instanceof ValueNode) {
                    return BooleanNode.FALSE_NODE;
                } else {//null이 아닐 경우
                    return BooleanNode.FALSE_NODE;
                }
            case ATOM_Q://한개의 Node만 존재 여부 판별
                innerNode = this.runExpr(operand);
                if (innerNode instanceof ListNode && ((ListNode) innerNode).car() instanceof IdNode) {//id 노드일 경우
                    innerNode = this.archive.get(((ListNode) innerNode).car().toString());
                    if (innerNode == null) {//예외 발생
                        System.out.println("error happen");
                        return null;
                    }
                }
                if (innerNode instanceof QuoteNode) {//QuoteNode에 대해 처리
                    innerNode = this.runQuote((ListNode) innerNode);//QuoteNode의 내부 값 가져오기
                }
                if (innerNode instanceof ListNode && ((ListNode) innerNode).car() instanceof QuoteNode) {
                    innerNode = this.runQuote((ListNode) innerNode);
                }//ListNode에서 QuoteNode를 벚겨낼 경우
                if (innerNode instanceof ListNode && ((ListNode) innerNode).car() != null) {
                    return BooleanNode.FALSE_NODE;//내부에 1개 이상일 경우
                } else {//1개만 있을 경우
                    return BooleanNode.TRUE_NODE;
                }
            case EQ_Q:
                Node firstNode = ((ListNode) this.runExpr(operand)).car();
                Node listNodeOfCdr = this.runExpr(operand.cdr().car());
                if (firstNode instanceof IdNode && listNodeOfCdr instanceof IdNode) {
                    if (firstNode.toString().equals(listNodeOfCdr.toString())) {
                        return BooleanNode.TRUE_NODE;
                    }
                }
                if (firstNode instanceof IdNode) {//id 노드일 경우
                    firstNode = this.archive.get(firstNode.toString());
                    if (firstNode == null) {//예외 발생
                        System.out.println("error happen");
                        return null;
                    }
                }
                if (listNodeOfCdr instanceof IdNode) {//두번쨰 값이 id 노드일 경우
                    listNodeOfCdr = this.archive.get(listNodeOfCdr.toString());
                    if (listNodeOfCdr == null) {
                        System.out.println("error happen");
                        return null;
                    }
                }//값가져와짐

                if (firstNode instanceof ListNode) {//값이 listnode일 경우
                    firstNode = this.runExpr(firstNode);//값에대해 runExpr해준다
                    if (firstNode instanceof ListNode && ((ListNode) firstNode).car() instanceof QuoteNode) {
                        firstNode = this.runQuote((ListNode) firstNode);
                    } else if (firstNode instanceof ListNode) {
                        firstNode = runExpr(((ListNode) firstNode).car());
                    }
                }
                if (listNodeOfCdr instanceof ListNode) {//ListNode일 경우
                    listNodeOfCdr = this.runExpr(listNodeOfCdr);
                    if (listNodeOfCdr instanceof ListNode && ((ListNode) listNodeOfCdr).car() instanceof QuoteNode) {
                        listNodeOfCdr = this.runQuote((ListNode) listNodeOfCdr);
                    } else if (listNodeOfCdr instanceof ListNode) {
                        listNodeOfCdr = runExpr(((ListNode) listNodeOfCdr).car());
                    }
                }
                //  Node secondNode = this.runQuote((ListNode) listNodeOfCdr);
                if (firstNode.toString().equals(listNodeOfCdr.toString())) {//두 값이 동일 한 경우
                    return BooleanNode.TRUE_NODE;
                } else {//주소 떄문에 다를 수 있음
                    return BooleanNode.FALSE_NODE;
                }
            case NOT://Not연산에서 List node일 경우가 있고 일반 boolean Node일 경우 가 있다
                Node resultNode;
                if (operand.cdr() != ListNode.EMPTYLIST) {
                    resultNode = this.runExpr(operand);//ListNode일 경우 다시 노드의 결과를 찾는 방식
                } else {
                    resultNode = operand.car();//Boolean 노드일 경우 Id노드일 경우
                }

                if (resultNode instanceof IdNode) {//id노드일 경우
                    resultNode = this.archive.get(resultNode.toString());
                    if (resultNode == null) {//오류 발생
                        System.out.println("error happen");
                        return null;
                    }
                    resultNode = this.runExpr(resultNode);//runexpr로 결과 값 리턴
                }
                if (resultNode.toString().equals("#F")) {//False일 경우 not이면 TRUE로 간다
                    return BooleanNode.TRUE_NODE;
                } else {
                    return BooleanNode.FALSE_NODE;
                }
            case COND://제일 앞에 True인 Node에 cdr 대해 값을 리턴
                innerNode = this.runExpr(operand.car());
                if (innerNode == null) {//예외 발생
                    break;
                }
                if (innerNode instanceof IdNode) {//id 노드 일 경우 가져온다
                    innerNode = this.archive.get(innerNode.toString());
                    if (innerNode == null) {
                        System.out.println("error happen");// 저의가 안된 경우 error
                        return null;
                    }
                }
                if (innerNode instanceof ListNode && ((ListNode) innerNode).car() instanceof ListNode && ((ListNode) ((ListNode) innerNode).car()).car() instanceof QuoteNode) {
                    return this.runExpr(((ListNode) innerNode).cdr().car());
                }//Quote 예외 1
                if (innerNode instanceof ListNode && ((ListNode) innerNode).car() instanceof QuoteNode) {
                    return this.runExpr((operand).cdr().car());
                }//Quote 예외2
                if (innerNode instanceof ListNode) {//listnode 일 경우
                    Node temp = innerNode;
                    if (((ListNode) innerNode).car() instanceof ListNode) {
                        innerNode = this.runExpr(((ListNode) innerNode).car());
                    }
                    if (innerNode instanceof BooleanNode) {//일반 boolean 노드일 경우
                        if (innerNode.toString().equals("#T")) {//참 결과
                            return this.runExpr(((ListNode) temp).cdr().car());
                        }
                    } else if (innerNode instanceof IntNode) {
                        return this.runExpr(((ListNode) temp).cdr().car());
                    } else if (((ListNode) innerNode).car() instanceof BooleanNode && ((ListNode) innerNode).car().toString().equals("#T")) {//결과 를 얻어옴
                        return this.runExpr(((ListNode) innerNode).cdr().car());//연산
                    } else if (operand.cdr().car() instanceof ValueNode) {
                        return operand.cdr().car();
                    } else {//F밖에없어서 다음 노드 탐색
                        return this.runFunction(operator, operand.cdr());
                    }
                } else {
                    if ((innerNode instanceof BooleanNode && innerNode.toString().equals("#T")) || innerNode instanceof IntNode) {//해당 값이 int노드이거나 boolean노드이 경우
                        if (operand.cdr().car() instanceof ListNode) {
                            return this.runExpr(operand.cdr().car());//연산 일 경우
                        } else if (operand.cdr().car() instanceof IdNode) {
                            Node data = this.archive.get(operand.cdr().car().toString());
                            if (data != null) {
                                return data;
                            }
                            System.out.println("error happen");
                            return null;
                        } else {
                            return operand.cdr().car();//하나의 노드 일 경우
                        }
                    }
                }
                return this.runFunction(operator, operand.cdr());//false일 경우 True가 존재 할 떄 까지 cdr값으로 탐색
            case DEFINE://정의부분
                if (!(operand.car() instanceof IdNode)) {
                    return null;
                }
                IdNode idNodeOfOperandCar = (IdNode) operand.car();//id 노드일경우
                Node runExprResultData = this.runExpr(operand.cdr().car());
                if (runExprResultData instanceof IdNode) {//해당 값에대해 너헝준게 된다
                    Node data = this.archive.get(runExprResultData.toString());
                    if (data != null) {
                        this.insertTable(idNodeOfOperandCar, data);
                    }
                } else if (runExprResultData instanceof BooleanNode || runExprResultData instanceof IntNode) {//id 노드르 제외 한 value노드일 경우
                    this.insertTable(idNodeOfOperandCar, runExprResultData);
                } else if (runExprResultData instanceof ListNode) {//list node일 경우 Quote를 담는 노드만 허용 가능 하므로 그부분을 찾는 부분
                    if (((ListNode) runExprResultData).car() instanceof QuoteNode) {
                        this.insertTable(idNodeOfOperandCar, runExprResultData);
                    }
                }
                return null;
            default:
                break;
        }
        return null;
    }

    private Node stripList(ListNode node) {//cdr list node일 경우 들어 오는 것
        if (node.car() instanceof ListNode && node.cdr() == ListNode.EMPTYLIST) {
            Node listNode = node.car();
            return listNode;//cdr이 비어있을 경우 car만 하나의 리스트 노드로 리턴
        } else {
            return node;//해당 list 노드의 cdr를 계속해서 return
        }
    }

    private Node runBinary(ListNode list) {
        BinaryOpNode operator = (BinaryOpNode) list.car();
        ListNode nextListNode = list.cdr();
        Node firstNode = null;
        Node secondNode = null;

        if (nextListNode.car() instanceof ListNode) {//Node에대해 가져오기
            firstNode = this.runExpr(nextListNode.car());
        }
        if (nextListNode.cdr().car() instanceof ListNode) {
            secondNode = this.runExpr(nextListNode.cdr().car());
        }//cdr의 값 가져오기
        if (firstNode == null) {
            firstNode = nextListNode.car();
        }//null일 경우 car가져오기
        if (secondNode == null) {
            secondNode = nextListNode.cdr().car();
        }//cdr의 car를 가져옴

        if (firstNode instanceof IdNode) {
            firstNode = this.archive.get(firstNode.toString());
            if (firstNode == null || (firstNode instanceof ListNode && ((ListNode) firstNode).car() instanceof QuoteNode)) {
                System.out.println("error happen");
                return null;
            }
        }

        if (secondNode instanceof IdNode) {
            secondNode = this.archive.get(secondNode.toString());
            if (secondNode == null || (secondNode instanceof ListNode && ((ListNode) secondNode).car() instanceof QuoteNode)) {
                System.out.println("error happen");
                return null;
            }
        }

        switch (operator.binType) {
            case PLUS:
                return new IntNode(Integer.toString(((IntNode) firstNode).getValue()
                        + ((IntNode) secondNode).getValue()));//연산
            case MINUS:
                return new IntNode(Integer.toString(((IntNode) firstNode).getValue()
                        - ((IntNode) secondNode).getValue()));//뺴기 연산
            case TIMES:
                return new IntNode(Integer.toString(((IntNode) firstNode).getValue()
                        * ((IntNode) secondNode).getValue()));//곱샘연산
            case DIV:
                return new IntNode(Integer.toString(((IntNode) firstNode).getValue()
                        / ((IntNode) secondNode).getValue()));//나누기 연산 후 노드 생성
            case LT:
                if (((IntNode) firstNode).getValue() < ((IntNode) secondNode).getValue()) {
                    return BooleanNode.TRUE_NODE;//비교 하고 TRUENODE반환
                } else {
                    return BooleanNode.FALSE_NODE;//false노드 반환
                }
            case GT://LT반대로 지정
                if (((IntNode) firstNode).getValue() > ((IntNode) secondNode).getValue()) {
                    return BooleanNode.TRUE_NODE;
                } else {
                    return BooleanNode.FALSE_NODE;
                }
            case EQ://두 값이 같을 경우
                if (((IntNode) firstNode).getValue().equals(((IntNode) secondNode).getValue())) {
                    return BooleanNode.TRUE_NODE;
                } else {
                    return BooleanNode.FALSE_NODE;
                }
            default:
                break;
        }
        return null;
    }

    private Node runQuote(ListNode node) {
        return ((QuoteNode) node.car()).nodeInside();
    }
}