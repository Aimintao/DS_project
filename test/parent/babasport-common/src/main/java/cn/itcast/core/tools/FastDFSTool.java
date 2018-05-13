package cn.itcast.core.tools;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

/**
 * FastDFS工具类
 * 
 * @author Administrator
 */
public class FastDFSTool {

	/**
	 * 上传文件到fastdfs中
	 * 
	 * @param bs 字节数组
	 * @param filename 文件名
	 * @return  文件上传后的路径
	 * @throws Exception 
	 */
	public static String uploadFile(byte[] bs, String filename) throws Exception {
		// 获得配置文件的绝对路径
		ClassPathResource classPathResource = new ClassPathResource(
				"fdfs_client.conf");
		String path = classPathResource.getClassLoader()
				.getResource("fdfs_client.conf").getPath();
		System.out.println(path);
		// 初始化客户端
		ClientGlobal.init(path);
		// 创建老大客户端
		TrackerClient trackerClient = new TrackerClient();
		// 获得老大的服务器端
		TrackerServer connection = trackerClient.getConnection();
		// 创建小弟客户端
		StorageClient1 storageClient1 = new StorageClient1(connection, null);

		// 获得扩展名
		String extension = FilenameUtils
				.getExtension(filename);

		// 通过小弟的客户端进行上传
		String filePath = storageClient1.upload_file1(bs, extension,
				null);
		System.out.println(filePath);
		return filePath;
	}
}
