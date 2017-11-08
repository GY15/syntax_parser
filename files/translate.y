expr : expr + term
      | term
      ;
term : term * factor
      | factor
       ;
factor : ( expr )
      | NUMBER
      ;