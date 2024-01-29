/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Family
 */
public class Bouquet {
    int id ;
    String nom ; 
    Vector<Activite> Activite ;

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

    public Vector<Activite> getActivite() {
        return Activite;
    }

    public void setActivite(Vector<Activite> Activite) {
        this.Activite = Activite;
    }
    
    public Bouquet(){
    }
    
    public Bouquet(int id , String nom){
        this.setId(id);
        this.setNom(nom);
    }
    
    public Vector<Bouquet> getAll(Connection connect){
        Vector<Bouquet> bouquets = new Vector<Bouquet>();
        try{
            Statement statement = connect.createStatement();
            ResultSet result = statement.executeQuery("Select * from bouquet");
            while(result.next()){
                Bouquet bouquet = new Bouquet(result.getInt("id") , result.getString("bouquet"));
                bouquets.add(bouquet);
            }
        }catch(Exception e ){
            
        }
        return bouquets ;      
    }
    
    public Bouquet getById (Connection connect , int id){
        Bouquet bouquet = new Bouquet();
        try{
            Statement statement = connect.createStatement();
            ResultSet resultat = statement.executeQuery("Select * from bouquet where id="+id);
            while(resultat.next()){
                bouquet.setId(resultat.getInt("id"));
                bouquet.setNom(resultat.getString("bouquet"));
                bouquet.setActivite(new Activite().activiteBouquet(connect, resultat.getInt("id")));
            }
        }catch(Exception e){
            
        }
        return bouquet ; 
    }
    
     public int insertBouquet (Connection connect , String Bouquet)throws Exception{
        try{
            int i=0;
            PreparedStatement statement = connect.prepareStatement("Insert into Bouquet values (default , '"+Bouquet+"')",Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while(resultSet.next()){
                i = resultSet.getInt(1);
            }
            return i;
        }catch(Exception e){
            throw e;
        }
    }
     
    public void insertActivivteBouquet(Connection connection , int idBouquet , String[] idactivite)throws Exception{
        for(int i=0 ; i<idactivite.length ;i++){
            PreparedStatement statement = connection.prepareStatement("INSERT INTO activitebouquet values(?,?)");
            statement.setInt(1, idBouquet);
            statement.setInt(2, Integer.parseInt(idactivite[i].trim()));
            statement.execute();
        }
    }
}
