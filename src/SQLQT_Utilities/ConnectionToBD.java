package SQLQT_Utilities;
import java.sql.Connection;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class ConnectionToBD {
	public static Connection con = null;
	
	public ConnectionToBD(Connection con) 
	{
		ConnectionToBD.con = con;
	}
}
