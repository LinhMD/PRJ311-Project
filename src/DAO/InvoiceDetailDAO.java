package DAO;

import DTO.InvoiceDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InvoiceDetailDAO {
	public static boolean saveInvoiceDetail(InvoiceDetail detail) throws SQLException {
		Connection connection = MyConnection.makeConnection();
		if(connection != null){
			String statement =  "insert into invoice_detail (id, id_pet, pro_id)\n" +
								"values (?, ?, ?);";
			PreparedStatement prepareStatement = connection.prepareStatement(statement);
			prepareStatement.setInt(1, detail.getInvoice().getId());
			prepareStatement.setInt(2, detail.getPet().getId());
			prepareStatement.setInt(3, detail.getService().getId());
			boolean result = prepareStatement.executeUpdate() == 0;
			connection.close();
			return result;
		}else return false;
	}
}
