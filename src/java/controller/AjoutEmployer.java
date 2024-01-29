/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Date;
import java.util.Vector;
import modele.Employer;
import modele.Poste;

/**
 *
 * @author hp
 */
public class AjoutEmployer extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AjoutEmployer</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AjoutEmployer at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        Connection connection = null;
        try {
            connection = connexion.Connexion.connection();
            Vector<Poste> postes = new Poste().select(connection, "poste");
            request.setAttribute("poste", postes);
            RequestDispatcher dispatcher = request.getRequestDispatcher("ajout_employer.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            response.getWriter().print(e);
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
    
        Connection connection = null;
        String nom = request.getParameter("nom");
        int idposte = Integer.parseInt(request.getParameter("poste").trim());
        double salaire = Double.parseDouble(request.getParameter("salaire").trim());
        String emb = request.getParameter("embauche");
        try {
            connection = connexion.Connexion.connection();
            Employer employer = new Employer();
            employer.setNom(nom);
            employer.setSalaire(salaire);
            employer.setIdposte(idposte);
            employer.setEmbauche(Date.valueOf(emb));
            employer.insert(connection, "employer");
            
            response.sendRedirect("AjoutEmployer");
        } catch (Exception e) {
            
        }finally{
            try {
                connection.close();
            } catch (Exception e) {
            }
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
