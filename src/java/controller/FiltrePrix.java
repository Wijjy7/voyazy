/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import connexion.Connexion;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Vector;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Voyage;

/**
 *
 * @author hp
 */
public class FiltrePrix extends HttpServlet {

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
        Connection connection = null;
        double min = Double.parseDouble(request.getParameter("min"));
        double max = Double.parseDouble(request.getParameter("max"));
        try {
            connection = Connexion.connection();
            Vector<Voyage> liste = new Vector<Voyage>();
            Vector<Voyage> voyages = (new Voyage()).select(connection, "voyage");
            for(int i=0 ; i<voyages.size() ; i++){
                double prix = voyages.get(i).prixVoyage(connection);
                if(prix<=max && prix>=min){
                    Voyage temp = voyages.get(i);
                    temp.setPrix(prix);
                    liste.add(temp);
                }
            }
            request.setAttribute("voyage", liste);
            RequestDispatcher dispatcher = request.getRequestDispatcher("liste_filtre.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            
        }finally{
            try {
                connection.close();
            } catch (Exception e) {
            }
        }
                
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
        processRequest(request, response);
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
        processRequest(request, response);
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
