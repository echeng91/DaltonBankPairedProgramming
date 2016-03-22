import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class DaltonBank {

	/*//SQL code to make tables.
	create table customers(
	id integer primary key,
	name varchar(50)
	);

	create table accounts(
	accountnumber integer primary key,
	ownerID integer,
	balance decimal(5,2)
	);

	create table transactions(
	accountid integer,
	amount decimal(5,2),
	trans_type varchar(50)
	);

	alter table accounts add foreign key (ownerID) references customers (custid);
	alter table transactions add foreign key (accountnumber) references accounts (accountnumber);
	alter table transactions add trans_date date;

	 */

	private static Connection con = null;

	//Constructor
	public DaltonBank(){

	}

	//add
	public void addCustomer(String name, int ID){
		try{
			PreparedStatement pstmt=con.prepareStatement("insert into customers (name,custID) values (?,?)");
			pstmt.setString(1, name);
			pstmt.setInt(2, ID);
			pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void addAccount(int custID, int accID, double balance)
	{
		try{
			PreparedStatement pstmt=con.prepareStatement("insert into accounts (accountnumber,ownerID, balance) values (?,?,?)");
			pstmt.setInt(1, accID );
			pstmt.setInt(2, custID);
			pstmt.setDouble(3, balance);
			pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void addTransaction(int accID, double amount, String transType, String transDate)
	{
		switch(transType)
		{
		case "c":
		case "check": amount = -amount;
		break;
		case "d":
		case "debit": amount = -amount;
		break;
		case "de":
		case "deposit": 
		break;
		case "w":
		case "withdrawal": amount = -amount;
		break;
		case "transfer to": 
		break;
		case "transfer from": amount = -amount;
		break;
		case "overdraft fee": amount = -amount;
		break;
		case "instant transfer fee": amount = -amount;
		break;
		default: break;
		}
		try{
			PreparedStatement pstmt=con.prepareStatement("insert into transactions (accountnumber, amount, trans_type, trans_date) values (?,?,?,to_date(?, 'MM/DD/YY'))");
			pstmt.setInt(1, accID );
			pstmt.setDouble(2, amount);
			pstmt.setString(3, transType);
			pstmt.setString(4, transDate);
			pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void addTransaction(int accID, double amount, String transType, Date transDate)
	{
		switch(transType)
		{
		case "c":
		case "check": amount = -amount;
		break;
		case "d":
		case "debit": amount = -amount;
		break;
		case "de":
		case "deposit": 
		break;
		case "w":
		case "withdrawal": amount = -amount;
		break;
		case "transfer to": 
		break;
		case "transfer from": amount = -amount;
		break;
		case "overdraft fee": amount = -amount;
		break;
		case "instant transfer fee": amount = -amount;
		break;
		default: break;
		}
		try{
			PreparedStatement pstmt=con.prepareStatement("insert into transactions (accountnumber, amount, trans_type, trans_date) values (?,?,?,?)");
			pstmt.setInt(1, accID );
			pstmt.setDouble(2, amount);
			pstmt.setString(3, transType);
			pstmt.setDate(4, transDate);
			pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
/*
	//update
	public void updateAccount(int accID, double newBalance)
	{
		//overdraft
		if(newBalance < 0.0)
		{
			ResultSet rs = null;
			int ownerID = 0;
			newBalance -= 35.0;
			double otherBalance = 0;
			int otherAccID = 0;
			try
			{
				con.setAutoCommit(false);
				PreparedStatement findOwner = con.prepareStatement("select ownerid from accounts where accountnumber = ?");
				findOwner.setInt(1, accID);
				rs = findOwner.executeQuery();
				while(rs.next())
				{
					ownerID = rs.getInt(1);
				}
				PreparedStatement payFrom = con.prepareStatement("select accountnumber, balance from accounts where ownerid = ? and balance > 0.0");
				payFrom.setInt(1, ownerID);
				rs = payFrom.executeQuery();
				while(rs.next())
				{
					otherAccID = rs.getInt(1);
					otherBalance = rs.getDouble(2);
				}
				if(otherBalance > 0)
				{
					otherBalance += newBalance; 
					otherBalance -= 15.00;//$15 instant transfer fee
					newBalance = 0.0;
					PreparedStatement otherAccount = con.prepareStatement("update accounts set balance = ? where accountnumber = ?");
					otherAccount.setDouble(1, otherBalance);
					otherAccount.setInt(2, otherAccID);
					otherAccount.executeUpdate();
				}
				PreparedStatement thisAccount = con.prepareStatement("update accounts set balance = ? where accountnumber = ?");
				thisAccount.setDouble(1, newBalance);
				thisAccount.setInt(2, accID);
				thisAccount.executeUpdate();
				con.commit();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try{
					con.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//end overdraft

		try{
			PreparedStatement pstmt = con.prepareStatement("update accounts set balance = ? where accountnumber = ?");
			pstmt.setDouble(1, newBalance);
			pstmt.setInt(2, accID);
			pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public double doTransactions(int accID)
	{
		double balance = 0;
		double amount = 0;
		String transType = "";
		ResultSet rs = null;
		try{
			PreparedStatement init = con.prepareStatement("select balance from accounts where accountnumber = ?");
			init.setInt(1, accID);
			rs = init.executeQuery();
			while(rs.next())
			{
				balance = rs.getDouble(1);
			}
			PreparedStatement trans = con.prepareStatement("select amount, trans_type from transactions where accountnumber = ? order by trans_date");
			trans.setInt(1, accID);
			rs = trans.executeQuery();
			while(rs.next())
			{
				amount = rs.getDouble(1);
				transType = rs.getString(2);
				switch(transType)
				{
				case "c":
				case "check": balance -= amount;
				break;
				case "d":
				case "debit": balance -= amount;
				break;
				case "de":
				case "deposit": balance += amount;
				break;
				case "w":
				case "withdrawal": balance -= amount;
				break;
				case "transfer to": balance += amount;
				break;
				case "transfer from": balance -= amount;
				break;
				}
				updateAccount(accID, balance);
			}

		}catch (SQLException e) {
			e.printStackTrace();
		}
		return balance;
	}
*/
	
	public double currentBalance(int accID)
	{
		double balance = 0.0;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int ownerID = 0;
		int otherAccID =0;
		double otherBalance = 0.0;
		Date transDate;
		
		try
		{
			PreparedStatement init = con.prepareStatement("select balance, ownerid from accounts where accountnumber = ?");
			init.setInt(1, accID);
			rs = init.executeQuery();
			while(rs.next())
			{
				balance = rs.getDouble(1);
				ownerID = rs.getInt(2);
			}
			PreparedStatement trans = con.prepareStatement("select amount, trans_date from transactions where accountnumber = ? order by trans_date");
			trans.setInt(1, accID);
			rs = trans.executeQuery();
			while(rs.next())
			{
				balance += rs.getDouble(1);
				transDate = rs.getDate(2);
				if(balance < 0)
				{
					addTransaction(accID, 35, "overdraft fee", transDate);
					balance -=35;
					PreparedStatement payFrom = con.prepareStatement("select accountnumber, balance from accounts where ownerid = ? and balance > 0.0");
					payFrom.setInt(1, ownerID);
					rs1 = payFrom.executeQuery();
					while(rs1.next())
					{
						otherAccID = rs1.getInt(1);
						otherBalance = rs1.getDouble(2);
					}
					if(otherBalance > 0)
					{
						addTransaction(otherAccID, 15, "instant transfer fee", transDate);
						otherBalance -= 15;
						addTransaction(otherAccID, balance, "pay debt", transDate);
						otherBalance += balance;
						addTransaction(accID, -balance, "debt paid", transDate);
						balance = 0;
					}
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return balance;
	}
	
	//deleting

	//select

	//connection
	public void connect()
	{
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:ora1/ora1@localhost:1521:orcl");
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void disconnect()
	{
		try {
			con.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
