package com.xxl.job.core.util;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * HttpClient4.3工具类
 * Created by daihuan on 2017/10/17.
 */
public class HttpClientUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);


    /**
     * post请求传输json参数
     *
     * @param url
     *            url地址
     * @param jsonParam
     *            参数
     * @return
     */
    public static String httpPost(String url, JSONObject jsonParam) {
        String str = "";
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        try {
            if (null != jsonParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(),
                        "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            //请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                try {
                    //读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    //把json字符串转换成json对象
                    //jsonResult = JSONObject.parseObject(str);
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url,e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url,e);
        } finally {
            httpPost.releaseConnection();
        }
        return str;
    }


    /**
     * 发送get请求
     * @param url
     * @param socketTimeout
     * @param connectTimeout
     * @return
     */
    public static String httpGet(String url,int socketTimeout,int connectTimeout) {
        String strResult="";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        request.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(request);
            //请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                strResult = EntityUtils.toString(entity, "utf-8");
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败,发生异常:" + url,e);
        } finally {
            request.releaseConnection();
        }
        return  strResult;
    }

    public static void main(String[] args) {
        //TODO 测试代码
        /*String getUrl="http://localhost:8080/comdhhello/system/helloworld.json?userName=daihuan";
        String resultGet=httpGet(getUrl,2000,2000);
        System.out.println(resultGet);*/
    }
}
