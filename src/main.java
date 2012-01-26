
public class main {
	
	public static void main( String args[] ) {
		
		if ( parse( args ) != 0 ) {
			System.exit( 1 );
		}
		
		if ( dependencies() != 0 ) {
			System.exit( 1 );
		}
		
		logic analyzer = new logic( args[ 0 ] );
		analyzer.start();

		//preprocess preprocessor = new preprocess()
	}
	
	private static int parse( String inArgs[] ) {
		
		return 1;
	}
	
	private static int dependencies() {
		// TrID, 7Zip, unrtf
		
		return 1;
	}

}