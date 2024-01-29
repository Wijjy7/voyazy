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
public class Activite {
    int id ; 
    String activite ;
    double prix;

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
    
    

    public Activite() {
      
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }
    
    public Activite(int id , String nom){
        this.setId(id);
        this.setActivite(nom);
    }
    
     public Vector<Activite> getAll(Connection connect){
        Vector<Activite> activites = new Vector<Activite>();
        try{
            Statement statement = connect.createStatement();
            ResultSet result = statement.executeQuery("Select * from activite");
            while(result.next()){
                Activite activite = new Activite(result.getInt("id") , result.getString("activite"));
                activite.setPrix(result.getDouble("prix"));
                activites.add(activite);
            }
        }catch(Exception e ){
            
        }
        return activites ;      
    }
    
     public Activite getById (Connection connect , int id){
        Activite activite = new Activite();
        try{
            Statement statement = connect.createStatement();
            ResultSet resultat = statement.executeQuery("Select * from activite where id="+id);
            while(resultat.next()){
                activite.setId(resultat.getInt("id"));
                activite.setActivite(resultat.getString("activite"));
                activite.setPrix(resultat.getDouble("prix"));
            }
        }catch(Exception e){
            
        }
        return activite ; 
    }
    
     public Vector<Activite> activiteBouquet (Connection connect , int id ){
         Vector<Activite> activiteBouquet = new Vector<Activite>();
         try{
             Statement statement = connect.createStatement();
             ResultSet result = statement.executeQuery("Select * from v_actBouquet where idBouquet="+id);
             while(result.next()){
                Activite activite = new Activite(result.getInt("idactivite") , result.getString("activite"));
                activite.setPrix(result.getDouble("prix"));
                activiteBouquet.add(activite);
             }
        }catch(Exception e){
             
        }
        
        return activiteBouquet;
     }
     
    public static void ajouterStock(Connection connection , int idActivite , int qte)throws Exception{
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO Stock VALUES(default, "+idActivite+","+qte +", now() )");
        statement.close();
    }
     
    public int insertActivite (Connection connect , String activite , double prix)throws Exception{
         try{
            int i=0;
            PreparedStatement statement = connect.prepareStatement("Insert into activite values (default , '"+activite+"',"+prix+")",Statement.RETURN_GENERATED_KEYS);
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
    
     public void insertActiviteBouquet (Connection connect , int idBouquet , int idActivite){
        try{
            Statement statement = connect.createStatement();
            statement.execute("Insert into activitebouquet values ("+idBouquet+" , '"+idActivite+"')");
        }catch(Exception e){
            
        }
    }
}