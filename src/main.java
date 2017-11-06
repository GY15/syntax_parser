import mylex.TokenListService;
import mylex.TokenListServiceImp;
import mylex.utility.Token;

import java.util.List;

/**
 * Created by 61990 on 2017/11/6.
 */
public class main {
    public static void main(String[] args){
        TokenListService service = new TokenListServiceImp();
        List<Token> tokens = service.getTokenList();
    }
}
