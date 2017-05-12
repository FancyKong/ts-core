package com.jdkcc.ts.web.controller;

import com.google.common.base.Throwables;
import com.jdkcc.ts.common.util.ValidateCode;
import com.jdkcc.ts.service.impl.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Controller
public class BasicController extends ABaseController {

	private final UserService userService;

    @Autowired
    public BasicController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 首页
     */
    @GetMapping(value = {"/","/index"})
    public String index(){
        return "index";
    }

    /**
     * 管理页面
     */
    @GetMapping(value = "admin")
    public String admin(){
        return "admin/datapanel";
    }

	/**
	 * 登陆页面
	 */
	@GetMapping(value = "/login")
	public String login(){
		return "admin/login";
	}
	
	@GetMapping("/403")
	public String unauthorizedRole(){
		return "error/403";
	}
	
	/**
	 * 生成验证码
	 * @param request 请求
	 * @param response 响应
	 * @throws IOException 图片生成错误
	 */
	@GetMapping(value = "/validateCode")
	public void validateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-cache");
		String verifyCode = ValidateCode.generateTextCode(ValidateCode.TYPE_NUM_ONLY, 4, null);
		request.getSession().setAttribute("validateCode", verifyCode);
		response.setContentType("image/jpeg");
		BufferedImage bim = ValidateCode.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null);
		ImageIO.write(bim, "JPEG", response.getOutputStream());
	}

	//文件存放路径
	private static final String FILE_PATH = "/cherish";

	@PostMapping("/imageUpload")
	@ResponseBody
	public String upload(@RequestParam("wangEditorH5File") MultipartFile multipartFile, HttpServletRequest request){
		String url = "";
		if (!multipartFile.isEmpty()) {
			File directory = new File(FILE_PATH);

			if (!directory.exists()) {
				directory.mkdirs();
			}

			try {
				String originalFilename = multipartFile.getOriginalFilename();

				String newFIleName = System.currentTimeMillis()//UUID.randomUUID().toString()
						+ originalFilename.substring(originalFilename.lastIndexOf("."));
				multipartFile.transferTo(new File(directory, newFIleName));
//				FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),
//				new File(directory,newFIleName));
				String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath() + "/";
				url = basePath + "imageDownload?filename=" + newFIleName;
			} catch (IOException e) {
				log.error("【上传出错】 {}", Throwables.getStackTraceAsString(e));
			}

		} // end if
		return url;
	}

	@GetMapping("/imageDownload")
	public ResponseEntity<byte[]> downloadImage(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException {
		File file = new File(FILE_PATH, filename);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", filename);
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(
				FileUtils.readFileToByteArray(file),
				headers, HttpStatus.OK);
	}
	
}
