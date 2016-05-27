package com.fourinone;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import com.fourinone.FileBatch.TryByteReadAdapter;
import com.fourinone.FileBatch.TryByteWriteAdapter;
import com.fourinone.FileBatch.TryIntReadAdapter;
import com.fourinone.FileBatch.TryIntWriteAdapter;
import com.fourinone.FileAdapter;
/**
 * fttp 适配器
 * @author 朱素海
 *
 */
public final class FttpAdapter
{
	//private URI fl = null;
	private FttpContractor fc;
	
	public FttpAdapter(String fttpPath) throws FttpException{
		//fl = FttpException.getURI(fttpPath);
		//fc = FttpContractor.getContractor(fttpPath);
		this(fttpPath, null);
	}
	
	public FttpAdapter(String fttpPath, String filename) throws FttpException{
		//System.out.println("fttpPath:"+fttpPath);
		fc = FttpContractor.getContractor(fttpPath, filename);
		//System.out.println("fc:"+fc);
	}
	
	public interface ByteFttpReadAdapter extends TryByteReadAdapter{
		public byte[] readAll() throws FttpException;//lockflag?setLock(true)
		public byte[] readAllSafety() throws FttpException;
	}
	
	public interface IntFttpReadAdapter extends TryIntReadAdapter{
		public int[] readIntAll() throws FttpException;
		public int[] readIntAllSafety() throws FttpException;
	}
	
	public interface FttpReadAdapter extends ByteFttpReadAdapter,IntFttpReadAdapter{}
	
	public interface IntFttpWriteAdapter extends TryIntWriteAdapter{
		public int writeInt(int[] its) throws FttpException;
		public int writeIntSafety(int[] its) throws FttpException;
		/*public void writeListInt(List<Integer> ls) throws FttpException;*/
	}
	
	public interface ByteFttpWriteAdapter extends TryByteWriteAdapter{
		public int write(byte[] bytes) throws FttpException;
		public int writeSafety(byte[] bytes) throws FttpException;
	}
	
	public interface FttpWriteAdapter extends ByteFttpWriteAdapter,IntFttpWriteAdapter{}
	
	public interface FileProperty{
		public boolean exists();
		public boolean isFile();
		public boolean isDirectory();
		public boolean isHidden();
		public boolean canRead();
		public boolean canWrite();
		public String getName();
		public String getParent();
		public String getPath();
		public long lastModified();
		public Date lastModifiedDate();
		public long length();
		public String[] list();
		public String getPathEncode();
	}
	
	public interface FttpAdapterOperate extends FttpReadAdapter,FttpWriteAdapter,FileProperty{}
	
	public FttpReadAdapter getFttpReader() throws FttpException{//param lock
		//return new FttpContractor(fl);//.getFttpReadAdapter(f);
		return fc;//fc.object();
	}
	
	public ByteFttpReadAdapter getByteFttpReader() throws FttpException{
		return getFttpReader();
	}
	
	public ByteFttpReadAdapter getByteFttpReader(long beginIndex, long bytesNum) throws FttpException{
		return getFttpReader(beginIndex, bytesNum);
	}
	
	public IntFttpReadAdapter getIntFttpReader() throws FttpException{
		return getFttpReader();
	}
	
	public IntFttpReadAdapter getIntFttpReader(long beginIndex, long intNum) throws FttpException{
		return getFttpReader(beginIndex, intNum);
	}
	/**
	 * 读取从beginIndex 开始，长度为bytesNum的数据
	 * @param beginIndex
	 * @param bytesNum
	 * @return
	 * @throws FttpException
	 */
	public FttpReadAdapter getFttpReader(long beginIndex, long bytesNum) throws FttpException{
		//return new FttpContractor(fl,beginIndex,bytesNum);
		/*FttpContractor fcread = fc.object();
		fcread.setReadArea(beginIndex, bytesNum);
		return fcread;*/
		fc.setReadArea(beginIndex, bytesNum);
		return fc;
	}
	
	public ByteFttpWriteAdapter getByteFttpWriter() throws FttpException{
		return getFttpWriter();
	}
	
	public IntFttpWriteAdapter getIntFttpWriter() throws FttpException{
		return getFttpWriter();
	}
	
	public ByteFttpWriteAdapter getByteFttpWriter(long beginIndex, long bytesNum) throws FttpException{
		return getFttpWriter(beginIndex, bytesNum);
	}
	
	public IntFttpWriteAdapter getIntFttpWriter(long beginIndex, long intNum) throws FttpException{
		return getFttpWriter(beginIndex, intNum);
	}
	
