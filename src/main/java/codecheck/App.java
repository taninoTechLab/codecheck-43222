package codecheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class App {
	public static void main(String[] args) {


		System.out.println("test");
		String result = getResult("http://challenge-server.code-check.io/");
		System.out.println(result);

		for (int i = 0, l = args.length; i < l; i++) {
			String output = String.format("argv[%s]: %s", i, args[i]);
			System.out.println(output);
		}
	}

	public static String getResult(String urlString){
		String result = "";
		try{
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(
		con.getInputStream()));
		String tmp = "";
		while ((tmp = in.readLine()) != null) {
		result += tmp;
		}
		in.close();
		con.disconnect();
		}catch(Exception e){
		e.printStackTrace();
		}
		return result;
		}
}
