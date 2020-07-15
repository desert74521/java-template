package com.test.business.interfaces.forcommon.service;

import com.test.protocol.oss.vo.OssTokenVo;

public interface ForCommonService {

	public OssTokenVo genetorOssToken(String uploaderType);
	
	public String genetorOssTokenForDownload(String link, int day);
}
