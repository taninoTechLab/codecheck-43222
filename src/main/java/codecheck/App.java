package codecheck;

public class App {
	public static void main(String[] args) {


		System.out.println("test");
//		runSample();
//		for (int i = 0, l = args.length; i < l; i++) {
//			String output = String.format("argv[%s]: %s", i, args[i]);
//			System.out.println(output);
//		}
	}

    void runSample() {
        Charset charset = StandardCharsets.UTF_8;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://httpbin.org/get");

        System.out.println
            ("requestの実行　「" + request.getRequestLine() + "」");
            //requestの実行　「GET http://httpbin.org/get HTTP/1.1」

        CloseableHttpResponse response = null;

        try {
            response = httpclient.execute(request);

            int status = response.getStatusLine().getStatusCode();
            System.out.println("HTTPステータス:" + status);
            //HTTPステータス:200

            if (status == HttpStatus.SC_OK){
                String responseData =
                    EntityUtils.toString(response.getEntity(),charset);
                System.out.println(responseData);
                //取得したデータが表示される
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}