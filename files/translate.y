expr : expr + term
      | expr
      ;
term : term + factor
      | factor
       ;
factor : ( expr )
      | NUMBER