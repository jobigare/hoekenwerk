package be.klaswelle;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class HoekenServlet
 */
public class HoekenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HoekenServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		
		JSONObject output = new JSONObject();
		JSONArray studenten = new JSONArray();
		JSONArray hoeken = new JSONArray();
		
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();

		Connection conn = null;
		Statement stmt = null; // Or PreparedStatement if needed
		Statement stmtHoeken = null;
		ResultSet rs = null;
		ResultSet rsHoeken = null;
		try {
			InitialContext cxt = new InitialContext();
			DataSource ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/postgres");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			stmtHoeken = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM studenten");
			rsHoeken = stmtHoeken.executeQuery("SELECT hoeknaam FROM hoeken");
			ResultSetConverter resConv = new ResultSetConverter();
			studenten = resConv.convert(rs);
			hoeken = resConv.convert(rsHoeken);
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			conn.close(); // Return to connection pool
			conn = null; // Make sure we don't close it twice
		} catch (SQLException e) {

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Always make sure result sets and statements are closed,
			// and the connection is returned to the pool
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					;
				}
				rs = null;
			}
			if (rsHoeken != null) {
				try {
					rsHoeken.close();
				} catch (SQLException e) {
					;
				}
				rsHoeken = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					;
				}
				stmt = null;
			}
			if (stmtHoeken != null) {
				try {
					stmtHoeken.close();
				} catch (SQLException e) {
					;
				}
				stmtHoeken = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					;
				}
				conn = null;
			}
		}
		
		output.put("studenten", studenten);
		output.put("hoeken", hoeken);
		
		out.print(output.toString());
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
