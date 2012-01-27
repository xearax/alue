import java.util.*;
import java.security.*;


public class extractor{

    private Vector<String> dirStack;
    private int type;
    private String license = "";
    private String path = "";
    
    
    
    public extractor(String mPath){
        dirStack = new Vector<String>();
        path = mPath;
    }
    
    public int start(){
        return 1;
    }
    
    public String getLicense(){
        return license;        
    }
    
    private int getType(){
        return 1;
    }
    
    private String extract(){
        return "";
    }
    
    private boolean clean(){
        boolean code=false;
        for(int i=0; i< dirStack.size();i++)
            code = delete(new File(dirStack.get(i)));
        return code;
    }
    
    private String extractMSI(String path){
        return "";
        
    }
    
    private String extractInno(String path){
        return "";
        
    }
    
    private String extractNSIS(String path) throws SecurityException{
        
        @unPackDir << unpackdir
        path = extractEscape(path)
        s=%x[7z -p1 x -y -o#{unpackdir} "#{path}"]
        return unpackdir
        return "";
        
    }
    
    private String extractInstallExplorer(String path){
        return "";
        
    }
    
    private boolean mktmp(){
        byte[] bytesOfMessage = path.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        String thedigest = md.digest(bytesOfMessage).toString();
        String tmpDir = "/tmp/"+thedigest;
        
        File tmp = new File(tmpDir);
        if(tmp.mkdir()){
            dirStack.add(tmpDir);
            return true;
        }
        return false;
    }
    
    //http://stackoverflow.com/a/779529
    private void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }

    
    private Vector<String> match(String text){
        Vector<String> data = new Vector<String>();
        int start = -1;
        int count = 0;
        for(int i=0; i<text.length(); i++){
            if(text.charAt(i) == '{'){
                start = (start != -1)?i:start;
                count++;
            }else if(text.charAt(i) == '}'){
                count--;
            }
            if((start > -1) && (count == 0)){
                data.add(text.substring(start+1, i-1));
                start = -1;
            }
        }
        return data;
    }
    
    private String escape(String path){
        
        if(path.contains("$")){
            path = path.replaceAll("$", "\\$");
        }else if(path.contains("!")){
            path = path.replaceAll("!", "\\!");
        }
        return path;         
    }
                 
}