/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.util.Vector;

/**
 *
 * @author USER
 */
public class Customer implements Comparable<Customer>{
    int id;
    String name;
    String address;
    Vector<Pet> CustomerPets;

    public Customer(int id, String owner_name, String owner_address) {
        this.id = id;
        this.name = owner_name;
        this.address = owner_address;
    }

    public Customer(int id, String name, String address, Vector<Pet> customerPet) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.CustomerPets = customerPet;
    }

    public Customer(){
    }

    public Customer(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Vector<Pet> getCustomerPets() {
        return CustomerPets;
    }

    public void setCustomerPets(Vector<Pet> customerPets){
        this.CustomerPets = customerPets;
    }

    @Override
    public int compareTo(Customer o) {
        return this.id - o.id;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
