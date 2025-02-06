/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jonathan.mileham
 */
@WebServlet(urlPatterns = {"/fileCreator"})
@MultipartConfig
public class fileCreator extends HttpServlet {

    //Values to check
    private String delimiter=",";
    private int lineNumOfColumn=1;
    private String columnName="Description";
    private String filePath="C:\\Users\\jonathan.mileham\\Downloads\\temp\\";
    private String fileExtention=".dat";
        //Value is gotten from: getPositionOfSearch
        private int positionOfColumn=-1;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            processFile(request, response);
        } catch (IOException ex) {
            System.out.println("Error with getting the response");
        }
    }

    private void processFile(HttpServletRequest request, HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        try {
            out.print("Delimiter used: "+delimiter+"\n");
            Part part = request.getPart("choosenFile");
            
            //Read all the lines of the file and convirt to arraylist
            BufferedReader buf= new BufferedReader(new InputStreamReader(part.getInputStream()));
            ArrayList<String> convertedBuffer= convertBufferedReaderToArrayList(buf);
            
            //Create a Map with all the keys being the different types of checks
            getPositionOfSearch(convertedBuffer.get(lineNumOfColumn-1));
            if(positionOfColumn!=-1){
                Map<String, ArrayList<String>> map=createMap(convertedBuffer);
                soutMap(map, response);
                
                if (createFiles(map)){
                    writeToFiles(map);
                }
                
            }else{out.print("positionOfColumn is "+positionOfColumn);}
            
        } 
        catch (IOException e) {out.print("Error occured while trying to read file \n");} 
        catch (ServletException ex) {out.print("Error occured while trying to get file \n");}
    }

    private ArrayList<String> convertBufferedReaderToArrayList(BufferedReader reader) throws IOException{
        String line;
        ArrayList<String> lines=new ArrayList<>();
            while ( (line=reader.readLine())!=null){
                lines.add(line);
            }
            return lines;
        }
    
    private void getPositionOfSearch(String columnNames){
        String[] split=columnNames.split(delimiter);
        
        for (int count = 0; count < split.length; count++) {
            Boolean found= split[count].equalsIgnoreCase(columnName);
            if(found){
                positionOfColumn=count;
            }
        }
    }
    
    private Map<String, ArrayList<String>> createMap(ArrayList<String> lines){
        Map<String, ArrayList<String>> map = new HashMap<>();
        String key;
          
        for (String line : lines) {
            String[] split=line.split(delimiter);
            if (split.length>=positionOfColumn+1){
                key=split[positionOfColumn];
                if(!key.equalsIgnoreCase(columnName)){
                    ArrayList<String>list= map.get(key);
                        if (list==null){
                            list= new ArrayList<>();
                        }
                    list.add(line);
                    map.put(key, list);
                }
            }
        }
            
        return map;
    }
    
    private void soutMap(Map<String,ArrayList<String>> map,HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        
        List<String> keys = new ArrayList<String>(map.keySet());
        
        for (String key : keys) {
            ArrayList<String> values=map.get(key);
            out.print(key+": "+ values.size() +"\n");
        }
        
    }
    
    private  boolean  createFiles(Map<String,ArrayList<String>> map){
        List<String> keys = new ArrayList<String>(map.keySet());
        
        for (String key : keys) {            
            File myFile= new File(filePath+key+fileExtention);
            
            if(myFile.exists()){
                myFile.delete();
            }
            try {
                myFile.createNewFile();
            } catch (IOException ex) {
                return false;
            }
        }
        return true;
    }
    
    private String writeToFiles(Map<String,ArrayList<String>> map){
    List<String> keys = new ArrayList<String>(map.keySet());
        
        for (String key : keys) {
            FileWriter myFile= null;
            try {
                myFile = new FileWriter(filePath+key+fileExtention);
                ArrayList<String> values=map.get(key);
                
                for (String value : values) {
                    myFile.write(value+"\n");
                }
            } catch (IOException ex) {return "Error when trying to read/write file: "+key;} 
            finally {
                try {
                    myFile.close();
                } catch (IOException ex) {
                    return "Error when trying to clsse file: "+key;
                }
            }
        }
        return "Successfully wrote to all files";
    }
}
