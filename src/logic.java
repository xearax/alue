import java.io.*;
import java.util.Vector;

public class logic {
	private Vector<verdict> classificationResult;
	private File path;
	private String modelPath;
	
	public logic( String inPath, String inModelPath ) {
		path = new File( inPath );
		classificationResult = new Vector<verdict>();
		modelPath = inModelPath;
	}

	public int start() {		
		
		traverse( path );
		
		return 0;
	}
	
	private boolean isFileOK( File inPath) {
		
		if ( inPath.canRead() ) {
			// Also check if file has supported file suffix, e.g. .exe, .msi ...
			return true;
		}
		else {
			misc.log( "Info: " + inPath.getAbsolutePath() + "is not readable." );
			return false;
		}
	}
	
	private void traverse( File inPath ) {
		if ( inPath.isDirectory() ) {
			File fileList[] = inPath.listFiles();
			
			for (int count=0; count < fileList.length; count++ ) {
				traverse( fileList[ count ] );
			}
		} else if ( inPath.isFile() ) {
			if ( isFileOK( inPath ) ) {
                String license;
                extractor Xtractor = new extractor( inPath.getAbsolutePath() );
                if(!inPath.getAbsolutePath().substring(inPath.getAbsolutePath().lastIndexOf(".")).equalsIgnoreCase("txt")){
                    int rtn = Xtractor.start();
                    if ( rtn != 0 ) {
                        misc.log( "Error: extractor failure. " + rtn);
                        System.exit( 1 );
                    }
                    license = Xtractor.getLicense();
                }else{
                    license = Xtractor.readFile(inPath.getAbsolutePath());
                }
				preprocess pprocess = new preprocess( license );
				Xtractor.clean();
                    
				if ( pprocess.start() != 0 ) {
					misc.log( "Error: preprocess failed for file: " + inPath.getAbsolutePath() );
					System.exit( 1 );
				}
				
				classifier cfier = new classifier( modelPath, pprocess.getInstances() );
				cfier.start();
				classificationResult.add( new verdict( inPath.getAbsolutePath(), cfier.getVerdict(),  cfier.getVerdictClass()) );
			}
		} else {
			misc.log( "Info: " + inPath.getAbsolutePath() + "is neither file nor directory." );
		}
	}
	
	public Vector<verdict> getVerdict() {
		return classificationResult;
	}
}