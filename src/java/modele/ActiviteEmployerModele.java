/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author hp
 */
public class ActiviteEmployerModele extends bdd.BddObject{
    int id;
    int idactivite;
    int idemployer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdactivite() {
        return idactivite;
    }

    public void setIdactivite(int idactivite) {
        this.idactivite = idactivite;
    }

    public int getIdemployer() {
        return idemployer;
    }

    public void setIdemployer(int idemployer) {
        this.idemployer = idemployer;
    }
    
    
}
