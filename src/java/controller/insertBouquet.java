package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Vector;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Activite;
import modele.Bouquet;

/**
 *
 * @author Family
 */
public class insertBouquet extends HttpServlet {

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
        Connection connection = null;
        try {
            connection = connexion.Connexion.connection();
            Vector<Activite> activites = new Activite().getAll(connection);
            request.setAttribute("activite", activites);
            response.getWriter().print(activites.size());
            RequestDispatcher dispatcher = request.getRequestDispatcher("ajout_bouquet.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
        }finally{
            try {
                connection.close();
            } catch (Exception e) {
            }
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
         String nomBouquet = request.getParameter("bouquet");
        String[] idActivite = request.getParameterValues("activite");
        Connection connection = null;
        try {
            connection = connexion.Connexion.connection();
            int idBouquet = (new Bouquet()).insertBouquet(connection, nomBouquet);
            new Bouquet().insertActivivteBouquet(connection, idBouquet, idActivite);
        } catch (Exception e) {
            response.getWriter().print(e);
        }finally{
            try {
                connection.close();
            } catch (Exception e) {
            }
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("liste");
        dispatcher.forward(request, response);
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
