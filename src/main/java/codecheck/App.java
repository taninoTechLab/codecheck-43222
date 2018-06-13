package codecheck;

import java.io.IOException;
import javax.servlet.http.HttpServlet;

public class App {
	public static void main(String[] args) {


		println("test");

		for (int i = 0, l = args.length; i < l; i++) {
			String output = String.format("argv[%s]: %s", i, args[i]);
			System.out.println(output);
		}


	}


}