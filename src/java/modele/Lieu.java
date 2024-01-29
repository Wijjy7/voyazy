/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import bdd.BddObject;

/**
 *
 * @author Family
 */
public class Lieu extends BddObject  {
    int id ; 
    String lieu ; 
    int idtypelieu ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getIdtypelieu() {
        return idtypelieu;
    }

    public void setIdtypelieu(int idtypelieu) {
        this.idtypelieu = idtypelieu;
    }
  
}
