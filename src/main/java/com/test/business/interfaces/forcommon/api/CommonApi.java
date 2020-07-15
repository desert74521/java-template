package com.test.business.interfaces.forcommon.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.business.interfaces.forcommon.service.ForCommonService;
import com.test.platform.aop.annotation.Auth;
import com.test.platform.apisetting.ApiRes;
import com.test.protocol.oss.vo.OssTokenVo;

@RestController
@RequestMapping("/common")
public class CommonApi {
		
	@Autowired
	private ForCommonService forCommonService;
		
	  
	@GetMapping(path = "/oss_token")
	@Auth
	public ApiRes<OssTokenVo> getOssToken() {
		String uploaderType = "";
		OssTokenVo ossToken = forCommonService.genetorOssToken(uploaderType);
	    return new ApiRes<OssTokenVo>(ossToken);
	}
	  
	@GetMapping(path = "/oss_token_download")
	@Auth
	public ApiRes<String> getOssTokenForDownload(@RequestParam("link") String link) {
		String ossDownloadToken = forCommonService.genetorOssTokenForDownload(link, 2);
	    return new ApiRes<String>(ossDownloadToken);
	}
}
