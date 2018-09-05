package be.klas2;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

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
		
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();

		Connection conn = null;
		Statement stmt = null; // Or PreparedStatement if needed
		ResultSet rs = null;
		try {
			DataSource ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/postgres");
			conn = ds.getConnection();
			stmt = conn.createStatement("select * from studenten");
			rs = stmt.executeQuery();
			ResultSetConverter resConv = new ResultSetConverter;
			JSONArray studenten = resConv(rs);
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			conn.close(); // Return to connection pool
			conn = null; // Make sure we don't close it twice
		} catch (SQLException e) {

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
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					;
				}
				stmt = null;
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
		
		out.print(jsonObject);
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
