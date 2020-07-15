package com.test.protocol;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.protocol.oss.OssProtocol;
import com.test.protocol.oss.vo.OssTokenVo;

@Component
public class ProtocolTool {
	
	@Autowired
	private OssProtocol ossProtocol;
	
	
	private static ProtocolTool protocolTool;
	
    @PostConstruct
    public void init() {
    	protocolTool = this;
    }
    
    
	/**获取osstoken信息用于阿里云oss上传*/
	public static OssTokenVo genetorOssToken(String uploaderType) {
		return protocolTool.ossProtocol.genetorOssToken(uploaderType);
	}
	
	/**获取下载地址用于下载oss资源*/
	public static String genetorOssTokenForDownload(String link, int day) {
		return protocolTool.ossProtocol.genetorOssTokenForDownload(link, day);
	}
	
	/**替换endpoint*/
	public static String replaceEndPoint(String link) {
		return protocolTool.ossProtocol.replaceEndPoint(link);
	}
	
}
