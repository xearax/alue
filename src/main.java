import java.io.*;
import java.util.Vector;

public class main {
	private static Vector<verdict> result;
	private static String modelPath = "rf.model";
	private static String arffPath ="arffMap.arff";
	private static String analyzePath;
	private static String classifierInfo;
	
	public static void main( String args[] ) {
		
		if ( parse( args ) != 0 ) {
			System.exit( 1 );
		}
		
		if ( dependencies() != 0 ) {
			System.exit( 2 );
		}
		
		printDisclaimer();
		
		logic alue = new logic( analyzePath, modelPath, arffPath );
		alue.start();
		result = alue.getVerdict();
		if ( (classifierInfo = alue.getClassifierInfo() ) == null ) {
			classifierInfo = "<not available>";
		}
		
		if ( result == null || result.isEmpty() ) {
			printUsage();
		}
	}
	
	private static int parse( String inArgs[] ) {
		int returnValue = 1;
		
		switch ( inArgs.length ) {
			case 0:
				break;
			case 1:
				analyzePath = inArgs[ 0 ];
				returnValue = 0;
			case 2:
				break;
			case 3:
				if ( inArgs[ 1 ].equalsIgnoreCase( "-c" ) && inArgs[ 2 ].length() > 0 ) {
					analyzePath = inArgs[ 0 ];
					modelPath = inArgs[ 2 ];
					returnValue = 0;
				} else if ( inArgs[ 1 ].equalsIgnoreCase( "-s" ) && inArgs[ 2 ].length() > 0 ) {
					analyzePath = inArgs[ 0 ];
					arffPath = inArgs[ 2 ];
					returnValue = 0;
				} else
					break;
			case 4:
				break;
			case 5:
				if ( inArgs[ 1 ].equalsIgnoreCase( "-c" ) && inArgs[ 2 ].length() > 0 ) {
					if ( inArgs[ 3 ].equalsIgnoreCase( "-s" ) && inArgs[ 4 ].length() > 0 ) {
						analyzePath = inArgs[ 0 ];
						modelPath = inArgs[ 2 ];
						arffPath = inArgs[ 4 ];
						returnValue = 0;
					}
					else {
						break;
					}
				}
				else if ( inArgs[ 1 ].equalsIgnoreCase( "-s" ) && inArgs[ 2 ].length() > 0 ) {
					if ( inArgs[ 3 ].equalsIgnoreCase( "-c" ) && inArgs[ 4 ].length() > 0 ) {
						analyzePath = inArgs[ 0 ];
						arffPath = inArgs[ 2 ];
						modelPath = inArgs[ 4 ];
						returnValue = 0;
					}
					else {
						break;
					}
				}
			default:
				break;
		}	
		
		if ( returnValue != 0 ) {
			printUsage();
			return 1;
		} 
		
		return 0;
	}
	
	private static int dependencies() {
		String dependencies[] = {"trid", "7z", "unrtf" };
		int result = 0;
		
		for ( int count=0; count < dependencies.length; count++ ) {
			File test = new File( dependencies[ count ] );
			
			if ( ! ( test.exists() && test.isFile() && test.canExecute() ) ) {
				System.err.println( " Dependency check failed for: " + dependencies[ count ] );
				result = 2;
			}
		}
		
		return result;
	}
	
	private static void printUsage() {
		System.err.println( "Usage: ./alue <path-to-analyze> [[-c <path-to-model>] [-s <path-to-structure>]]" );
	}
	
	private static void printDisclaimer() {
		System.out.println( "" );
		System.out.println( "                                ----=[ DISCLAIMER ]=---" );		
		System.out.println( "   All software available from this website is copyright protected © 2012 by A. Borg and   " );
		System.out.println( "              M. Boldt at Blekinge Institute of Technology (BTH), Sweden.                  " );
		System.out.println( "                                                                                           " );
		System.out.println( "    As well, all software is provided on an 'as is' basis without warranty of any kind,    " );
		System.out.println( "                                 express or implied.                                       " );
		System.out.println( "                                                                                           " );
		System.out.println( "   Under no circumstances and under no legal theory, whether in tort, contract, or other-  " );
		System.out.println( "   wise, shall BTH, A. Borg, or M. Boldt be liable to you or to any other person for any   " );
		System.out.println( "    indirect, special, incidental, or consequential damages of any character including,    " );
		System.out.println( "   without limitation, damages for loss of goodwill, work stoppage, computer failure or    " );
		System.out.println( "  malfunction, or for any and all other damages or losses. If you do not agree with these  " );
		System.out.println( "                  terms, then you you are advised to not use the software.                 " );
		System.out.println( "" );
		System.out.println( "                                         ----" );
		System.out.println( "" );
	}
}