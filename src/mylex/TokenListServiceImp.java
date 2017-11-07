package mylex;
/**
 * Created by 61990 on 2017/10/25.
 */
import mylex.exception.NotTokenException;
import mylex.process.*;
import mylex.exception.NotFoundREsException;
import mylex.exception.NotREsException;
import mylex.exception.WrongSort;
import mylex.utility.StaticVal;
import mylex.utility.Token;
import mylex.utility.Token_RE;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TokenListServiceImp implements TokenListService{
    List<Token> tokensList =new ArrayList<>();
    List<String> expr = new ArrayList<>();
    @Override
    public List<Token> getTokenList()
    {
        String path = "files/REs.l";
        RE_collection handler = new RE_collection(path);
        try {
            handler.createREs();
            handler.postfixERs();
        }catch (IOException e1){
            e1.printStackTrace();
        }catch (NotFoundREsException e2){
            e2.printStackTrace();
        }catch (WrongSort e3){
            e3.printStackTrace();
        }catch (NotREsException e4){
            e4.printStackTrace();
        }
        NFA_collection handleRE = new NFA_collection(handler.getExpressions());
        for (Token_RE re : handler.getExpressions()) {
            expr.add(re.getToken());
        }
        DFA_collection handle = null;
        try {
            handleRE.handle_RE_to_NFA();
            handle = new DFA_collection(handleRE.getNFA_Expressions());
            handle.handle_NFA_to_DFA();
            handle.minDFA();
//            int i =1+1;
        } catch (NotREsException e) {
            e.printStackTrace();
        }
        //至此得到所有的Token
        HandleTxt handleTxt=null;
        List<List<Token>> tokenLists=null;
        try {
            handleTxt = new HandleTxt("files/input.txt", handle);
            tokenLists = handleTxt.getTokens();
        } catch (NotTokenException e) {
            e.printStackTrace();
        }

        try {

            FileWriter fw = new FileWriter(new File("files/output.txt").getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (List<Token> tokens : tokenLists) {
                for (Token token : tokens) {
                    if (!token.getToken().equals("ε")) {
//                        System.out.print(token.getAllToken());
//                        bw.write(token.getAllToken()+"\t"+token.getYylval()+"\t"+token.getText()+"\n");
                            tokensList.add(token);

                    }


                }
                System.out.println();
            }
            bw.close();
        }catch (Exception e){
            System.out.println("写文件出错");
        }

        return tokensList;
    }

    @Override
    public List<String> getAllToken() {
        return this.expr;
    }
}
