import java.io.*;
import java.util.Vector;

public class main {
	private static Vector<verdict> result;
	private static String modelPath = "";
	
	public static void main( String args[] ) {
		
		if ( parse( args ) != 0 ) {
			System.err.println( "Exiting." );
			System.exit( 1 );
		}
		
		if ( dependencies() != 0 ) {
			System.err.println( "Exiting." );
			System.exit( 2 );
		}
		
		logic alue = new logic( args[ 0 ], modelPath );
		alue.start();
		result = alue.getVerdict();
		
		// Temporarily printing the verdict vector containing all paths and classification results
		for ( int a=0; a<result.size(); a++ )
			System.out.println( "+ " + result.get(a).toString() );
	}
	
	private static int parse( String inArgs[] ) {
		if ( inArgs.length > 0 )
			if ( inArgs[ 0 ].length() > 0 )
				return 0;
			else if ( inArgs.length > 2 ) {
				if ( inArgs[ 1 ].equalsIgnoreCase( "-c" ) ) {
					modelPath = inArgs[ 2 ];
				}
			}
		
		System.err.println( "Usage: ./alue <path-to-eula-file-or-directory> [-c <path-to-model-file>]" );
		
		return 1;
	}
	
	private static int dependencies() {
		String dependencies[] = {"trid", "7z" };
		int result = 0;
		
		for ( int count=0; count < dependencies.length; count++ ) {
			File test = new File( dependencies[ count ] );
			
			if ( ! ( test.exists() && test.isFile() && test.canExecute() ) ) {
				System.err.println( "Dependency check failed for: " + dependencies[ count ] );
				result = 2;
			}
		}
		
		return result;
	}
}