	public FttpWriteAdapter getFttpWriter() throws FttpException{
		return fc;//fc.object();//getFttpWriter(-1, -1);//lock
	}
	
	
	public FttpWriteAdapter getFttpWriter(long beginIndex, long bytesNum) throws FttpException{
		//return new FttpContractor(fl,beginIndex,bytesNum);
		/*FttpContractor fcwrite = fc.object();
		fcwrite.setWriteArea(beginIndex, bytesNum);
		return fcwrite;*/
		fc.setWriteArea(beginIndex, bytesNum);
		return fc;
	}
	/**
	 * 获取文件属性
	 * @return
	 * @throws FttpException
	 */
	public FileProperty getProperty() throws FttpException{
		fc.acquireProperty();
		return fc;
	}
	
	public FileProperty[] getChildProperty() throws FttpException{
		return fc.getChildProperty();
	}
	
	/*public FileProperty[] getChildProperty() throws FttpException{
		FileResult[] frarr = fc.getChildProperty();
		FttpContractor[] fcs = null;
		if(frarr!=null&&frarr.length>0){
			fcs = new FttpContractor[frarr.length];
			for(int i=0;i<frarr.length;i++){
				FttpContractor fc = getContractor();
				fc.acquireProperty(frarr[i]);
				fcs[i]=fc;
			}
		}
		return fcs;
	}*/
	/**
	 * 列出计算机上的硬盘目录
	 * @return
	 * @throws FttpException
	 */
	
	public String[] listRoots() throws FttpException{
		return fc.listRoots();
	}
	/**
	 * 获取集群文件系统根目录
	 * @return
	 */
	public static String[] fttpRoots(){
		return FttpContractor.getContractor().fttpRoots();
	}
	
	public static String[] fttpRootsPath(String[] roots){
		String[] rootspath = null;
		if(roots!=null){
			rootspath = new String[roots.length];
			for(int i=0;i<roots.length;i++)
				rootspath[i]="fttp://"+roots[i];
		}
		return rootspath;
	}
	
	public static String[] fttpRootsPathEncode(String[] roots){
		String[] rootspath = fttpRootsPath(roots);
		if(rootspath!=null){
			for(int i=0;i<rootspath.length;i++)
				rootspath[i]=ObjectBytes.getUtf8UrlString(rootspath[i]);//getUrlString encodeReplace
		}
		return rootspath;
	}
	/**
	 * 创建文件
	 * @return
	 * @throws FttpException
	 */
	public FttpAdapter createFile() throws FttpException{
		fc.create(true);
		return this;
	}
	/**
	 * 创建目录
	 * @return
	 * @throws FttpException
	 */
	public FttpAdapter createDirectory() throws FttpException{
		fc.create(false);
		return this;
	}
	/**
	 * 删除文件
	 * 
	 * 某些情况下delete方法没那么快及时生效
	 * 为了能高效读写文件，如果虚拟机内有缓存，导致不能马上删除，可以通过修改文件后缀，添加删除标志，再在其他时间同一删除
	 * @return
	 * @throws FttpException
	 */
	public boolean delete() throws FttpException{
		return fc.delete();
	}
	/**
	 * 把一个文件复制到指定路径
	 * @param topath
	 * @return
	 * @throws FttpException
	 */
	public FttpAdapter copyTo(String topath)throws FttpException{
		return copyTo(topath,FileAdapter.m(1));
	}
	
	public FttpAdapter copyTo(String topath, long every)throws FttpException{
		return fc.copy(topath,every)?new FttpAdapter(topath):null;//tryCopy
	}
	
	public Result<FttpAdapter> tryCopyTo(String topath){
		return tryCopyTo(topath,FileAdapter.m(1));
	}
	
	public Result<FttpAdapter> tryCopyTo(String topath, long every){
		return fc.tryCopy(topath,every);//tryCopy
	}
	/**
	 * 重命名文件，返回命名后的文件
	 * @param newname
	 * @return
	 * @throws FttpException
	 */
	public FttpAdapter rename(String newname)throws FttpException{
		return fc.rename(newname)?new FttpAdapter(this.getProperty().getParent(),newname):null;
	}
	
	//close() close pool
	/**
	 * 关闭文件操作器
	 */
	public void close(){
		fc = null;
	}
	/**
	 * 关闭并退出
	 */
	public void closeExit(){
		fc.exit();
		close();
	}
	
	public static void main(String[] args)
	{
	}
}