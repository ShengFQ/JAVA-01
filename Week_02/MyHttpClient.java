import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 使用httpClient访问netty服务器
 * */
public class MyHttpClient {

    public static void main(String[] args) {
        String urlStr="http://localhost:8801";
        String getResult=doGet(urlStr);
        System.out.println(String.format("get : %s",getResult));


    }
    /**
     * get请求没有参数,返回的是字符串
     * BufferedReader>InputStreamReader>
    * **/
    private static String doGet(String url){
        try{
            HttpClient httpClient=new DefaultHttpClient();
            HttpGet request=new HttpGet(url);
            HttpResponse response=httpClient.execute(request);
            if(response!=null && response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                String strResult=    EntityUtils.toString(response.getEntity());
                return strResult;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post请求(用于key-value格式的参数)
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map params){

        BufferedReader in = null;
        try {
            // 定义HttpClient
            HttpClient client = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));

            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));

                //System.out.println(name +"-"+value);
            }
            request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if(code == 200){
                //请求成功
                String strResult=    EntityUtils.toString(response.getEntity());
                return strResult;
            }
            else{    //
                System.out.println("状态码：" + code);
                return null;
            }
        }
        catch(Exception e){
            e.printStackTrace();

            return null;
        }
    }
}
