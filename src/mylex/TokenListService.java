package mylex;

import mylex.utility.Token;

import java.util.List;

/**
 * Created by 61990 on 2017/11/6.
 */
public interface TokenListService {
    List<Token> getTokenList();
    List<String> getAllToken();
}
