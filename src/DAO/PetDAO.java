package DAO;

import DTO.Customer;
import DTO.Pet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PetDAO {
	public static Vector<Pet> getCustomerPet(Customer customer) throws SQLException {
		Vector<Pet> pets = new Vector<>();
		Connection connection = MyConnection.makeConnection();
		if(connection != null){
			String statement =  "select p.id_pet, p.name_pet, p.age_pet, p.id_type, p.owner_id\n" +
								"from dbo.pet p\n" +
								"where p.owner_id = ?";
			PreparedStatement prepareStatement = connection.prepareStatement(statement);
			prepareStatement.setInt(1, customer.getId());
			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				int id = resultSet.getInt("id_pet");
				String name = resultSet.getString("name_pet");
				int age_pet = resultSet.getInt("age_pet");
				int id_type = resultSet.getInt("id_type");
				pets.add(new Pet(id, name, age_pet, customer, id_type));
			}
			connection.close();
		}
		return pets;
	}
}
