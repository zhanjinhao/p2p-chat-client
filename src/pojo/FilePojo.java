package pojo;

/**
 * 
 * 封装 发送的文件的信息
 * @author ISJINHAO
 *
 */
public class FilePojo {
	private String srcId;
	private String fileName;
	private long fileSize;
	private String md5;
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FilePojo))
			return false;
		FilePojo other = (FilePojo)obj;
		if(this.srcId.equals(other.getSrcId()) && this.md5.equals(other.getMd5()))
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.md5.hashCode() + this.srcId.hashCode();
	}
	@Override
	public String toString() {
		return "FilePojo [srcId=" + srcId + ", fileName=" + fileName + ", fileSize=" + fileSize + ", md5=" + md5 + "]";
	}
	
	
}