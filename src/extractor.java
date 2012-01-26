import java.util.*;

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
        return false;
    }
    
    private String extractMSI(String path){
        return "";
        
    }
    
    private String extractInno(String path){
        return "";
        
    }
    
    private String extractNSIS(String path){
        return "";
        
    }
    
    private String extractInstallExplorer(String path){
        return "";
        
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