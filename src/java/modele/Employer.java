/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author hp
 */
public class Employer extends bdd.BddObject{
    int id;
    String nom;
    double salaire;
    int idposte;
    Date embauche;
    String poste;
    
    public void changePoste(Connection connection)throws Exception{
        boolean change = false;
        LocalDate embauche = this.getEmbauche().toLocalDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(embauche, now);
        int diff = period.getYears();
        if(diff<=2 && diff>=0){
            change = true;
            this.setIdposte(1);
        }
        if(diff<=5 && diff>=2){
            change = true;
            this.setIdposte(2);
        }
        if(diff>=5){
            change = true;
            this.setIdposte(3);
        }
        if(change){
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE EMPLOYER set idposte="+this.getIdposte()+" where id="+this.getId());
            statement.close();
        }
    }
    public Date getEmbauche() {
        return embauche;
    }

    public void setEmbauche(Date embauche) {
        this.embauche = embauche;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
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

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public int getIdposte() {
        return idposte;
    }

    public void setIdposte(int idposte) {
        this.idposte = idposte;
    }
    
    
}
