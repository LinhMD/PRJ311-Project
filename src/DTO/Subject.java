/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class Subject implements Comparable<Subject>{
  
    private String Name;
    private List<Student> listOfStudent;

    public List<Student> getListOfStudent() {
        return listOfStudent;
    }

    public void setListOfStudent(List<Student> listOfStudent) {
        this.listOfStudent = listOfStudent;
    }

    public Subject() {
    }

    public Subject( String Name) {
        this.Name = Name;
        this.listOfStudent = new ArrayList<>();
    }


    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public int compareTo(Subject o) {
        return this.getName().compareTo(o.getName());
    }
}
