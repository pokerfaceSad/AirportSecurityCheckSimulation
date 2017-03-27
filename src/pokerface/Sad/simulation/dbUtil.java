package pokerface.Sad.simulation;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;


public class dbUtil {
	
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConn() throws FileNotFoundException, IOException, SQLException  {
		Properties pro = new Properties();
		Connection conn = null;
		pro = getProperties();
		conn = DriverManager.getConnection(pro.getProperty("url"),pro.getProperty("user"),pro.getProperty("password"));
		return conn;
	}
	public static Properties getProperties() throws FileNotFoundException, IOException{
		
		Properties pro = new Properties();
		pro.load(new FileInputStream("db.properties"));
		return pro;
			
	}
	public static void beginTransaction(Connection conn){
		if(conn!=null){
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
	public static void commitTransaction(Connection conn){
		if(conn!=null){
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
	public static void rollback(Connection conn){
		if(conn!=null){
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
	public static void close(PreparedStatement ps, Connection conn,ResultSet rs) {

		if(rs!=null)
		{
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(ps!=null)
		{
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn!=null)
		{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getDate() {
		Properties pro = new Properties();
		String date = null;
		try {
			pro = getProperties();
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(pro.getProperty("dateFormat"));
			date = sdf.format(d);
			
			return date;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	public static boolean initPassenger(int no,String isVIP){
		Properties pro = null;
		Connection conn = null;
		PreparedStatement ps = null;
		boolean insertSuccess = false;
		try {
			conn = dbUtil.getConn();
			pro = dbUtil.getProperties();
			dbUtil.beginTransaction(conn);
			ps = conn.prepareStatement(pro.getProperty("insert_sql"));
			ps.setInt(1, no);
			ps.setString(2, isVIP);
			if(ps.executeUpdate()==1)
			{
				insertSuccess = true;
				dbUtil.commitTransaction(conn);
			}else {
				dbUtil.rollback(conn);
			}
		} catch (Exception e) {
			dbUtil.rollback(conn);
			e.printStackTrace();
		}finally{
			dbUtil.close(ps, conn, null);
		}
		return insertSuccess;
	}
	public static boolean writeMsg(String sql){
		Properties pro = null;
		Connection conn = null;
		PreparedStatement ps = null;
		boolean insertSuccess = false;
		try {
			conn = dbUtil.getConn();
			pro = dbUtil.getProperties();
			dbUtil.beginTransaction(conn);
			ps = conn.prepareStatement(sql);
			if(ps.executeUpdate()==1)
			{
				insertSuccess = true;
				dbUtil.commitTransaction(conn);
			}else {
				dbUtil.rollback(conn);
			}
		} catch (Exception e) {
			dbUtil.rollback(conn);
			e.printStackTrace();
		}finally{
			dbUtil.close(ps, conn, null);
		}
		return insertSuccess;
	}
	private static boolean writeRelationIntoDB(String fanUid,String followederUid ) {
		Properties pro = null;
		Connection conn = null;
		PreparedStatement ps = null;
		boolean insertSuccess = false;
		try {
			conn = dbUtil.getConn();
			pro = dbUtil.getProperties();
			dbUtil.beginTransaction(conn);
			ps = conn.prepareStatement(pro.getProperty("intsert_relation"));
			ps.setString(1, fanUid);
			ps.setString(2, followederUid);
			if(ps.executeUpdate()==1)
			{
				insertSuccess = true;
				dbUtil.commitTransaction(conn);
			}else {
				dbUtil.rollback(conn);
			}
		} catch (Exception e) {
			dbUtil.rollback(conn);
			e.printStackTrace();
		}finally{
			dbUtil.close(ps, conn, null);
		}
		return insertSuccess;
	}

}
