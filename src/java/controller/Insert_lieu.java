/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import connexion.Connexion;
import static java.awt.PageAttributes.MediaType.C;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Lieu;
import modele.TypeLieu;

/**
 *
 * @author Family
 */
public class Insert_lieu extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
            
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        TypeLieu type = new TypeLieu();
        try {
            Connection connection = Connexion.connection();
            Vector<TypeLieu> typelieu = type.select(connection , "typelieu");
            request.setAttribute("typeLieu" , typelieu);
            RequestDispatcher dispatch = request.getRequestDispatcher("insert_lieu.jsp");
            dispatch.forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Insert_lieu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            String name =request.getParameter("lieu");
            int type = Integer.parseInt(request.getParameter("type"));          
            Lieu lieu = new Lieu(); 
            try{
            Connection connexion = Connexion.connection();
            lieu.setLieu(name);
            lieu.setIdtypelieu(type);
            lieu.insert(connexion, "lieu");
            }catch(Exception e){
                
            }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
