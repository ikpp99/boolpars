package com.pik.boolpars;
 
public class TextSearch implements iCalcOneBoolExpr
{
       private String stringToSearch;
       static void tt( String s ){ System.out.println( s );}
       static public void main( String... a ) throws Exception{ new TextSearch();}
       TextSearch() throws Exception{
 
              BoolParser pars = new BoolParser(" abc & ! \" de \" ");
              tt( testBool.ttl( pars.exp ));
              testBool.ttCmd( pars.cmd, pars.cmd.length );
              stringToSearch = "...abc__de+++"; tt( stringToSearch+" -> "+pars.exec( this ));
              stringToSearch= "...abc__d&e+++"; tt( stringToSearch+" -> "+pars.exec( this ));
       }
      
       @Override public boolean calcOneBoolExpr( String exp )
       {
              if( exp.startsWith("!")) return !stringToSearch.contains( exp.substring( 1 ).trim() );
              return stringToSearch.contains( exp );
       }
}
