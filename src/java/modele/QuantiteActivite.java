/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author hp
 */
public class QuantiteActivite extends bdd.BddObject{
    int idvoyage;
    int idactivite;
    int quantite;
    double duree;
    
    public Vector<QuantiteActivite> getQteVoyage(Connection connection , int idvoyage)throws Exception{
        Vector<QuantiteActivite> activites = new Vector<QuantiteActivite>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM quantiteactivite where idvoyage="+idvoyage);
        while(resultSet.next()){
            QuantiteActivite qa = new QuantiteActivite();
            qa.setIdvoyage(resultSet.getInt(1));
            qa.setIdactivite(resultSet.getInt(2));
            qa.setQuantite(resultSet.getInt(3));
            qa.setDuree(resultSet.getDouble(4));
            activites.add(qa);
        }
        
        return activites;
    }
    
    public int getIdvoyage() {
        return idvoyage;
    }

    public void setIdvoyage(int idvoyage) {
        this.idvoyage = idvoyage;
    }

    public int getIdactivite() {
        return idactivite;
    }

    public void setIdactivite(int idactivite) {
        this.idactivite = idactivite;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getDuree() {
        return duree;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }
    
    
}
