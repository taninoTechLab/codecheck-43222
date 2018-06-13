package codecheck;

import java.io.IOException;
import javax.servlet.http.HttpServlet;

public class App {
	public static void main(String[] args) {
		for (int i = 0, l = args.length; i < l; i++) {
			String output = String.format("argv[%s]: %s", i, args[i]);
			System.out.println(output);
		}

		println("test");
		println("test2");
	}

	@WebServlet("http://challenge-server.code-check.io/api/hash")
	@SuppressWarnings("serial")
	public class HashApi extends HttpServlet {
		public void doGet(HttpServletRequest req, HttpServletResponse res)
		    throws ServletException, IOException
		  {
		    List<String> hash = new List<String>;
		    Res.json(
		      res, Collections.singletonMap("q", hash)
		    );
		  }
	}

	public class Res {
		  static Gson gson = new Gson();
		  static void json(HttpServletResponse res, Object toJson)
		    throws IOException
		  {
		    res.setContentType("application/json");
		    res.setCharacterEncoding("utf-8");
		    res.getWriter().println(
		      gson.toJson(toJson)
		    );
		  }
	}
