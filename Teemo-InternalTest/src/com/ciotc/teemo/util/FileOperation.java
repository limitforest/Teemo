package com.ciotc.teemo.util;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;




public class FileOperation {
	/**
	 * 根据文件对话框，选取文件
	 * @param parent
	 * @return
	 */
	public static String chooseFile(Component parent){
		JFileChooser chooser = new JFileChooser("Database");
		//TxtFileFilter txtFilter = new TxtFileFilter();
		//JavaFileFilter javaFilter = new JavaFileFilter();
		TmoFileFilter tmoFileFilter = new TmoFileFilter();
		// For test.
		
		chooser.addChoosableFileFilter(tmoFileFilter);
		chooser.setFileFilter(tmoFileFilter);
		//chooser.addChoosableFileFilter(txtFilter);
		//chooser.addChoosableFileFilter(javaFilter);
		//chooser.setFileFilter(txtFilter);
		int returnVal = chooser.showOpenDialog(parent);
		if(returnVal == chooser.APPROVE_OPTION){
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
	
	/**
	 * 根据文件对话框，保存文件
	 * @param parent
	 * @return
	 */
	public static String saveFile(Component parent,String srcName){
		JFileChooser chooser = new JFileChooser("D:\\");
		//TxtFileFilter txtFilter = new TxtFileFilter();
		//JavaFileFilter javaFilter = new JavaFileFilter();
		TmoFileFilter tmoFileFilter = new TmoFileFilter();
		// For test.
		chooser.setSelectedFile(new File(srcName));
		//chooser.addChoosableFileFilter(tmoFileFilter);
		//chooser.setFileFilter(tmoFileFilter);
		//chooser.addChoosableFileFilter(txtFilter);
		//chooser.addChoosableFileFilter(javaFilter);
		//chooser.setFileFilter(txtFilter);
		
		int returnVal = chooser.showSaveDialog(parent);
		if(returnVal == chooser.APPROVE_OPTION){
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
	
	public static void delFile(String path2) {
		File file = new File(path2);
		String fileParent = file.getParent();

		if (file.isFile() && file.exists()) {
			file.delete();
			JOptionPane.showMessageDialog(null, "文件:  "+path2+"删除成功。");
			File dir = new File(file.getParent());
			String[] dirFile = dir.list();
			if (dirFile.length == 0) {
				/*if (JOptionPane.showConfirmDialog(new JFrame(), "目录为空。是否删除空目录？", "请确认",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {*/
					dir.delete();
					JOptionPane.showMessageDialog(null, "空目录:   " + dir
							+ "    删除成功！");
//				} 
			/*else {
					JOptionPane.showMessageDialog(null, "用户取消删除操作。");
				}*/
			}
		} else {
			File dir = new File(fileParent);
			String[] dirFile = dir.list();
			// JOptionPane.showMessageDialog(null, "文件:   " + path2 +
			// "    不存在。删除失败！");
			// 删除空目录.
			if (dir.isDirectory() && dirFile.length == 0) {
				if (JOptionPane.showConfirmDialog(new JFrame(), "文件:   "
						+ path2 + "    不存在。删除失败！" + "是否删除空目录？", "请确认",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					dir.delete();
					JOptionPane.showMessageDialog(null, "空目录:   " + dir
							+ "    删除成功！");
				} else {
					JOptionPane.showMessageDialog(null, "用户取消删除操作。");
				}
			}
		}
	}
	/**
	 * 根据文件 得到文件内的二进制数据
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public  static byte[] readFile(File file)  {
		
		
		if (file.exists() && file.isFile()) {
			long fileLength = file.length();
			if (fileLength > 0L) {
				
				try {
					byte[] content = FileUtils.readFileToByteArray(file);
					return content;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
				
//				DataInputStream fis=null;
//				try {
//					fis = new DataInputStream( new BufferedInputStream(
//							new FileInputStream(file)));
//					byte[] content = new byte[(int) fileLength];
//					fis.read(content);
//					//fis.close();
//					//fis = null;
//					return content;
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					//加一个提示对话框 表明 文件找不到
//					return null;
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					//加一个提示对话框 表明 文件读取错误
//					return null;
//				}finally{
//					IOUtils.closeQuietly(fis);
//				}
				
				
			}
		} else {
			return null;
		}
		return null;
	}
	
	
	
}
class TmoFileFilter extends FileFilter {
	public String getDescription() {
		return "*.tmo(teemo源文件)";
	}

	public boolean accept(File file) {
		//String name = file.getName();
		//return name.toLowerCase().endsWith(".tmo");
		return check(file);
	}
	
	public boolean check(File file){
		if(file.isFile())
			return file.getName().toLowerCase().endsWith(".tmo");
		else
			return true;
		/*File[] files = file.listFiles();
		boolean b = false;
		for(File f:files){
			b = b | check(f);
			if(b == true)
				return true;
		}
		return b;*/
	}
}


class JavaFileFilter extends FileFilter {
	public String getDescription() {
		return "*.java(java源文件)";
	}

	public boolean accept(File file) {
		String name = file.getName();
		return name.toLowerCase().endsWith(".java");
	}
}

class TxtFileFilter extends FileFilter {
	public String getDescription() {
		return "*.txt(文本文件)";
	}

	public boolean accept(File file) {
		String name = file.getName();
		return name.toLowerCase().endsWith(".txt");
	}
}