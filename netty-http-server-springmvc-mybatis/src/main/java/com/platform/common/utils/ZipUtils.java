package com.platform.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public abstract class ZipUtils {
	
	/** 
     * 将文件压缩为zip文件.
     */ 
    public static void zip(final String zipPath, final String dir) throws IOException { 
        FileOutputStream fos = null; 
        ZipOutputStream zipout = null; 
        try { 
            fos = new FileOutputStream(zipPath);
            zipout = new ZipOutputStream(fos);
            final File file = new File(dir); 
            zip(zipout, file, "", Filter.TRUE_FILTER);
        } finally {
        	IOUtils.closeQuietly(zipout);
        	IOUtils.closeQuietly(fos);
        } 
    }
    
    /**
     * 将文件压缩输出到out.
     */
    public static void zip(final OutputStream out, final String dir) throws IOException {
    	ZipOutputStream zipout = null;
    	try {
    		zipout = new ZipOutputStream(out);
    		zip(zipout, new File(dir), "", Filter.TRUE_FILTER);
    	} finally {
    		IOUtils.closeQuietly(zipout);
    	}
    }
    

    /**
     * 将文件夹压缩输出到out（不输出文件夹本身）.
     * 
     * @param out 输出流
     * @param dir 文件夹
     */
    public static void zipDir(final OutputStream out, final String dir) throws IOException {
    	zipDir(out, dir, Filter.TRUE_FILTER);
    }
    
    /**
     * 过滤器，只压缩符合条件的文件.
     * 
     * @author minwh
     */
    public static interface Filter {
    	boolean needs(File file);
    	public static final Filter TRUE_FILTER = new TrueFilter();
    }
    
    public static class TrueFilter implements Filter {
    	public boolean needs(final File file) {
			return true;
		}
    }
    
    public static void zipDir(final OutputStream out, final String dir, final Filter filter) throws IOException {
    	final File file = new File(dir);
    	if (!file.isDirectory()) {
    		throw new IllegalArgumentException("dir参数必须指定一个文件夹");
    	}
    	ZipOutputStream zipout = null;
    	try {
    		zipout = new ZipOutputStream(out);
        	final File[] fileArr = file.listFiles(); // 递归写入目录下的所有文件 
            for(final File f : fileArr) {
            	zip(zipout, f, "", filter);
            } 
    	} finally {
    		IOUtils.closeQuietly(zipout);
    	}
    }
    
    /** 
     * 将文件 file写入到 zip输出流中.
     */ 
    private static void zip(final ZipOutputStream out, final File file, String base, final Filter filter) throws IOException {
    	if (!filter.needs(file)) {
    		return;
    	}
        if (file.isDirectory()) {
            base += file.getName() + "/"; 
            final ZipEntry entry = new ZipEntry(base); // 创建一个目录条目 [以 / 结尾] 
            out.putNextEntry(entry); // 向输出流中写入下一个目录条目 
            final File[] fileArr = file.listFiles(); // 递归写入目录下的所有文件 
            for(final File f : fileArr) {
            	zip(out, f, base, filter); 
            } 
        } else if(file.isFile()) { 
            base += file.getName(); 
            final ZipEntry entry = new ZipEntry(base); // 创建一个文件条目 
            out.putNextEntry(entry); // 向输出流中写入下一个文件条目 
            final FileInputStream in = new FileInputStream(file); // 写入文件内容 
            int data = in.read(); // 为了防止出现乱码 
            while(data != -1) { // 此处按字节流读取和写入 
            	out.write(data); 
               	data = in.read(); 
            } 
            IOUtils.closeQuietly(in);
        } 
    }
    
    public static void unzip(final File zipPath, final String outdir) throws IOException {
    	unzip(zipPath.getAbsolutePath(), outdir);
    }
    
    /** 
     * 解压 zip 文件.
     */ 
    public static void unzip(final String zipPath, final String outdir) throws IOException { 
        FileInputStream fis = null; 
        ZipInputStream zipin = null; 
        new File(outdir).mkdirs();
        try { 
            fis = new FileInputStream(zipPath); // 文件输入流 
            zipin = new ZipInputStream(fis); // zip 输入流 
            ZipEntry entry = null; // 文件条目 
            entry = zipin.getNextEntry(); 
            while(entry != null){ 
                if(entry.isDirectory()) { 
                    String name = entry.getName(); 
                    name = name.substring(0, name.length() - 1); 
                    final File dir = new File(outdir + File.separator + name); 
                    dir.mkdirs(); // 创建目录 
                } else { 
                    final String name = entry.getName(); 
                    final File file = new File(outdir + File.separator + name); 
                    final File parentFile = file.getParentFile();
					parentFile.mkdirs();
                    file.createNewFile(); // 创建文件 
                    final FileOutputStream fos = new FileOutputStream(file); 
                    int data = zipin.read(); 
                    while(data != -1){ 
                        fos.write(data); 
                        data = zipin.read(); 
                    } 
                    fos.flush();
                    IOUtils.closeQuietly(fos);
                } 
                entry = zipin.getNextEntry(); 
            }
        } finally { 
            IOUtils.closeQuietly(zipin);
            IOUtils.closeQuietly(fis);
        } 
    }
}
