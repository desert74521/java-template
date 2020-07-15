package com.test.business.interfaces.forcommon.service.impl;

import org.springframework.stereotype.Service;

import com.test.business.interfaces.forcommon.service.ForCommonService;
import com.test.protocol.ProtocolTool;
import com.test.protocol.oss.vo.OssTokenVo;

@Service
public class ForCommonServiceImpl implements ForCommonService {

	@Override
	public OssTokenVo genetorOssToken(String uploaderType) {
		return ProtocolTool.genetorOssToken(uploaderType);
	}

	@Override
	public String genetorOssTokenForDownload(String link, int day) {
		link = ProtocolTool.replaceEndPoint(link);
		return ProtocolTool.genetorOssTokenForDownload(link, day);
	}

}
