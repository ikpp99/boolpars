package com.pik.boolpars;
 
public class testBool
{
       static public void main( String... a ) throws Exception {
             
              int Narg = 8;  boolean[] ab = new boolean[ Narg ];
              String bool="((((A|B)&C)|(D|(E&F)))&(G|H))&(A|D|H)";
 
              BoolParser pars = new BoolParser( bool );  //######
              String exps="exp[]"; for( String ex: pars.exp ) exps+=", "+ex;
              tt( bool+"\n"+exps ); ttCmd( pars.cmd, pars.cmd.length );
 
              BoolArray boolAr = new BoolArray();
 
              for( int v=0; v < Math.pow( 2, Narg ); v++){
                     int mask = 1;
                     for( int i=0; i<Narg; i++){ ab[i] = ( mask & v ) > 0; mask*=2;}
                     boolAr.set( ab );
                     String s=""; for( boolean q: ab ) s+=(q?"T":"f");
                    
                     boolean par = pars.exec( boolAr ),
                     check = ((((ab[0]|ab[1])&ab[2])|(ab[3]|(ab[4]&ab[5])))&(ab[6]|ab[7]))&(ab[0]|ab[3]|ab[7]);
                    
                     tt( v+"\t"+s +" "+ par +" ? " + check );
                     if( par != check ) tt("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ERROR !!!");
              }
       }
 
       static void tt( String s ){ System.out.println( s );}
       public static String ttl( String[] ss ){ String r="";int i=0; for( String s: ss ) r+="\n"+(i++)+":\t"+s; return r+"\n";}
       public static void ttCmd( int[] ar ){ int x=ar.length; ttCmd( ar, x );}
       public static void ttCmd( int[] ar, int n ){
              String s="int[]:"; for( int i=0; i<n; i++) s+=" "+ar[i];
              tt( s );
       }
 
       static class BoolArray implements iCalcOneBoolExpr{
              private boolean[] ab;
              public void set( boolean[] AB ){ ab = AB;}
             
              @Override public boolean calcOneBoolExpr( String s ) {
                     int i = (int) s.charAt( 0 ) - 'A';
                     return ab[ i ];
              }
       }
}
