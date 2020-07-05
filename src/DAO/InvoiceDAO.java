package DAO;

import DTO.Invoice;
import DTO.InvoiceDetail;

import java.sql.*;
import java.util.Calendar;

public class InvoiceDAO {
	public static int getLatestID() throws Exception {
		Connection connection = MyConnection.makeConnection();
		if(connection != null){
			String statement =  "select max (i.[inv_id]) from [dbo].[invoice] i";
			PreparedStatement prepareStatement = connection.prepareStatement(statement);
			ResultSet resultSet = prepareStatement.executeQuery();
			if (resultSet.next())
				return resultSet.getInt(1);
		}else{
			throw new Exception("have some error with data base connection");
		}
		return 0;
	}
	public static boolean insertInvoice(Invoice invoice) throws SQLException {
		Connection connection = MyConnection.makeConnection();
		if(connection != null){
			String statement =  "insert into invoice (inv_id, inv_date, owner_id)\n" +
								"values (?, ?, ?);";
			PreparedStatement prepareStatement = connection.prepareStatement(statement);

			prepareStatement.setInt(1, invoice.getId());
			prepareStatement.setDate(2, new Date(invoice.getDate().getTime()));
			prepareStatement.setInt(3, invoice.getCustomer().getId());
			boolean execute = prepareStatement.execute();
			for (InvoiceDetail detail : invoice.getDetails())
				if(InvoiceDetailDAO.saveInvoiceDetail(detail)){
					System.out.println("fail add detail" + detail.toString());
				}
			connection.close();
			return execute;
		}else return false;
	}
}
