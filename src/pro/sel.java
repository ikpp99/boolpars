package pro;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
 
import com.pik.boolpars.BoolParser;
import com.pik.boolpars.iCalcOneBoolExpr;
 
public class sel implements iCalcOneBoolExpr
{
       public static void main( String... a ) {
              try { new sel( a );} catch( Exception e ) { e.printStackTrace();}
              finally {
                     try { fr.close();} catch( Exception e ) {}
                     try { fw.close();} catch( Exception e ) {}
              }
       }
       static BufferedReader fr=null;
       static FileWriter fw=null;
      
       private String stringToSearch;
       @Override public boolean calcOneBoolExpr( String exp ) {
              if( exp.startsWith("!")) return !stringToSearch.contains( exp.substring( 1 ).trim() );
              return stringToSearch.contains( exp );
       }
      
       public sel( String... a ) throws Exception
       {
        if( a.length < 2 ) { tt( "\n\t> sel  File  <conditions(%='|'or'&' ): \"cond1[ % cond2 [ % cond3 ]]...\">  [<out: \"n[-1=all][|arg1 [ |arg2...]]\">]" ); return;}
        fr = new BufferedReader( new FileReader( a[0] ));
       
        BoolParser reason = new BoolParser( a[1] );
       
        ArrayList<String> out  = pars( a.length>2? a[2]:"-1", '|');
       
        String fout = a[1].replaceAll(" ","");
        fout = fout.substring(1).replaceAll("\\W","x")
                     +(new SimpleDateFormat("_HHmmss")).format( Calendar.getInstance().getTime())+".sel";
       
        fw = new FileWriter( fout );
        String s="$ sel"; char d=' ';
        for( String q: a){ s+="  "+d+q+d; d='\"';}
        fw.write( s+"  >  "+fout+"\n\n");
//if(1==1)return;
       
        while( (stringToSearch = fr. readLine()) != null ){
              if( stringToSearch.length() >0 ){
                     if( reason.exec( this )) {
                           String r="";
                           for( String q: out ){
                                  int aa=0, ee=0;
                                  if( q.equals("-1") ){ r=stringToSearch; break;}
                                  else if( (ee=getInt( q )) >0 ){}
                                  else {
                                         aa = stringToSearch.indexOf( q );
                                         if( aa<0) ee = aa;
                                         else      ee = stringToSearch.indexOf(":",aa+1);  if( ee<0 ) ee=stringToSearch.length();
                                  }
                                  if( stringToSearch.length()<ee ) ee=stringToSearch.length();
                                  if( aa>-1 && aa<ee ) {
                                  String bae =  stringToSearch.substring( aa, ee ).trim();
                                  int x = bae.length();
                                  if( x > 0 && bae.charAt(x-1) !=';') bae+=';';
                                         r += bae+" ";
                                  }
                           }
                           fw.write( r+"\n");
                     }
              }
        }
        tt("\n\tFile:  "+fout+"  is created.\n");
       }
      
       private int getInt( String s ){ try{ int i=Integer.parseInt(s); return i;} catch(Exception e){ return -1;}}
      
       private ArrayList<String> pars( String s, char dlm ) {
              ArrayList<String> lst = new ArrayList<String>();
              int i=0, x=s.length();
              while( i<x ){
                     int e=s.indexOf( dlm, i ); if( e < 0 ) e=x;
                     String f = s.substring( i,e ).trim();
                     if( f.length()>0 ) lst.add( f );
                     i=e+1;
              }
              return lst;
       }
       static private void tt(  String s ){ System.out.println( s );}
}
