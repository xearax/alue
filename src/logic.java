import java.io.*;
import java.util.Vector;
import jlibs.core.lang.Ansi;

public class logic {
	private Vector<verdict> classificationResult;
	private File path;
	private String modelPath;
	private String arffPath;
	private String classifierInfo;
	
	public logic( String inPath, String inModelPath, String inArffPath ) {
		path = new File( inPath );
		classificationResult = new Vector<verdict>();
		modelPath = inModelPath;
		arffPath = inArffPath;
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
                String mPath = inPath.getAbsolutePath();
                if(!mPath.substring(mPath.lastIndexOf(".")).equalsIgnoreCase(".txt")){
                    int rtn = Xtractor.start();
                    if ( rtn != 0 ) {
                        misc.log( "Error: extractor failure. " + rtn);
                        System.exit( 1 );
                    }
                    license = Xtractor.getLicense();
                }else{
                    license = Xtractor.readFile(mPath);
                }
				preprocess pprocess = new preprocess( license, arffPath );
				Xtractor.clean();
                    
				if ( pprocess.start() != 0 ) {
					misc.log( "Error: preprocess failed for file: " + inPath.getAbsolutePath() );
					System.exit( 1 );
				}
				
				classifier cfier = new classifier( modelPath, pprocess.getInstances() );
				if ( cfier.start() != 0 ) {
					misc.log( "Error: classification failed." );
					System.exit( 1 );
				}
				
				classificationResult.add( new verdict( inPath.getAbsolutePath(), cfier.getVerdict(),  cfier.getVerdictClass()) );
				
				String sClass = cfier.getVerdictClass();
				
				if ( sClass.compareToIgnoreCase( "spyware" ) != 0 ) {
					Ansi red = new Ansi(Ansi.Attribute.BRIGHT, Ansi.Color.RED, null );
					sClass = red.colorize("SPYWARE");
				} else if ( sClass.compareToIgnoreCase( "legit" ) != 0 ) {
					Ansi red = new Ansi(Ansi.Attribute.BRIGHT, Ansi.Color.GREEN, null );
					sClass = red.colorize(" LEGIT ");
				} 
				System.out.println( "  [" + sClass + "]  :  " + inPath.getAbsolutePath() );
			}
		} else {
			misc.log( "Info: " + inPath.getAbsolutePath() + " is neither file nor directory." );
		}
	}
	
	public Vector<verdict> getVerdict() {
		return classificationResult;
	}
	
	public String getClassifierInfo() {
		return classifierInfo;
	}
}