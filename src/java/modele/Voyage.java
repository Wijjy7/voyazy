/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import bdd.BddObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Family
 */
public class Voyage extends BddObject  {
    int id ; 
    String nom ; 
    int duree ; 
    int idlieu ; 
    int idbouquet ; 
    double prix ;
    double benefice;

    public Voyage(){}
    public int insert(Connection connection) throws SQLException{
        int id = 0;
        String sql = "insert into voyage values(default, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        statement.setString(5, this.getNom());
        statement.setInt(1, this.getDuree());
        statement.setInt(2, this.getIdlieu());
        statement.setInt(3, this.getIdbouquet());
        statement.setDouble(4, this.getPrix());
        int result = statement.executeUpdate();
        if(result>0){
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next()){
                id = generatedKeys.getInt(1);
                System.out.println(id);
            }
        }
        return id;
    }
    public static void insertQteActivite(Connection connect,int idVoyage,int idActivite,int qte , int duree)throws Exception{
        Statement statement = connect.createStatement();
        statement.execute("INSERT INTO quantiteActivite VALUES ("+idVoyage+","+idActivite+","+qte+" , "+duree+")");
        statement.close();
    }
    public Vector<Activite> getActivite(Connection connection)throws Exception{
        Vector<Activite> activites = new Vector<Activite>();
        Statement statement = connection.createStatement();
        ResultSet resultSet =statement.executeQuery("select idactivite from quantiteActivite where idvoyage="+this.getId());
        while(resultSet.next()){
            Activite activite = new Activite().getById(connection, resultSet.getInt(1));
            activites.add(activite);
        }
        return activites;
    }
    public int getQTE(Connection connect, int activite)throws Exception{
        int qte = 0 ;
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select quantite from quantiteActivite where idvoyage ="+this.getId()+" and idactivite="+activite);
        while(resultSet.next()){
            qte = resultSet.getInt(1);
        }
        statement.close();
        
        return qte;
    }
    public double prixVoyage(Connection connection)throws Exception{
        double prix = 0;
        Vector<Activite> activites = this.getActivite(connection);
        for(int i=0 ; i<activites.size() ; i++){
            int qte = this.getQTE(connection, activites.get(i).getId());
            prix += qte * activites.get(i).getPrix();
        }
        prix += this.salaireVoyage(connection);
        return prix;
    }  
    public double salaireVoyage(Connection connection)throws Exception{
        double salaire = 0 ;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT salaire from prixEmployerVoyageDuree where idvoyage="+this.getId());
        while(resultSet.next()){
            salaire = resultSet.getDouble(1);
        }
        return salaire;
    }
    public double getBenefice(Connection connection)throws Exception{
        return this.getPrix()-this.prixVoyage(connection);
    }

    public double getBenefice() {
        return benefice;
    }

    public void setBenefice(double benefice) {
        this.benefice = benefice;
    }
    
    
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getIdlieu() {
        return idlieu;
    }

    public void setIdlieu(int idlieu) {
        this.idlieu = idlieu;
    }

    public int getIdbouquet() {
        return idbouquet;
    }

    public void setIdbouquet(int idbouquet) {
        this.idbouquet = idbouquet;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Voyage(int duree, int idlieu, int idbouquet, double prix) {
        this.duree = duree;
        this.idlieu = idlieu;
        this.idbouquet = idbouquet;
        this.prix = prix;
    }
    
    
}
