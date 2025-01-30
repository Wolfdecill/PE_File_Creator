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
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonathan.mileham
 */
@WebServlet(urlPatterns = {"/fileCreator"})
@MultipartConfig
public class fileCreator extends HttpServlet {

    private String demimiter=",";
    private String[] fileName= {"GP","SP"};
    private String[] searchFor={"GP","Speciality"};
    
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
            Part part = request.getPart("choosenFile");
            
        } 
        catch (IOException e) {out.print("Error occured while trying to read file \n");} 
        catch (ServletException ex) {out.print("Error occured while trying to get file \n");}
    }
    
}
