package com.platform.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public abstract class FileUtils extends org.apache.commons.io.FileUtils {

	/**
	 * 创建文件，如果文件路径上的目录不存在，创建相应目录.
	 */
	public static void createFile(final String filename) throws IOException {
		final File file = new File(filename);
		if (file.getParentFile() != null && file.getParentFile().exists() == false) {
            if (file.getParentFile().mkdirs() == false) {
            	throw new IOException("创建目录失败：" + file.getParentFile().getAbsolutePath());
            }
        }
		file.createNewFile();
	}
	
	/**
	 * 复制文件
	 * @param originalPath
	 * @param targetPath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void copyFile(String originalPath, String targetPath) throws FileNotFoundException, IOException {
		IOUtils.copy(new FileInputStream(originalPath), new FileOutputStream(targetPath));
	}
	
	public static void main(String[] args) {
		try {
			
			
			FileUtils.copyFile("E:\\2011-11-14Test\\mpml.files\\nb.D110000jingjigcb_20111114-01.pagebrief.1.jpg", "E:\\aaa\\");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getExtensionName(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length() - 1))) {   
                return "."+filename.substring(dot + 1);   
            }   
        }   
        return filename;   
    }   
}
