package com.mkf_test.showtexts.utils;

import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpUtil {
	public static final String POST = "POST";
	public static final String GET = "GET";


	/**
	 * 发送网络请求
	 * @return
	 */
	public static void sendHttpForBack(final String url,final String msg, final String method,String charset,final Map<String, String> headmap,final httpback httpback) {
		RequestParams params = new RequestParams(url);
//		params.setHeader("Content-Type","text/html;charset=gbk");
//		params.setCharset("gbk");
		params.setCharset(charset);
		Callback.Cancelable cancelable
				= x.http().get(params,
				new Callback.CommonCallback<String>() {
					@Override
					public void onSuccess(String result) {
//						Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
						httpback.back(result, 200);
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
						if (ex instanceof HttpException) { // 网络错误
							HttpException httpEx = (HttpException) ex;
							int responseCode = httpEx.getCode();
							String responseMsg = httpEx.getMessage();
							String errorResult = httpEx.getResult();
							// ...
						} else { // 其他错误
							// ...
						}
					}

					@Override
					public void onCancelled(CancelledException cex) {
						Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onFinished() {

					}
				});
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// 网络请求
//				try {
//					URL Url = new URL(url);
//					HttpURLConnection connection = (HttpURLConnection) Url
//							.openConnection();
//					connection.setRequestMethod(method);
//					if (method.equals(POST)) {
//						connection.setDoOutput(true);
//						if (headmap!=null) {
//							Set<String> keys = headmap.keySet();
//							for (String key : keys) {
//								connection
//								.setRequestProperty(key,
//										headmap.get(key));
//							}
//						}else {
//							connection
//							.setRequestProperty("content-Type",
//									"application/x-www-form-urlencoded; charset=utf-8");
//						}
//						OutputStream os = connection.getOutputStream();
//						os.write(msg.getBytes());
//					}
//					connection.connect();
//					if (connection.getResponseCode() == 200) {
//						BufferedInputStream is=new BufferedInputStream( connection.getInputStream());
//						byte[] b = new byte[1024];
//						StringBuffer buffer = new StringBuffer();
//						int i;
//						while ((i = is.read(b)) != -1) {
//							buffer.append(new String(b, 0, i));
//						}
//						httpback.back(buffer.toString(), connection.getResponseCode());
//					} else {
//						InputStream is = connection.getErrorStream();
//						byte[] b = new byte[1024];
//						StringBuffer buffer = new StringBuffer();
//						int i;
//						while ((i = is.read(b)) != -1) {
//							buffer.append(new String(b, 0, i));
//						}
//						httpback.back(buffer.toString(), connection.getResponseCode());
//					}
//
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//		}).start();
	}
	public static void sendHttpForBack(String url, final String method,Map<String, String> headmap,httpback back) {
		sendHttpForBack(url, "", method,"utf-8",headmap,back);
	}
	public static void sendHttpForBack(String url, final String method,String charset,httpback back) {
		sendHttpForBack(url, "", method,charset,null,back);
	}
	public static void sendHttpForBack(String url, String charset,httpback back) {
		sendHttpForBack(url, "", GET,charset,null,back);
	}
	private static String getPostMsg(HashMap< String, String> map) {
		StringBuilder builder = new StringBuilder();
		Iterator<String> key = map.keySet().iterator();
		while (true) {
			String str = key.next();
			if (map.get(str) != null) {
				builder.append(str + "=" + map.get(str));
				builder.append("&");
			}
			if (!key.hasNext()) {
				builder.deleteCharAt(builder.length() - 1);
				return  builder.toString();
			}
		}
	}
    public interface   httpback{
    	void back(String data, int responseCode);
    }
}
