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
import java.time.LocalDate;
import java.util.Vector;
import modele.Activite;
import modele.Reservationn;
import modele.ResteActivite;
import modele.Voyage;

/**
 *
 * @author hp
 */
public class Reservation extends HttpServlet {

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
            out.println("<title>Servlet Reservation</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Reservation at " + request.getContextPath() + "</h1>");
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
        try{
            connection = connexion.Connexion.connection();
            Vector<Voyage> voyages = new Voyage().select(connection, "voyage");
            request.setAttribute("voyages", voyages);
            RequestDispatcher rd = request.getRequestDispatcher("Reservation.jsp");
            rd.forward(request, response);
        }catch(Exception e){
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
        int idVoyage = Integer.parseInt(request.getParameter("voyage").trim());
        int qte = Integer.parseInt(request.getParameter("qte").trim());
        try{
            connection = connexion.Connexion.connection();
            Reservationn reservation = new Reservationn();
            reservation.setDate(Date.valueOf(LocalDate.now()));
            reservation.setIdVoyage(idVoyage);
            reservation.setQte(qte);
            reservation.setNomClient(nom);
            
            Vector<ResteActivite> activites = new Reservationn().check(connection, reservation);
            if(reservation.checkVector(activites)){
                reservation.insert(connection, "reservation");            
                response.sendRedirect("Reservation");
            }else{
                String message = "Tsy ampy :";
                for(int i=0 ; i<activites.size() ; i++){
                    if(activites.get(i).getQte()<0){
                        message += activites.get(i).getActivite()+" : "+activites.get(i).getQte()*-1;
                        message+=" , ";
                    }
                }
                request.setAttribute("message", message);
                RequestDispatcher dispatcher = request.getRequestDispatcher("Exception.jsp");
                dispatcher.forward(request, response);
            }
            
        }catch(Exception e){
            
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
