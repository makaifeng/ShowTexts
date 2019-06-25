package com.mkf_test.showtexts.utils;

import android.text.TextUtils;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    public static final String POST = "POST";
    public static final String GET = "GET";


    /**
     * 发送网络请求
     *
     * @return
     */
    public static void sendHttpForBack(final String url, final String msg, final String method, String charset, final Map<String, String> headmap, final httpBack httpback) {
        OkHttpClient client;
        if (url.startsWith("https://")) {
            client = getUnsafeOkHttpClient();
        } else {
            client = new OkHttpClient();
        }
        Request.Builder builder = new Request.Builder().url(url);
        if (!TextUtils.isEmpty(charset)){
            builder.addHeader("Content-Type","application/x-www-form-urlencoded;charset="+charset);
        }
        Request request = builder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//				Toast.makeText(x.app(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    httpback.back(response.body().string()
                            , 200);
                }
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
//						httpBack.back(buffer.toString(), connection.getResponseCode());
//					} else {
//						InputStream is = connection.getErrorStream();
//						byte[] b = new byte[1024];
//						StringBuffer buffer = new StringBuffer();
//						int i;
//						while ((i = is.read(b)) != -1) {
//							buffer.append(new String(b, 0, i));
//						}
//						httpBack.back(buffer.toString(), connection.getResponseCode());
//					}
//
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//		}).start();
    }

    public static void sendHttpForBack(String url, final String method, Map<String, String> headmap, httpBack back) {
        sendHttpForBack(url, "", method, "utf-8", headmap, back);
    }

    public static void sendHttpForBack(String url, final String method, String charset, httpBack back) {
        sendHttpForBack(url, "", method, charset, null, back);
    }

    public static void sendHttpForBack(String url, String charset, httpBack back) {
        sendHttpForBack(url, "", GET, charset, null, back);
    }

    private static String getPostMsg(HashMap<String, String> map) {
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
                return builder.toString();
            }
        }
    }

    //okHttp3添加信任所有证书
    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            };
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    x509TrustManager
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory( sslContext.getSocketFactory(),x509TrustManager);

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public interface httpBack {
        void back(String data, int responseCode);
    }
}
