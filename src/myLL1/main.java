package myLL1;

import myLL1.process.Basic;
import mylex.TokenListService;
import mylex.TokenListServiceImp;
import mylex.exception.NotFoundREsException;
import mylex.utility.Token;

import java.io.IOException;
import java.util.List;

/**
 * Created by 61990 on 2017/11/6.
 */
public class main {
    public static void main(String[] args){
        TokenListService service = new TokenListServiceImp();
        List<Token> tokens = service.getTokenList();
        List<String> allTokenString =service.getAllToken();
        Basic basic = new Basic(allTokenString);
        try {
            basic.create();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundREsException e) {
            e.printStackTrace();
        }
    }
}
