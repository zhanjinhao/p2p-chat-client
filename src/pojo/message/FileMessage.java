package pojo.message;

import pojo.FilePojo;

/**
 * 
 * 封装文件发送请求和文件接收响应的消息
 * @author ISJINHAO
 *
 */
public class FileMessage extends Message{

	private static final long serialVersionUID = 1L;
	
	private String fileName;
	
	private long fileSize;
	
	private String md5;
	
	private String reqOrRes;
	
	public String getReqOrRes() {
		return reqOrRes;
	}

	public void setReqOrRes(String reqOrRes) {
		this.reqOrRes = reqOrRes;
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
	
	public FilePojo generateFilePojo() {
		
		FilePojo filePojo = new FilePojo();
		filePojo.setFileName(this.fileName);
		filePojo.setFileSize(this.getFileSize());
		filePojo.setMd5(this.md5);
		filePojo.setSrcId(this.getSrcId());
		return filePojo;
		
	}
	
}
