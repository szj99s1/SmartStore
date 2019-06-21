package com.daofu.commons.utils.file;

import com.daofu.commons.propertie.CompUtils;
import com.daofu.commons.utils.DataUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author lichuang
 */
public class FileUtil {
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static String fileUpload(MultipartFile file, String path) {
		if (CompUtils.isOss()) {
			return ossUpload(file, path);
		} else {

			// 上传文件路径
			String rootpath = CompUtils.getFileRootPath();
			return localUpload(file, rootpath, path);
		}
	}

	private static String ossUpload(MultipartFile file, String path) {
		String fileUrl = null;
		try {
			// 流转换 将MultipartFile转换为oss所需的InputStream
			CommonsMultipartFile cf = (CommonsMultipartFile) file;
			DiskFileItem fi = (DiskFileItem) cf.getFileItem();
			InputStream fileContent = fi.getInputStream();
			String fileName = fi.getName();
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			fileName = DataUtils.getUuid() + "." + suffix;
			fileUrl = OSSManageUtil.getInstance().uploadFile(fileContent, path, fileName);
		} catch (Exception ex) {
			logger.error("上传失败：", ex);
		}
		return fileUrl;
	}

	private static String localUpload(MultipartFile file, String rootpath, String path) {
		String fileUrl = null;
		try {
			// 上传文件名
			String filename = file.getOriginalFilename();
			String suffix = filename.substring(filename.lastIndexOf(".") + 1);
			filename = DataUtils.getUuid() + "." + suffix;
			File filepath = new File(rootpath + path, filename);
			// 判断路径是否存在，如果不存在就创建一个
			if (!filepath.getParentFile().exists()) {
				filepath.getParentFile().mkdirs();
			}
			// 将上传文件保存到一个目标文件当中
			file.transferTo(filepath);
			fileUrl = CompUtils.getApplicationUrl() + "/images/" + path + "/" + filename;
		} catch (Exception ex) {
			logger.error("上传失败：", ex);
		}
		return fileUrl;
	}

	public static String commonFileUpload(HttpServletRequest request, File file, String path) {
		if (CompUtils.isOss()) {
			return commonOssUpload(file, path);
		} else {
			// 上传文件路径
			//String rootpath = request.getServletContext().getRealPath("/images/");
			String rootpath = CompUtils.getFileRootPath();
			return commonLocalUpload(file, rootpath, path);
		}
	}

	private static String commonOssUpload(File file, String path) {
		String fileUrl = "";
		try {
			// 流转换 将MultipartFile转换为oss所需的InputStream
			// CommonsMultipartFile cf = (CommonsMultipartFile) file;
			// DiskFileItem fi = (DiskFileItem) cf.getFileItem();
			InputStream fileContent = new FileInputStream(file);
			String fileName = file.getName();
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			fileName = UUID.randomUUID().toString().toLowerCase().replaceAll("-", "") + "." + suffix;
			fileUrl = OSSManageUtil.uploadFile(fileContent, path, fileName);
		} catch (Exception e) {
			logger.error("上传失败：", e);
		}
		return fileUrl;
	}

	private static String commonLocalUpload(File file, String rootpath, String path) {
		String fileUrl = null;
		try {
			// 上传文件名
			String filename = file.getName();
			String suffix = filename.substring(filename.lastIndexOf(".") + 1);
			filename = UUID.randomUUID().toString().toLowerCase().replaceAll("-", "") + "." + suffix;
			File filepath = new File(rootpath + path, filename);
			// 判断路径是否存在，如果不存在就创建一个
			if (!filepath.getParentFile().exists()) {
				filepath.getParentFile().mkdirs();
			}
			try {
				FileInputStream in = new FileInputStream(file);
				FileOutputStream out = new FileOutputStream(filepath);
				int len;
				byte[] buff = new byte[4096];
				while ((len = in.read(buff)) != -1) {
					out.write(buff, 0, len);
				}
				in.close();
				out.close();
			} catch (Exception e) {
				logger.error("上传失败：", e);
			}

			fileUrl = CompUtils.getFileRootPath() + "/images/" + path + "/" + filename;
		} catch (Exception e) {
			logger.error("上传失败：", e);
		}
		return fileUrl;
	}

	public static void delPic(String url) {
		if(StringUtils.isBlank(url)){
			return;
		}
		if (url.indexOf("aliyun")>=0) {
			ossDel(url);
		} else {
			//String filepath = request.getServletContext().getRealPath("/images/") + url.split("images/")[1];
			  String filepath= CompUtils.getFileRootPath()+ url.split("images/")[1];
			localDel(filepath);

		}
	}

	private static void ossDel(String filepath) {
		try {
			OSSManageUtil.deleteFile(filepath);
		} catch (Exception e) {
			logger.error("上传失败：", e);
		}

	}

	public static void localDel(String filepath) {
		try {
			File file = new File(filepath);
			if (file.exists() && file.isFile()) {
				file.delete();
			}
		} catch (Exception e) {
			logger.error("上传失败：", e);
		}

	}

	public static void main(String[] args) throws Exception{
		File file = new File("D:/greensoft/apache-tomcat-8.5.35/webapps/smart-store-console/images/validate/af49f683377a4d10b37517d758588dac.jpg");
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("YYYYMM");
		java.util.Date currentTime = new java.util.Date();//得到当前系统时间
		String yyyymm = formatter.format(currentTime); //将日期时间格式化 
	  String fileurl = FileUtil.commonOssUpload(file, "face/"+yyyymm);
	//FileUtil.ossDel("https://smartstore.oss-cn-hangzhou.aliyuncs.com/face/38cf09124d934259878fa2f07cae3975.jpg");
		//String fileurl = FileUtil.commonLocalUpload(file,"D:/greensoft/apache-tomcat-8.5.35/webapps/smart-store-console/images/", "face_log/"+yyyymm);
	   System.out.println(fileurl);
	}
}
