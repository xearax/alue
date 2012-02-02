import java.util.*;
import java.security.*;
import java.io.*;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.text.*;
import java.util.UUID;



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
                if(n.contains("% (.")){
                    //System.out.println(n); //Uncomment to output the type of a file.
                    type = n.toLowerCase();
                    break;
                }
            }
        }catch(Exception e){
            misc.log("Error: getType(); "+e.toString());
        }
        if(mPath.substring(mPath.lastIndexOf(".")).equalsIgnoreCase(".msi") || mPath.substring(mPath.lastIndexOf(".")).equalsIgnoreCase(".mst"))
            returnCode = 2;
        else if(type.contains("inno")){
            returnCode = 1;
        }else if(type.contains("msi") || type.contains("wise") || type.contains("vise") || type.contains("mst")){
            returnCode = 2;
        }else if(type.contains("nsis")){
            returnCode = 3;
        }
        
        return returnCode;
    }
    
    private int extract(String mPath){
        if(!codes.contains(mPath.substring(mPath.length()-4)))
            return 23;
    
        if(mPath.contains("license") || mPath.contains("eula")){
            if(mPath.contains(".txt")){
                license = readFile(mPath);
                return 0;
            }else if(mPath.contains(".rtf")){
                license = deRTF(readFile(mPath));
                return 0;
            }
        }
        
        String tmpDir = mktmp(mPath);
        if(!tmpDir.equalsIgnoreCase("false")){
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
                    error = extractMSI(mPath, tmpDir);
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
            misc.log("Error: extract(); ");
            //throw new SecurityException("Failed to create temp directory for: "+path);
            return 21;
        }

    }
    
    private String kommand(String cmd) throws InterruptedException{
        String data = "";
        try{
            Process p=Runtime.getRuntime().exec(cmd); 
            p.waitFor(); 
            BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
            String line=reader.readLine(); 
            while(line!=null) 
            { 
                //System.out.println(line);
                data += line+"\n";
                line=reader.readLine(); 
            } 
            return data;
        }
        catch(IOException e1) {
            misc.log("Error: kommand(); "+e1.toString());
            throw new InterruptedException("Failed to run command: "+e1);
        } 
    }
    
    public boolean clean(){
        boolean code=false;
        for(int i=0; i< dirStack.size();i++){
            try{
                delete(new File(dirStack.get(i)));
                
            }catch(Exception e){misc.log("Error: clean(); "+e.toString());}
        }
        return code;
    }
    
    private String readFile(String mPath){
        String line="", str="";
        try{
            BufferedReader in = new BufferedReader(new FileReader(mPath));
            while ((str = in.readLine()) != null) {
                line +=str;
                line +="\n";
            }
            in.close();
        }catch(Exception e){misc.log("Error: readFile(); "+e.toString());}
        return line;
    }
    
    private void saveFile(String mPath, String data){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(mPath));
            out.write(data);
            out.close();
        }catch(Exception e){
            misc.log("Couldn't save File: "+mPath);
        }
    }
    
    private int extractMSI(String mPath, String tmpDir){
        int errorCode = 31;
        String line ="", tmp = "";;
        try{
            kommand("7z x -y -o"+tmpDir+" "+mPath+"");
        }catch(Exception e){
            misc.log("Error: extractMSI(); "+e.toString());
        }
        if(new File(tmpDir+"/!_StringData").exists()){
            line = readFile(tmpDir+"/!_StringData");
            
            Vector<String> s = match(line); 
            for(String str : s){
                if(str.toLowerCase().contains("rtf".subSequence(0, 2))) //misc.log("Found..");
                    tmp += deRTF(str);
            }
        }
        if(tmp.length()>2){
            license = tmp;
            errorCode = 0;
        }
        
        return errorCode;
        
    }
    
    private boolean extractInno(String mPath, String tmpDir){
        return false;
        
    }
    
    private boolean extractNSIS(String mPath, String tmpDir){
        boolean errorCode = false;
        try{
            kommand("7z -p1 x -y -o"+tmpDir+" \""+path+"\" > /dev/null");
            errorCode = true;
        }catch(Exception e){
            misc.log("Error: extractNSIS(); "+e.toString());
        }
        return errorCode;
        
    }
    
            
            
    private String mktmp(String mPath){
        try{
            String uuid = UUID.randomUUID().toString();
            String tmpDir = "/tmp/"+uuid;
            
            File tmp = new File(tmpDir);
            if(tmp.mkdir()){
                dirStack.addElement(tmpDir);
                return tmpDir;
            }
        }catch(Exception e){
            misc.log("Error: mktmp(); "+e.toString());
            return "false";
        }
        return "false";
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

    private String deRTF(String text){
        String mText = "";
        try{
            saveFile(".tmp", text);
            mText = kommand("unrtf --text --nopict .tmp");
            mText = mText.substring(mText.indexOf("-----------------\n")+17);
            //misc.log(mText);
            delete(new File(".tmp"));
        }catch(Exception e){
            misc.log("Error: deRTF; "+e.toString());
            System.exit(1);
        }
        return mText;
    }
    
    private Vector<String> match(String text){
        Vector<String> data = new Vector<String>();
        int start = -1;
        int count = 0;
        for(int i=0; i<text.length(); i++){
            if(text.charAt(i) == '{'){
                if(start == -1){
                    start = i;
                }
                count++;
            }else if(text.charAt(i) == '}'){
                count--;
            }
            if((start != -1) && (count == 0)){
                data.add(text.substring(start, i));
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