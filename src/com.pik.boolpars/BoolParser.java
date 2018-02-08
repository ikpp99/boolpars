package com.pik.boolpars;
/**
* Parsing of the boolean expression like: (((A|B)&C)|(D|(E&F)))&G
*   inp: String with complex bool expression
*   out: String[] exp  //e.g.{A,B,C...}.
*        int[]    cmd  //sequence to Stack processor( reverse Polish record ),
*                 contains: 0,1,2 - indx of exp[],  -1=|, -2=&
*   i.g. cmd = { 0,1,-1, 2,-2, 3,4,5,-2,-1, -1, 6,-2 }
*                A B |   C &   D E F &  |   |   G &
*                ((A|B) & C)   (D|(E&F))    |
*                (                          )   G &
*                                 
 * @author ven_kilyashenko, v.30.10.2017
 */
import java.util.ArrayList;
import java.util.Arrays;
 
public class BoolParser {
 
       public String[] exp;     // return: expressions[] like A,B,return true/false
       public  int[]   cmd;     // return: program for Stack Processor( use Reverse Polish Record )
 
       private int[]   pol;     // temp cmd
       private int pc, pp, brk, pExp, expBeg;
      
       private StringBuffer s;
       private ArrayList<String> expList;
       static private final int OR=-1, AND=-2, OPN=-3, CLO=-4;
       static private final char aa='\'', qq='\"';
      
       public BoolParser( String bool ) throws Exception {
              if( bool !=null ) {
                     s = new StringBuffer( bool.trim() );
                     expList = new ArrayList<>();
                     cmd = new int[ bool.length() ]; pc=0;
                     int i=0, ins=0;
                    
                     pExp=0; expBeg=-1;
                     while( i < s.length())
                     {                                                             boolean cont=false;
                           char c = s.charAt( i );
                           switch( c )
                           {
                                  case ' ': case '\t': cont=true; break;
                                 
                                  case aa: case qq: int en=s.indexOf( ""+c, i+1 );
                                                       if( en <0 ) Error("absent close "+c);
                                                       s.deleteCharAt( i );
                                                       i=en-1;
                                                       s.deleteCharAt( i );
                                                       cont=true; break;
                                 
                                  case '\\':    if( i<s.length()-1 ) s.deleteCharAt( i++ );
                                                       cont=true; break;
 
                                  case '|':     ins=OR;  break;
                                  case '&':     ins=AND; break;
 
                                  case '(':     ins=OPN; break;
                                  case ')':     ins=CLO; break;
 
                                  default:      if( expBeg<0 ) expBeg=i;            
                                                       cont = true;
                           }
                    
                           boolean last = i==s.length()-1;
                           if( expBeg >-1 && ( !cont || last )){
                                  if( last ) i++;
                                  addExpr( i ); expBeg =-1;
                           }
                           if( ins <0 ){ cmd[pc++] = ins; ins=0;}
                           i++;
                     }
                     if( expBeg >-1 ) addExpr( s.length() );
 
                     exp = new String[ expList.size() ];
                     for( i=0; i<exp.length; i++) exp[i] = expList.get( i );
 
                     pol = new int[ pc ];  pc=pp=brk=0;
                     polish();
                     if( brk !=0 ) Error("Bracket Structure");
                     cmd = Arrays.copyOf( pol, pp );
                    
                     s=null; expList=null;
              }
       }
       private void addExpr( int x ){
              while( x > expBeg ){
                     char c=s.charAt( --x );
                     if( c!=' ' && c!='\t' && c!=')') break;
              }
              if( x >= expBeg ){
                     expList.add( s.substring( expBeg, x+1 ));
                     cmd[pc++] = pExp++;
              }
       }
      
       private void polish() throws Exception {
              boolean start=true;
              int op=1, val=0, beg=pc;
              while( pc < pol.length ){
                     val = cmd[ pc++ ];
                     if(      val==OPN ){
                           brk++;
                           polish();
                           if( beg+1< pc ) start=false;
                     }
                     else if( val==CLO ){
                           if( op <0 ){
                                  int t = cmd[ pc-2 ];
                                  if( t==OR || t==AND ){ pc--; Error("Bool. before')'");}
                                  pol[ pp++ ] = op; op=1;
                           }
                           brk--;
                           return;
                     }
                     else if( start ){
                           if( val <0 ) Error("Bool. before/after'('");
                           start = false;
                           pol[ pp++ ] = op = val;
                     }
                     else {
                           if( val <0 ){
                                  if( op <0 ) Error("2 Bool.Op.");
                                  op = val;
                           }
                           else {
                                  if( op >0 ) Error("2 Args.");
                                  pol[ pp++ ] = val;
                                  pol[ pp++ ] = op;  op=1;
                           }
                     }
              }
              if( op < 0 ) pol[ pp++ ] = op;
       }
 
       public boolean exec( iCalcOneBoolExpr boolCalc ) throws Exception {
              int x = cmd.length;
              if( x >-1 ){
                     stack = new boolean[ x ];
                     for(int i=0;i<cmd.length;i++){
                           int op = cmd[i] ;
                           if( op >-1 ) push( boolCalc.calcOneBoolExpr( exp[ op ]));
                           else         boolOp( op );  
                     }
                     return pop();
              }
              return false;
       }
 
       private boolean[] stack;
       private int sp=0;
       private void    push( boolean val ) throws Exception { stack[sp++]=val;}
       private boolean pop ()              throws Exception { return stack[--sp];}
       private void    boolOp( int op )    throws Exception {
              boolean aa=pop(), bb=pop();
              push( op==OR? aa | bb: aa & bb );
       }
       private void Error( String s ) throws Exception { throw new Exception( s+" "+(pc-1));}
}
