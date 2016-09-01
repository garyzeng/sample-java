package sample.tree.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBTreeTest {

	private static void getResult(Connection conn, int pid, int level) {
		if(conn != null) {
			String sql = "select * from t_table where pid = " + pid;
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<level; i++) {
				sb.append("---");
			}
			Statement stmt = null;
			ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					System.out.print(sb + rs.getString("name"));
					getResult(conn, rs.getInt("id"), level);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if(rs != null) {
						rs.close();
					}
					if(stmt != null) {
						stmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new RuntimeException("the instance of Connection is null");
		}
	}
	
	private static void delete(Connection conn, int id) {
		if(conn != null) {
			String sql = "select * from t_table where pid = " + id;
			Statement stmt = null;
			ResultSet rs = null;
			
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					delete(conn, rs.getInt("id"));
				}
				stmt.executeUpdate("delete from t_table where id = " + id);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if(rs != null) {
						rs.close();
					}
					if(stmt != null) {
						stmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 根节点的id为1，父id为0
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/drp", "root", "root");
			getResult(conn, 0, 0);
			delete(conn, 1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}









