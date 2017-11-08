package myLL1;

import myLL1.exception.ConflictHappen;
import myLL1.exception.InputError;
import myLL1.exception.LeftRecursionException;
import myLL1.process.Basic;
import myLL1.process.Monitor;
import myLL1.process.PPT_creator;
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
        PPT_creator ppt_creator;
        Monitor monitor ;
        try {
            basic.create();
            ppt_creator = new PPT_creator(basic.getExpressions());
            ppt_creator.initPPT();
            ppt_creator.check();
            monitor = new Monitor(tokens,ppt_creator.getPpt_items());
            monitor.run();
            monitor.writeFile();
        } catch (IOException | ConflictHappen | LeftRecursionException | NotFoundREsException | InputError e) {
            e.printStackTrace();
        }

    }
}
