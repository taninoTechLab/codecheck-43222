package codecheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class App {
	public static void main(String[] args) throws Exception {

		for (int i = 0, l = args.length; i < l; i++) {
//			String output = String.format("argv[%s]: %s", i, args[i]);
			StringBuilder url = new StringBuilder().append("http://challenge-server.code-check.io/api/hash?q=");

			String result = getResult(url.append(args[i]).toString());
//			System.out.println(result);
//			System.out.println(output);

			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			// ScriptEngine の eval に JSON を渡す時は、括弧で囲まないと例外が発生します。eval はセキュリティ的には好ましくないので、安全であることが不明なデータを扱うことは想定していません。
			// 外部ネットワークと連携するプログラムで使用しないでください。
			Object obj = engine.eval(String.format("(%s)", result));
			// Rhino は、jdk1.6,7までの JavaScript エンジン。jdk1.8は「jdk.nashorn.api.scripting.NashornScriptEngine」
			Map<String, Object> map = jsonToMap(obj,
					engine.getClass().getName().equals("com.sun.script.javascript.RhinoScriptEngine"));
			System.out.println(map.get("hash").toString());
		}
	}

	public static String getResult(String urlString) {
		String result = "";
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	static Map<String, Object> jsonToMap(Object obj, boolean rhino) throws Exception {
		// Nashorn の場合は isArray で obj が配列かどうか判断できますが、特に何もしなくても配列番号をキーにして値を取得し Map に格納できるので、ここでは無視しています。
		// Rhino だとインデックスを文字列として指定した場合に値が返ってこないようなので、仕方なく処理を切り分けました。
		// 実際は HashMap なんか使わずに自分で定義したクラス（配列はそのオブジェクトの List プロパティ）にマップすることになると思うので、動作サンプルとしてはこんなもんでよろしいかと。
		boolean array = rhino ? Class.forName("sun.org.mozilla.javascript.internal.NativeArray").isInstance(obj)
				: false;
		Class<?> scriptObjectClass = Class.forName(rhino ? "sun.org.mozilla.javascript.internal.Scriptable"
				: "jdk.nashorn.api.scripting.ScriptObjectMirror");
		// キーセットを取得
		Object[] keys = rhino ? (Object[]) obj.getClass().getMethod("getIds").invoke(obj)
				: ((java.util.Set) obj.getClass().getMethod("keySet").invoke(obj)).toArray();
		// get メソッドを取得
		Method method_get = array ? obj.getClass().getMethod("get", int.class, scriptObjectClass)
				: (rhino ? obj.getClass().getMethod("get", Class.forName("java.lang.String"), scriptObjectClass)
						: obj.getClass().getMethod("get", Class.forName("java.lang.Object")));
		Map<String, Object> map = new HashMap<String, Object>();
		for (Object key : keys) {
			Object val = array ? method_get.invoke(obj, (Integer) key, null)
					: (rhino ? method_get.invoke(obj, key.toString(), null) : method_get.invoke(obj, key));
			if (scriptObjectClass.isInstance(val)) {
				map.put(key.toString(), jsonToMap(val, rhino));
			} else {
				map.put(key.toString(), val.toString()); // サンプルなので、ここでは単純に toString() してますが、実際は val の型を有効に活用した方が良いでしょう。
			}
		}
		return map;
	}
}
