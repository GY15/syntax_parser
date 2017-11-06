package mylex;

import mylex.exception.NotTokenException;
import mylex.process.*;
import mylex.utility.Token;
import mylex.utility.Token_DFA;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 61990 on 2017/10/28.
 */
public class HandleTxt {
    //记录每一行的token
    List<List<Token>> tokens;
    public HandleTxt(String path, DFA_collection collection) throws NotTokenException{
        File inputFile = new File(path);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inputFile));
            String temp = null;
            int line = 0;
            tokens= new ArrayList<>();
            while ((temp = br.readLine()) != null) {
                if(temp.equals(""))
                temp +="\n";
                List<Token> tokenList = collection.getTokenList(temp,line);
                tokens.add(tokenList);
                line++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<List<Token>> getTokens() {
        return tokens;
    }
}
