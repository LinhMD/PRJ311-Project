package DAO;

import DTO.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ServiceDAO {
	public static Vector<Service> getServices() throws SQLException {
		Vector<Service> customers = new Vector<>();
		Connection connection = MyConnection.makeConnection();
		if(connection != null){
			String statement =  "select p.pro_id, p.pro_name, p.price\n" +
								"from dbo.procedure_ p";
			PreparedStatement prepareStatement = connection.prepareStatement(statement);
			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				int id = resultSet.getInt("pro_id");
				String proName = resultSet.getString("pro_name");
				int price = resultSet.getInt("price");
				customers.add(new Service(id, proName, price));
			}
			connection.close();
		}
		return customers;
	}
}
