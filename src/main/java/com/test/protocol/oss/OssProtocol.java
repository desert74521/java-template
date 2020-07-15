package com.test.protocol.oss;

import com.test.protocol.oss.vo.OssTokenVo;

public interface OssProtocol {
	
	public OssTokenVo genetorOssToken(String uploaderType);
	
	public String genetorOssTokenForDownload(String link, int day);
	
	public String replaceEndPoint(String link);
}
