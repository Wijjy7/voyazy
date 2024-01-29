/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import controller.Reste;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Vector;

/**
 *
 * @author hp
 */
public class Reservationn extends bdd.BddObject{
    int id;
    int idVoyage;
    String nomClient;
    Date date;
    int qte;

    public static void main(String[] args)throws Exception{
        Connection connection = connexion.Connexion.connection();
        Reservationn reservation = new Reservationn();
        reservation.setDate(Date.valueOf(LocalDate.now()));
        reservation.setIdVoyage(23);
        reservation.setQte(1);
        reservation.setNomClient("Ok");
        Vector<ResteActivite> activites = new Reservationn().check(connection, reservation);
        for(int i=0 ; i<activites.size() ; i++){
            System.out.println(activites.get(i).getQte());
        }
    }
    public Vector<ResteActivite> check(Connection connection, Reservationn reservationn)throws Exception{
        Vector<ResteActivite> restes = new ResteActivite().select(connection, "resteStockTotal");
        Vector<QuantiteActivite> activites = new QuantiteActivite().getQteVoyage(connection, reservationn.getIdVoyage());
        for(int i=0 ; i<activites.size() ; i++){
            for(int j=0 ; j<restes.size() ; j++){
                if(activites.get(i).getIdactivite()==restes.get(j).getId()){
                    int qte = activites.get(i).getQuantite()*reservationn.getQte();
                    System.out.println("o"+qte);
                    int reste = restes.get(j).getQte()-qte;
                    restes.get(j).setQte(reste);
                }
            }
        }
        return restes;
    }
    
    public boolean checkVector(Vector<ResteActivite> activites){
        boolean check = true;
        for(int i=0 ; i<activites.size() ; i++){
            if(activites.get(i).getQte()<0){
                check = false;
            }
        }
        return check;
    }
    
    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }
            
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVoyage() {
        return idVoyage;
    }

    public void setIdVoyage(int idVoyage) {
        this.idVoyage = idVoyage;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    
}
