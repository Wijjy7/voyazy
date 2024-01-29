/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import connexion.Connexion;
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
import modele.Bouquet;
import modele.Duree;
import modele.Lieu;
import modele.Voyage;

/**
 *
 * @author Family
 */
public class Insert_voyage extends HttpServlet {

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
         Connection connexion = null;
        try {
            connexion = Connexion.connection();
            Vector<Lieu> lieux = new Lieu().select(connexion, "lieu");
            Vector<Bouquet> bouquets = new Bouquet().getAll(connexion);
            Vector<Duree> duree = new Duree().select(connexion, "duree");
            request.setAttribute("lieu" , lieux);
            request.setAttribute("bouquet" , bouquets);
            request.setAttribute("duree" , duree);
            response.getWriter().print(lieux.size());
            response.getWriter().print(bouquets.size());
            RequestDispatcher dispatch = request.getRequestDispatcher("insert_voyage.jsp");
            dispatch.forward(request, response);
        } catch (Exception ex) {
            response.getWriter().print(ex);
            Logger.getLogger(Insert_voyage.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                connexion.close();
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
        int duree = Integer.parseInt(request.getParameter("duree"));
         int lieu = Integer.parseInt(request.getParameter("lieu"));
         int bouquet = Integer.parseInt(request.getParameter("bouquet"));
         double prix = Double.parseDouble(request.getParameter("prix"));
         String nom = request.getParameter("nom");
        Voyage voyage = new Voyage(duree, lieu , bouquet, prix);
        voyage.setNom(nom);
        try{
            Connection connexion = Connexion.connection();
            int id = voyage.insert(connexion);
            response.getWriter().print(id);
            Bouquet bouquet1 = (new Bouquet()).getById(connexion, bouquet);
            request.setAttribute("bouquet", bouquet1);
            request.setAttribute("voyage", id);
            RequestDispatcher dispatcher = request.getRequestDispatcher("ajout_quantite_activite.jsp");
            dispatcher.forward(request, response);
        }catch(Exception e){
                response.getWriter().print(e);
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
