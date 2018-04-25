package com.yc.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

/**
 * @author Yue Chang
 * @ClassName: HttpUtil
 * @Description: HTTP CLIENT类用于发送HTTP请求
 * @date 2016年9月22日 上午10:59:19
 * @since 1.0
 */
public class HttpUtil {

    private static final Logger logger = Logger.getLogger(HttpUtil.class);

    private HttpUtil() {}
    /**
     * 连接超时 （单位：秒）
     */
    private static final int CONNECTION_TIME_OUT = 30;

    /**
     * 读取数据超时 （单位：秒）
     */
    private static final int READ_TIME_OUT = 60;

    public static Map<String, Integer> proxyHostMap = new HashMap<String, Integer>() {
        {
            put("111.231.237", 39954);
            put("123.206.203", 2797);
            put("111.231.20", 21926);
        }
    };


    /**
     * 设置http代理
     *
     * @param url
     * @return
     * @throws IOException
     * @category 设置代理
     * @author dd
     * @since IOS xxx/Android xxx
     */
    public static HttpURLConnection setHttpProxy(URL url) throws IOException {
        InetSocketAddress addr = getRandomInetSocketAddress();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
        return (HttpURLConnection) url.openConnection(proxy);
    }

    public static InetSocketAddress getRandomInetSocketAddress(){

        int i = new Random().nextInt(3);
        if(i > 1){
            return new InetSocketAddress("211.159.165.195", 53135);
        } else{
            return new InetSocketAddress("211.159.166.225", 51212);
        }
    }


    /**
     * @param strURL
     * @param params 请求参数
     * @return
     * @category 发送GET请求
     * @author Yue Chang
     * @date 2016年9月22日 上午11:43:28
     * @since 1.0.0
     */
    public static String doGetWithProxy(String strURL, Map<String, String> params, String charSet) {
        HttpURLConnection httpConn = null;
        InputStreamReader input = null;
        BufferedReader bufReader = null;
        try {
            strURL += getUrlParam(params, charSet);
            Authenticator.setDefault(new MimvpProxyJava.MyAuthenticator("08ed0b663f17", "aa1d63dd24"));
            URL url = new URL(strURL);
            httpConn = setHttpProxy(url);
            httpConn.setConnectTimeout(1000 * CONNECTION_TIME_OUT);// 设置连接主机超时（单位：毫秒）
            httpConn.setReadTimeout(1000 * READ_TIME_OUT);// 设置从主机读取数据超时（单位：毫秒）
            httpConn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
            input = new InputStreamReader(httpConn.getInputStream(), charSet);
            bufReader = new BufferedReader(input);
            String line = "";
            StringBuilder contentBuf = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                contentBuf.append(line);
            }
            String result = contentBuf.toString();
            return result;
        } catch (Exception e) {
            logger.error("【异常】从远程获取数据", e);
            return "";
        } finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                    bufReader = null;
                }
                if (input != null) {
                    input.close();
                    input = null;
                }
                if (httpConn != null) {
                    httpConn.disconnect();//断开连接
                    httpConn = null;
                }
            } catch (Exception e2) {
                logger.error(e2);
            }
        }
    }

    /**
     * @param strURL
     * @param params 请求参数
     * @return
     * @category 发送GET请求
     * @author Yue Chang
     * @date 2016年9月22日 上午11:43:28
     * @since 1.0.0
     */
    public static String doGet(String strURL, Map<String, String> params, String charSet) {
        HttpURLConnection httpConn = null;
        InputStreamReader input = null;
        BufferedReader bufReader = null;
        try {
            strURL += getUrlParam(params, charSet);
            URL url = new URL(strURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(1000 * CONNECTION_TIME_OUT);// 设置连接主机超时（单位：毫秒）
            httpConn.setReadTimeout(1000 * READ_TIME_OUT);// 设置从主机读取数据超时（单位：毫秒）
            httpConn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
            input = new InputStreamReader(httpConn.getInputStream(), charSet);
            bufReader = new BufferedReader(input);
            String line = "";
            StringBuilder contentBuf = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                contentBuf.append(line);
            }
            String result = contentBuf.toString();
            return result;
        } catch (Exception e) {
            logger.error("【异常】从远程获取数据", e);
            return "";
        } finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                    bufReader = null;
                }
                if (input != null) {
                    input.close();
                    input = null;
                }
                if (httpConn != null) {
                    httpConn.disconnect();//断开连接
                    httpConn = null;
                }
            } catch (Exception e2) {
                logger.error(e2);
            }
        }
    }



    /**
     * @param charSet
     * @return
     * @category 将map参数转化为url参数
     * @author Yue Chang
     * @date 2016年9月22日 上午11:14:34
     * @since 1.0.0
     */
    public static String getUrlParam(Map<String, String> params, String charSet) {
        StringBuffer url = new StringBuffer();
        // 组装参数
        if (params != null && !params.isEmpty()) {
            url.append("?");
            for (String key : params.keySet()) {
                try {
                    if (!StringUtils.isBlank(params.get(key))) {

                        url.append(key + "=" + URLEncoder.encode(params.get(key).toString(), charSet) + "&");
                    } else {
                        url.append(key + "=&");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            url.deleteCharAt(url.length() - 1);
        }
        return url.toString();
    }
}
