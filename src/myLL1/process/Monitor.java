package myLL1.process;

import myLL1.exception.InputError;
import myLL1.utility.*;
import mylex.utility.Token;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by 61990 on 2017/11/8.
 */
public class Monitor {
    //栈
    Stack<Sign> stack;
    //输入
    List<Token> tokens;
    List<PPT_item> ppt_items;
    //保存推导序列
    List<SequenceOfAction> actions;

    public Monitor(List<Token> tokens, List<PPT_item> ppt_items) {
        stack = new Stack<>();
        actions = new ArrayList<>();
        this.stack.push(StaticVal.endOfSentence);
        this.stack.push(StaticVal.firstSign);
        this.tokens = tokens;
        this.ppt_items = ppt_items;
    }

    /**
     * 运行monitor 计算推导序列
     */
    public void run() throws InputError {
        int ip = 0;
        while(!stack.peek().equals(StaticVal.endOfSentence)){
            if(stack.peek().isTerminal()){
                if(stack.peek().getVal().equals(tokens.get(ip).getToken())) {
                    actions.add(new SequenceOfAction("匹配",tokens.get(ip)));
                    ip++;
                    stack.pop();
                }else {
                    throw new InputError();
                }
            }else{
                Derivation derivation = matchM(stack.peek(),tokens.get(ip).getToken());
                if(derivation!=null){
                    actions.add(new SequenceOfAction("输出",derivation));
                    stack.pop();
                    List<Sign> signs = derivation.getTail();
                    for (int i = signs.size()-1;i>=0;i--){
                        if(!signs.get(i).equals(StaticVal.getNullSign())) {
                            stack.push(signs.get(i));
                        }
                    }
                }else{
                    throw new InputError();
                }
            }
        }
    }

    private Derivation matchM(Sign peek, String token) {
        for (PPT_item ppt_item:ppt_items){
            if(ppt_item.getStart().equals(peek) && ppt_item.getEnd().getVal().equals(token)){
                return ppt_item.getDerivation();
            }
        }
        return null;
    }

    public void writeFile() {

        try {

            FileWriter fw = new FileWriter(new File("files/output.txt").getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

                for (SequenceOfAction action : actions) {
                        bw.write(action.toString()+"\n");
                }
                System.out.println();
            bw.close();
        }catch (Exception e){
            System.out.println("写文件出错");
        }
    }
}
