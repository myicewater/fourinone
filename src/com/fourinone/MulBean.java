package com.fourinone;

import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
/**
 * 配置文件工具类
 * @author 朱素海
 *
 */
public class MulBean extends ResourceBean
{
	private String nativeLangCode;
	/**
	 * 根据不同编码读取配置文件
	 * @param langCode
	 */
	public MulBean(String langCode)
	{
		super();
		resourcesName = "META-INF/config";
		init(langCode);
	}
	/**
	 * 根据不同编码初始化配置文件资源
	 * @param langCode
	 */
	public void init(String langCode)
	{
		if(langCode==null)
		{
			bundle = ResourceBundle.getBundle(resourcesName, Locale.getDefault());
		}
		else if(langCode.toUpperCase().equals("ISO-8859-1"))
		{
			nativeLangCode = "ISO-8859-1";
			bundle = ResourceBundle.getBundle(resourcesName, Locale.US);
		}
		else if(langCode.toUpperCase().equals("GB2312"))
		{
			nativeLangCode = "GB2312";
			bundle = ResourceBundle.getBundle(resourcesName, Locale.PRC);
		}
		else if(langCode.toUpperCase().equals("BIG5"))
		{
			nativeLangCode = "BIG5";
			bundle = ResourceBundle.getBundle(resourcesName, Locale.TAIWAN);//new Locale("zh", "TW");
		}
	}
	/**
	 * 根据key获取资源文件值，没有获取到则返回keyWord
	 */
	public String getString(String keyWord)
	{		
		return getString(keyWord, "");
	}
	/**
	 * 根据key获取资源文件值，没有获取到返回topStr+keyWord
	 * @param keyWord
	 * @param topStr
	 * @return
	 */
	public String getString(String keyWord, String topStr)
	{		
		String str = "";
		try
		{
			str = bundle.getString(keyWord);
					
		}
		catch(MissingResourceException ex)
		{
			str = topStr+keyWord;
			//System.err.println(ex);	
		}		
		return str;		
	}
	/**
	 * 返回空格的ISO-8859-1编码
	 * @return
	 */
	public String getSpace()
	{
		String space = "";
		if(nativeLangCode!=null&&nativeLangCode.equals("ISO-8859-1"))
			space = "&nbsp;";
	
		return space;
	}
	/**
	 * 返回文件内容
	 * @param relativeUri
	 * @return
	 */
	public String getFileString(String relativeUri){
		StringBuffer sb = new StringBuffer();
		try{
			Reader f = new InputStreamReader(this.getClass().getResourceAsStream(relativeUri));
			BufferedReader fb = new BufferedReader(f);
			String s = "";
			while((s=fb.readLine())!=null){
				sb = sb.append(s);
			}
			f.close();
			fb.close();
		}catch(IOException ex){
			//System.err.println(ex);
		}
		//System.out.println(sb);
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
		MulBean rb = new MulBean("ISO-8859-1");
		//try{Thread.sleep(10000L);}catch(Exception ex){}
		/*System.out.println(rb.getString("QSXYSJ"));
		System.out.println(rb.getString("YBB"));
		System.out.println(rb.getString("YGSJ"));*/
	}
}
