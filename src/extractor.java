import java.util.*;
import java.security.*;
import java.io.*;


public class extractor{

    private Vector<String> dirStack;
    private int type;
    private String license = "";
    private String path = "";
    
    private List<String> codes = Arrays.asList(".dll",".msi", ".exe", ".cab", ".rtf", ".txt", ".rar", ".7z", ".zip");
    
    public extractor(String mPath){
        dirStack = new Vector<String>();
        path = mPath;
    }
    
    public int start(){
        return traverse(path);
    }
           
    private int traverse(String mPath){
        int error = 0;
        File f = new File(mPath);
        if(f.isDirectory()){
            for (File c : f.listFiles()){
               error = traverse(c.getPath());
            }
        }else{
            error = extract(mPath);
        }
        return error;   
    }
    
    public String getLicense(){
        return license;        
    }
    
    private int getType(String mPath){
        int returnCode = -1;
        String type = "";
        try{
            type=kommand("./trid -d:TrIDDefs.TRD -r:10 \""+mPath+"\"");
            for(String n : type.split("\n")){
                if(n.matches("% (.*)")){
                    type = n.toLowerCase();
                    break;
                }
            }
        }catch(Exception e){
            
        }
        if(mPath.substring(mPath.lastIndexOf(".")).equalsIgnoreCase(".msi") || mPath.substring(mPath.lastIndexOf(".")).equalsIgnoreCase(".mst"))
            returnCode = 2;
        else if(type.contains("inno")){
            returnCode = 1;
        }else if(type.contains("msi") || type.contains("wise") || type.contains("vise")){
            returnCode = 2;
        }else if(type.contains("nsis")){
            returnCode = 3;
        }
        
        return returnCode;
    }
    
    private int extract(String mPath){
        if(!codes.contains(mPath.substring(mPath.length()-5)))
            return 23;
        if(mktmp(mPath)){
            String tmpDir = dirStack.lastElement();
            mPath = escape(mPath);
            int error = 0;
            int type = getType(path);
            switch(type){
                case 1:
                    if(extractNSIS(mPath, tmpDir))
                        traverse(tmpDir);
                    else
                        error = 23;
                    break;
                case 2:
                    license+=extractMSI(mPath, tmpDir);
                    break;
                case 3:
                    if(extractInno(mPath, tmpDir))
                        traverse(tmpDir);
                    else
                        error = 23;
                    break;
                default:
                    break;
            }
            return error;
            
        }else{
            //throw new SecurityException("Failed to create temp directory for: "+path);
            return 21;
        }

    }
    
    private String kommand(String cmd) throws InterruptedException{
        try{
            Process p=Runtime.getRuntime().exec(cmd); 
            p.waitFor(); 
            BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
            String line=reader.readLine(); 
            while(line!=null) 
            { 
                System.out.println(line); 
                line=reader.readLine(); 
            } 
            return line;
        }
        catch(IOException e1) {
            throw new InterruptedException("Failed to run command: "+e1);
        } 
    }
    
    private boolean clean(){
        boolean code=false;
        for(int i=0; i< dirStack.size();i++){
            try{
                delete(new File(dirStack.get(i)));
                
            }catch(Exception e){}
        }
        return code;
    }
    
    private String readFile(String mPath){
        String line="", str="";
        try{
            BufferedReader in = new BufferedReader(new FileReader(mPath));
            while ((str = in.readLine()) != null) {
                line +=str;
            }
            in.close();
        }catch(Exception e){}
        return line;
    }
    
    private String extractMSI(String mPath, String tmpDir){
        String line ="";
        try{
            kommand("7z x -y -o"+tmpDir+" "+mPath);
        }catch(Exception e){
            return line;
        }
        if(new File(tmpDir+"/!_StringData").exists()){
            line = readFile(tmpDir+"/!_StringData");

            Vector<String> s = match(line);
            for(String str : s){
                if(str.contains("\rtf"))
                    line = str;
            }
        }
        
        return line;
        
    }
    
    private boolean extractInno(String mPath, String tmpDir){
        return false;
        
    }
    
    private boolean extractNSIS(String mPath, String tmpDir){
        boolean s = false;
        try{
            kommand("7z -p1 x -y -o"+tmpDir+" \""+path+"\"");
            s = true;
        }catch(Exception e){
            
        }
        return s;
        
    }
    
            
            
    private boolean mktmp(String mPath){
        try{
            byte[] bytesOfMessage = mPath.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            String thedigest = md.digest(bytesOfMessage).toString();
            String tmpDir = "/tmp/"+thedigest;
            
            File tmp = new File(tmpDir);
            if(tmp.mkdir()){
                dirStack.add(tmpDir);
                return true;
            }
        }catch(Exception e){
            return false;
        }
        return true;
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