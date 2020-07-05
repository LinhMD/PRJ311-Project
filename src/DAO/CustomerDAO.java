/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import static DAO.PetDAO.getCustomerPet;

/**
 *
 * @author USER
 */
public class CustomerDAO {
    public static Vector<Customer> getCustomers() throws SQLException {
	    Vector<Customer> customers = new Vector<>();
	    Connection connection = MyConnection.makeConnection();
	    if(connection != null){
		    String statement = "select o.owner_id, o.owner_name, o.owner_address \n" +
				    "from dbo.pet_owner o";
		    PreparedStatement prepareStatement = connection.prepareStatement(statement);
		    ResultSet resultSet = prepareStatement.executeQuery();
		    while(resultSet.next()){
			    int id = resultSet.getInt("owner_id");
			    String owner_name = resultSet.getString("owner_name");
			    String owner_address = resultSet.getString("owner_address");
			    Customer customer = new Customer(id, owner_name, owner_address);
			    try{
				    customer.setCustomerPets(getCustomerPet(customer));
			    }catch (SQLException e){
			    	e.printStackTrace();
			    }
			    customers.add(customer);
		    }
		    connection.close();
	    }
	    return customers;
    }
}
