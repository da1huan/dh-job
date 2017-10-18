package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.xxl.job.core.util.HttpClientUtils.httpGet;


/**
 * 任务Handler的一个URL（URL模式）
 * 
 * 开发步骤：
 * 1、新建一个继承com.xxl.job.core.handler.IJobHandler的Java类；
 * 2、该类被Spring容器扫描为Bean实例，如加“@Component”注解；
 * 3、添加 “@JobHander(value="自定义jobhandler名称")”注解，注解的value值为自定义的JobHandler名称，该名称对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 * 5、定时访问指定的URL,当前为Get请求
 * @author daihuan 2017-10-17
 */
@JobHander(value="urlJobHandler")
@Component
public class URLJobHandler extends IJobHandler {

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("XXL-JOB-URL mode");

		for(int i=0;i<params.length;i++){
			XxlJobLogger.log("params["+i+"]:"+params[i]);
		}
		String getUrl=params[0];
        int socketTimeout=5000;
        int connectTimeout=5000;
        if(params.length==3){
            socketTimeout=Integer.valueOf(params[1]);
            connectTimeout=Integer.valueOf(params[2]);
        }
		String resultGet=httpGet(getUrl,socketTimeout,connectTimeout);
		XxlJobLogger.log("Get Result:"+resultGet);
		return ReturnT.SUCCESS;
	}
	
}
