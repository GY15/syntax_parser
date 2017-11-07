package myLL1;

import myLL1.exception.LeftRecursionException;
import myLL1.process.Basic;
import myLL1.process.PPT_creator;
import mylex.TokenListService;
import mylex.TokenListServiceImp;
import mylex.exception.NotFoundREsException;
import mylex.utility.StaticVal;
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
        PPT_creator ppt_creator;
        try {
            basic.create();
            ppt_creator = new PPT_creator(basic.getExpressions());
            ppt_creator.initPPT();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundREsException e) {
            e.printStackTrace();
        }catch (LeftRecursionException e){
            e.printStackTrace();
        }
    }
}
