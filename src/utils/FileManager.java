package utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import pojo.FilePojo;

public class FileManager {
	private Map<String, List<FilePojo>> filePojos;

	private final static FileManager INSTANCE = new FileManager();
	
	private FileManager() {
		filePojos = new ConcurrentHashMap<String, List<FilePojo>>();
	}

	public static FileManager getInstance() {
		return INSTANCE;
	}
	
	public Map<String, List<FilePojo>> getFilePojos() {
		return filePojos;
	}

	public void setFilePojos(Map<String, List<FilePojo>> filePojos) {
		this.filePojos = filePojos;
	}

	public boolean addFilePojo(String userid, FilePojo filePojo) {
		
		if(filePojo.getMd5() == null || filePojo.getSrcId() == null)
			return false;
		List<FilePojo> list = filePojos.get(userid);
		if(list == null || list.size() == 0)
			list = new LinkedList<>();
		if(list.contains(filePojo))
			return false;
		list.add(filePojo);
		filePojos.put(userid, list);
		return true;
	}
	
	public static void main(String[] args) {
		
		FileManager fm = FileManager.getInstance();
		FilePojo filePojo1 = new FilePojo();
		filePojo1.setMd5("!@");
		filePojo1.setSrcId("12");
		fm.addFilePojo("123", filePojo1);
		
		System.out.println(fm.getFilePojos());
		FilePojo filePojo2 = new FilePojo();
		filePojo2.setMd5("!@");
		filePojo2.setSrcId("124");
		fm.addFilePojo("123", filePojo2);
		System.out.println(fm.getFilePojos());
	}
	
	
}
