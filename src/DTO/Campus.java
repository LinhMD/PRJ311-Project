/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author user
 */
public class Campus implements Comparable<Campus>{
   
   private String Name;

    public Campus() {
    }

    public Campus( String Name) {
       
        this.Name = Name;
    }


    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    @Override
    public String toString() {
        return this.Name;
    }

    @Override
    public int compareTo(Campus o) {
        return this.getName().compareTo(o.getName());
    }
}
