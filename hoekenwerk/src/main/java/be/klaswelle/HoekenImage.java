package be.klaswelle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class HoekenImage
 */
public class HoekenImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HoekenImage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject input = null;
		JSONArray studenten = new JSONArray();
		JSONArray hoeken = new JSONArray();
		
		String hoeknaam = request.getParameter("hoeknaam");
		
		response.setContentType("image/jpeg");
		
		OutputStream out = response.getOutputStream();

		Connection conn = null;
		Statement stmtHoeken = null;
		ResultSet rsHoeken = null;
		BufferedImage img = null;
		BufferedImage outputImage = null;
		try {
			InitialContext cxt = new InitialContext();
			DataSource ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/postgres");
			conn = ds.getConnection();
			stmtHoeken = conn.createStatement();
			rsHoeken = stmtHoeken.executeQuery("SELECT afbeelding FROM hoeken WHERE hoeknaam = '" + hoeknaam + "'");
			rsHoeken.next();
			byte [] afbeelding = rsHoeken.getBytes("afbeelding");
			
			try {
				img = ImageIO.read(new ByteArrayInputStream(afbeelding));
				outputImage = new BufferedImage((int) (img.getWidth() * 0.6), (int) (img.getHeight() * 0.6), img.getType());
				
				Graphics2D g2d = outputImage.createGraphics();
				g2d.drawImage(img, 0, 0, (int) (img.getWidth() * 0.6), (int) (img.getHeight() * 0.6), null);
		        g2d.dispose();
				
				} catch (IOException e) {
				    e.printStackTrace();
				}
			rsHoeken.close();
			rsHoeken = null;
			stmtHoeken.close();
			stmtHoeken = null;
			conn.close(); // Return to connection pool
			conn = null; // Make sure we don't close it twice
		} catch (SQLException e) {

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Always make sure result sets and statements are closed,
			// and the connection is returned to the pool
			if (rsHoeken != null) {
				try {
					rsHoeken.close();
				} catch (SQLException e) {
					;
				}
				rsHoeken = null;
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
		
		ImageIO.write(outputImage, "jpeg", out);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
