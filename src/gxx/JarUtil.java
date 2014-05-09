package gxx;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Web Browser
 * @author �����Ͽ���
 */
public class JarUtil {
	// jar����
	private String jarName;
	// jar�����ھ���·��
	private String jarPath;

	public JarUtil(Class clazz) {
		String path = clazz.getProtectionDomain().getCodeSource().getLocation()
				.getFile();
		try {
			path = java.net.URLDecoder.decode(path, "UTF-8");
		} catch (java.io.UnsupportedEncodingException ex) {
			Logger.getLogger(JarUtil.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		java.io.File jarFile = new java.io.File(path);
		this.jarName = jarFile.getName();
		java.io.File parent = jarFile.getParentFile();
		if (parent != null) {
			this.jarPath = parent.getAbsolutePath();
		}
	}

	/**
	 * ��ȡClass������Jar��������
	 * 
	 * @return Jar���� (���磺C:\temp\demo.jar �򷵻� demo.jar )
	 */
	public String getJarName() {
		try {
			return java.net.URLDecoder.decode(this.jarName, "UTF-8");
		} catch (java.io.UnsupportedEncodingException ex) {
			Logger.getLogger(JarUtil.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return null;
	}

	/**
	 * ȡ��Class�����ڵ�Jar��·��
	 * 
	 * @return ����һ��·�� (���磺C:\temp\demo.jar �򷵻� C:\temp )
	 */
	public String getJarPath() {
		try {
			return java.net.URLDecoder.decode(this.jarPath, "UTF-8");
		} catch (java.io.UnsupportedEncodingException ex) {
			Logger.getLogger(JarUtil.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return null;
	}
}
