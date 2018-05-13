package cn.itcast.core.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import cn.itcast.core.dictionary.Constants;
import cn.itcast.core.tools.FastDFSTool;

/**
 * 上传文件控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class UploadAction {

	// 上传单个文件
	@RequestMapping(value = "/uploadFile.do")
	@ResponseBody
	public Map<String, String> uploadFile(MultipartFile mpf) throws Exception {
		System.out.println(mpf.getOriginalFilename());

		String filePath = FastDFSTool.uploadFile(mpf.getBytes(),
				mpf.getOriginalFilename());

		// 将filepath封装成json格式
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("path", Constants.FDFS_SERVER + filePath);

		return hm;
	}

	// 上传多个文件
	@RequestMapping(value = "/uploadFiles.do")
	@ResponseBody
	public List<String> uploadFiles(@RequestParam MultipartFile[] mpfs)
			throws Exception {

		List<String> al = new ArrayList<String>();

		for (MultipartFile mpf : mpfs) {

			String filePath = FastDFSTool.uploadFile(mpf.getBytes(),
					mpf.getOriginalFilename());
			al.add(Constants.FDFS_SERVER + filePath);
		}
		return al;
	}

	// 接收富文本编辑器传递的图片(无敌版：不考虑文件的name，强行接收) 单张
	@RequestMapping(value = "/uploadFck.do")
	@ResponseBody
	public HashMap<String, Object> uploadFck(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// 将request强转为spring提供的MultipartRequest
		MultipartRequest mr = (MultipartRequest) request;

		// 获得MultipartRequest里面的所有文件
		Set<Entry<String, MultipartFile>> entrySet = mr.getFileMap().entrySet();

		for (Entry<String, MultipartFile> entry : entrySet) {
			MultipartFile mpf = entry.getValue();

			// 将文件上传到分布式文件系统，并返回文件的存储路径及名称
			String uploadFile = FastDFSTool.uploadFile(mpf.getBytes(),
					mpf.getOriginalFilename());

			// 返回json格式的字符串（图片的绝对网络存放地址）
			HashMap<String, Object> hashMap = new HashMap<String, Object>();

			// error和url名字都是固定死的
			hashMap.put("error", 0);
			hashMap.put("url", Constants.FDFS_SERVER + uploadFile);

			return hashMap;
		}
		return null;
	}
}
