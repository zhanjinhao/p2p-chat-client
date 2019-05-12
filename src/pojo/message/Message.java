package pojo.message;

import java.io.Serializable;

import constant.AgreementsConfig;
import constant.MessageType;

/**
 * 
 * 所有消息的根类，只包含目的用户。<br/>
 * 系統指定：所有的消息都要指定目的用户、目的ip、目的端口、源用户、源ip、源端口，即使在当前功能中没有作用
 * @author ISJINHAO
 *
 */
public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String type = MessageType.ROOT;
	
	public String getType() {
		return type;
	}
	
	
	private String dstId;
	
	private String dstIp;
	
	private Integer dstPort; 
	
	private String srcId;
	
	private String srcIp;
	
	private Integer srcPort;
	
	public Message() {}
	
	public Message(String dstId, String dstIp, Integer dstPort, String srcId, String srcIp, Integer srcPort) {
		this.dstId = dstId;
		this.dstIp = dstIp;
		this.dstPort = dstPort;
		this.srcId = srcId;
		this.srcIp = srcIp;
		this.srcPort = srcPort;
	}

	public String getDstId() {
		return dstId;
	}

	public void setDstId(String dstId) {
		this.dstId = dstId;
	}

	public String getDstIp() {
		return dstIp;
	}

	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}

	public Integer getDstPort() {
		return dstPort;
	}

	public void setDstPort(Integer dstPort) {
		this.dstPort = dstPort;
	}

	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	public String getSrcIp() {
		return srcIp;
	}

	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}

	public Integer getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(Integer srcPort) {
		this.srcPort = srcPort;
	}
	
	public Boolean isPublicMessage() {
		return AgreementsConfig.PUBLIC_MESSGE.equals(dstId);
	}

	@Override
	public String toString() {
		return "Message [dstId=" + dstId + ", dstIp=" + dstIp + ", dstPort=" + dstPort + ", srcId=" + srcId + ", srcIp="
				+ srcIp + ", srcPort=" + srcPort + "]";
	}
	
}
