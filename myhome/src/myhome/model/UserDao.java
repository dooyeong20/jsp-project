package myhome.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {
	// �̱��� ����
	private static UserDao instance;
	private String id = "root";
	private String pw = "root";
	private UserDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static UserDao getInstance() {
		if(null == instance) {
			instance = new UserDao();
		}
		return instance;
	}
	
	/////////////////////////////////////
	private Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/myhomedb", id, pw);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	private void close(Connection conn, PreparedStatement ps) {
		close(conn, ps, null);
	}
	
	private void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	////////////////////////////////////////////////
	public boolean insert(UserDto dto) {
		String sql = "INSERT INTO user(id, email, name, password, regdate) VALUES(?, ?, ?, ?, DEFAULT)";
		boolean result = false;
		Connection conn = getConnection();
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, dto.getId());
			ps.setString(2, dto.getEmail());
			ps.setString(3, dto.getName());
			ps.setString(4, dto.getPassword());
			
			result = ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		close(conn, ps);
		return result;
	}
	
	public boolean delete(UserDto dto) {
		String sql = "DELETE FROM user WHERE id = ? and password = ?";
		boolean result = false;
		Connection conn = getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, dto.getId());
			ps.setString(2, dto.getPassword());
			
			result = ps.executeUpdate() > 0;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		close(conn, ps);
		return result;
	}
	
	public UserDto getUserById(String id) {
		String sql = "SELECT id, password, email, name, regdate FROM user WHERE id = ?";
		UserDto dto = new UserDto();
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				dto.setId(rs.getString("id"));
				dto.setPassword(rs.getString("password"));
				dto.setEmail(rs.getString("email"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("regdate"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return dto;
	}
	
	public boolean updateUser(UserDto dto) {
		String sql = "UPDATE user SET name = ?, password = ?, email = ? WHERE id = ? AND password = ?";
		boolean result = false;
		Connection conn = getConnection();
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, dto.getName());
			ps.setString(2, dto.getPassword());
			ps.setString(3, dto.getEmail());
			ps.setString(4, dto.getId());
			ps.setString(5, dto.getPassword());
			
			result = ps.executeUpdate() > 0;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		close(conn, ps);
		return result;
	}
	
	public boolean verify(UserDto dto) {
		String sql = "SELECT name FROM user WHERE id = ? AND password = ?";
		boolean result = false;
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, dto.getId());
			ps.setString(2, dto.getPassword());
			
			rs = ps.executeQuery();
			
			result = rs.next();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		close(conn, ps, rs);
		return result;
	}
	
	public ArrayList<UserDto> getAllUsers(){
		ArrayList<UserDto> users = new ArrayList<UserDto>();
		String sql = "SELECT id, password, name, email, regdate FROM user";
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserDto user = null;
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				user = new UserDto();
				user.setId(rs.getString("id"));
				user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setRegdate(rs.getString("regdate"));
				users.add(user);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	public boolean deleteUserByAdmin(String id) {
		String sql = "DELETE FROM user WHERE id = ?";
		boolean result = false;
		Connection conn = getConnection();
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			result = ps.executeUpdate() > 0;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		close(conn, ps);
		return result;
	}
	
}