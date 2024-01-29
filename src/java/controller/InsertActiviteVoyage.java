/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import connexion.Connexion;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Vector;
import modele.Activite;
import modele.Bouquet;
import modele.Voyage;

/**
 *
 * @author hp
 */
public class InsertActiviteVoyage extends HttpServlet {

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
        int idB = Integer.parseInt(request.getParameter("bouquet"));
        int idVoyage = Integer.parseInt(request.getParameter("voyage"));
        Connection connect = null;
        try {
            connect = Connexion.connection();
            Bouquet bouquet = (new Bouquet()).getById(connect, idB);
            Vector<Activite> activites = bouquet.getActivite();
            for(int i=0 ; i<activites.size() ; i++){
                int qte = Integer.parseInt(request.getParameter(activites.get(i).getActivite()));
                int idActivite = Integer.parseInt(request.getParameter("id_"+activites.get(i).getActivite()));
                int duree = Integer.parseInt(request.getParameter("duree_"+activites.get(i).getActivite()));;
                Voyage.insertQteActivite(connect,idVoyage,idActivite,qte , duree);
            }
        } catch (Exception e) {
            response.getWriter().print(e);
        }finally{
            try {
                connect.close();
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
