package client.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSONObject;

import constant.InetConfig;
import pojo.message.ChatMessage;
import pojo.message.LoginMessage;
import pojo.message.Message;
import pojo.message.OfflineMessage;
import sun.util.logging.resources.logging;

public class UDPSocket{

	private static DatagramSocket send;
	
	private static Charset charset = Charset.forName("utf-8");
	
	static {
		try {
			send = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void sendMsg(Message msg) {
		
		String string = JSONObject.toJSONString(msg);
		System.out.println(string);
		byte[] bs = string.getBytes(charset);
		try {
			DatagramPacket dp = new DatagramPacket(bs, bs.length, InetAddress.getByName(msg.getDstIp()), InetConfig.CLIENT_CHAT_PORT);
			send.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		LoginMessage lm = new LoginMessage();
		lm.setDstIp("192.168.43.248");
		lm.setId("00000000");
		UDPSocket.sendMsg(lm);
		
	}
	
	/**
	 * 
	 * 发送消息的方法
	 * @param cm ： 当前ip、消息内容、目的ip
	 * 
	 */
	public static void sendChatMsg(ChatMessage cm) {
		
		String string = JSONObject.toJSONString(cm);
		byte[] bs = string.getBytes(charset);
		try {
			DatagramPacket dp = new DatagramPacket(bs, bs.length, InetAddress.getByName(cm.getDstIp()), InetConfig.CLIENT_CHAT_PORT);
			send.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void sendLoginMsg(LoginMessage lm) {
		
		String string = JSONObject.toJSONString(lm);
		byte[] bs = string.getBytes(charset);
		try {
			DatagramPacket dp = new DatagramPacket(bs, bs.length, InetAddress.getByName(lm.getDstIp()), InetConfig.CLIENT_CHAT_PORT);
			send.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void sendOfflineMsg(OfflineMessage om) {
		
		String string = JSONObject.toJSONString(om);
		byte[] bs = string.getBytes(charset);
		try {
			DatagramPacket dp = new DatagramPacket(bs, bs.length, InetAddress.getByName(om.getDstIp()), InetConfig.CLIENT_CHAT_PORT);
			send.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
