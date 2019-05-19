package client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import client.socket.UDPSocket;
import constant.InetConfig;
import constant.MessageType;
import pojo.UserPojo;
import pojo.message.ChatMessage;
import pojo.message.FileMessage;
import pojo.message.LoginMessage;
import pojo.message.OfflineMessage;
import utils.ui.JTextPaneUtils;

import javax.swing.*;

public class Client extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public static final int BUFFER_SIZE = 1024;// 每次发送、接收的缓冲区大小
	private String localUserName;
	private String localPwd;
	// “在线用户列表ListModel”,用于维护“在线用户列表”中显示的内容
	private final DefaultListModel<String> onlinUserDlm = new DefaultListModel<String>();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	private JPanel contentPane;
	private JTextField textFieldUserName;
	private JPasswordField passwordFieldPwd;
	private JTextField textFieldMsgToSend;
	private JTextPane textPaneMsgRecord;
	private JList<String> listOnlineUsers;
	private JButton btnLogon;
	private JButton btnSendMsg;
	private JButton btnSendFile;
	private JButton btnNewButton;

	private BlockingDeque<UserPojo> userlist = new LinkedBlockingDeque<>();
	private Charset charset = Charset.forName("utf-8");
	
	// 注册框,主界面为父界面,注册界面为子界面
	private RegiestDialog regiestDialog = new RegiestDialog(this);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		
		// 客户端
		setTitle("\u5BA2\u6237\u7AEF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 612, 397);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelNorth = new JPanel();
		panelNorth.setBorder(new EmptyBorder(0, 0, 5, 0));
		contentPane.add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.X_AXIS));

		JLabel lblUserName = new JLabel("\u7528\u6237\u540D\uFF1A");
		panelNorth.add(lblUserName);

		textFieldUserName = new JTextField();
		panelNorth.add(textFieldUserName);
		textFieldUserName.setColumns(10);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut);

		JLabel lblPwd = new JLabel("密码：");
		panelNorth.add(lblPwd);
		passwordFieldPwd = new JPasswordField();
		passwordFieldPwd.setColumns(10);
		panelNorth.add(passwordFieldPwd);

		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut_4);

		// 显示注册的 界面
		btnNewButton = new JButton("注册");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				regiestDialog.setVisible(true);
				Client.this.setVisible(false);
			}
		});
		panelNorth.add(btnNewButton);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut_1);

		btnLogon = new JButton("\u767B\u5F55"); // “登录”按钮
		btnLogon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ByteBuffer recvBuff=ByteBuffer.allocate(10240); //接收缓冲区
			    ByteBuffer sendBuff=ByteBuffer.allocate(10240); //发送缓冲区
			    
			    // 和服务器建立连接
				SocketChannel clientChannel = null;
				try {
					
					clientChannel = SocketChannel.open();
					clientChannel.connect(InetConfig.inetAddress);
					
					// 如果按钮的文本是登录，表示这是一个登录操作
					if (btnLogon.getText().equals("登录")) {
						localUserName = textFieldUserName.getText().trim();
						localPwd = new String(passwordFieldPwd.getPassword());
						
						// 判断 账户密码  ，需要不为 ""
						if (localUserName.length() > 0 && localPwd.length() > 0) {
							
							// 封装登录消息
							LoginMessage lm = new LoginMessage();
							lm.setId(localUserName);
							lm.setPwd(localPwd);
							
						    sendBuff.clear(); 	//清空发送缓冲区
				            sendBuff=ByteBuffer.wrap(JSONObject.toJSONString(lm).getBytes(charset)); //字符串包装到缓冲区
							clientChannel.write(sendBuff);
							recvBuff.clear(); 	//清空接收缓冲区
							clientChannel.read(recvBuff);
							recvBuff.flip(); 	//指针回至收到数据的起点
				            String serverReply=charset.decode(recvBuff).toString();
				            
				            // 账号密码错误
				            if("false".equals(serverReply)){
				            	JOptionPane.showMessageDialog(null, "账号或密码错误！");
				            	
				            // 同一账户 多次登录
				            } else if ("reLogin".equals(serverReply)) {
				            	JOptionPane.showMessageDialog(null, "此账户已登录！");
				            
				            // 返回来的是一个JSON字符串的时候表示是用户列表，此处不再做 正则表达式 验证（因为这个正则表达式不会写
				            } else {
				            	JOptionPane.showMessageDialog(null, "登录成功！");
				            	
								String msgRecord = dateFormat.format(new Date()) + " 登录成功\r\n";
								JTextPaneUtils.printTextLog(textPaneMsgRecord, msgRecord, Color.red);
								
								List<UserPojo> userArray = JSONArray.parseArray(serverReply, UserPojo.class);
								System.out.println(userArray);
								
								// 将服务器发过来的用户存在客户端 且 将用户id显示在面板上 且 通知其他所有的客户端自己上线
								Iterator<UserPojo> iterator = userArray.iterator();
								
								int i = 0;
								
								while(iterator.hasNext()) {
									UserPojo next = iterator.next();
									
									System.out.println((i++) + "   " + next);
									
									userlist.put(next);
									onlinUserDlm.addElement(next.getId());
									LoginMessage tempLm = new LoginMessage();
									tempLm.setDstId(next.getIp());
									tempLm.setId(next.getId());
									UDPSocket.sendMsg(tempLm);
									
								}
								
								// 开启 UDP 监听
								new Thread(new ListenUDP()).start();
								
				            	// 将“登录”按钮设为“退出”按钮
								btnLogon.setText("退出");
								// 将发送文件按钮设为可用状态
								btnSendFile.setEnabled(true);
								// 将发送消息按钮设为可用状态
								btnSendMsg.setEnabled(true);
				            }
						}
						
					// 如果 按钮的文本是 退出 表示这是一个 退出操作
					} else if (btnLogon.getText().equals("退出")) {
						if (JOptionPane.showConfirmDialog(null, "是否退出?", "退出确认",	JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
							OfflineMessage om = new OfflineMessage();
							om.setId(localUserName);
						    sendBuff.clear(); 	//清空发送缓冲区
				            sendBuff=ByteBuffer.wrap(JSONObject.toJSONString(om).getBytes(charset)); //字符串包装到缓冲区
							clientChannel.write(sendBuff);
							recvBuff.clear(); 	//清空接收缓冲区
							System.exit(0);
						}
					}
					clientChannel.close();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "连接错误", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
				
			}
		});
		panelNorth.add(btnLogon);

		// 消息记录
		JSplitPane splitPaneCenter = new JSplitPane();
		splitPaneCenter.setResizeWeight(1.0);
		contentPane.add(splitPaneCenter, BorderLayout.CENTER);

		JScrollPane scrollPaneMsgRecord = new JScrollPane();
		scrollPaneMsgRecord.setViewportBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"\u6D88\u606F\u8BB0\u5F55", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPaneCenter.setLeftComponent(scrollPaneMsgRecord);

		textPaneMsgRecord = new JTextPane();
		scrollPaneMsgRecord.setViewportView(textPaneMsgRecord);

		// 在线用户
		JScrollPane scrollPaneOnlineUsers = new JScrollPane();
		scrollPaneOnlineUsers.setViewportBorder(
				new TitledBorder(null, "\u5728\u7EBF\u7528\u6237", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPaneCenter.setRightComponent(scrollPaneOnlineUsers);

		listOnlineUsers = new JList<String>(onlinUserDlm);
		scrollPaneOnlineUsers.setViewportView(listOnlineUsers);

		JPanel panelSouth = new JPanel();
		panelSouth.setBorder(new EmptyBorder(5, 0, 0, 0));
		contentPane.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new BoxLayout(panelSouth, BoxLayout.X_AXIS));

		textFieldMsgToSend = new JTextField();
		panelSouth.add(textFieldMsgToSend);
		textFieldMsgToSend.setColumns(10);

		// 发送消息
		btnSendMsg = new JButton("\u53D1\u9001\u6D88\u606F"); // “发送消息”按钮
		btnSendMsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msgContent = textFieldMsgToSend.getText();
				if (msgContent.length() > 0) {
					
					String dstUserName = listOnlineUsers.getSelectedValue();
					if(dstUserName == null || dstUserName == "") {
						JOptionPane.showMessageDialog(null, "请选择聊天对象！");
						return;
					}
					
					ChatMessage cm = new ChatMessage();
					cm.setContent(msgContent);
					cm.setDstId(dstUserName);
					
					Iterator<UserPojo> iterator = userlist.iterator();
					while(iterator.hasNext()) {
						UserPojo next = iterator.next();
						if(next.getId().equals(dstUserName)) {
							cm.setDstIp(next.getIp());
							break;
						}
					}
					UDPSocket.sendMsg(cm);
				}
			}
		});

		// 连接符
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		panelSouth.add(horizontalStrut_2);
		panelSouth.add(btnSendMsg);

		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		panelSouth.add(horizontalStrut_3);

		// 发送文件
		btnSendFile = new JButton("\u53D1\u9001\u6587\u4EF6");
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String dstUserName = listOnlineUsers.getSelectedValue();
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.showOpenDialog(Client.this);
				File selectedFile = jFileChooser.getSelectedFile();// 获得用户选择的文件
				if (selectedFile == null) { // 参数检查
					JOptionPane.showMessageDialog(Client.this, "未选择任何文件");
					return;
				}
				FileMessage fm = new FileMessage();
				fm.setDstId(dstUserName);
				Iterator<UserPojo> iterator = userlist.iterator();
				while(iterator.hasNext()) {
					UserPojo next = iterator.next();
					if(next.getId().equals(dstUserName)) {
						fm.setDstIp(next.getIp());
						break;
					}
				}
				fm.setSrcId(localUserName);
				fm.setFileName(selectedFile.getAbsolutePath());
				fm.setType(MessageType.FILE_SEND);
				UDPSocket.sendMsg(fm);
				
			}
		});
		panelSouth.add(btnSendFile);
		// 将发送文件按钮设为不可用状态
		btnSendFile.setEnabled(false);
		// 将发送消息按钮设为不可用状态
		btnSendMsg.setEnabled(false);
	}
	
	class ListenUDP implements Runnable{

		@Override
		public void run() {
			try {
				System.out.println("开始监听 UDP 端口:   ");
				
				// 监听消息接受端口
				DatagramSocket revieve = new DatagramSocket(InetConfig.CLIENT_CHAT_PORT);
				while(true){
					byte[] bs = new byte[10240];
					DatagramPacket dp = new DatagramPacket(bs, bs.length);
					revieve.receive(dp);		//这个方法具有等待功能,等待发送端发送过来的数据
					InetAddress sendAddress = dp.getAddress();
					System.out.println("发送端是:"+sendAddress.getHostAddress());
					byte[] data = dp.getData();
					int len = dp.getLength();
					String receiveMsg = new String(data, 0, len, charset);
					System.out.println("发送端说:  "+receiveMsg);
					
					 // 接受到的字符串解码成JSONObject对象
			        JSONObject recvObj = JSONObject.parseObject(receiveMsg);
			        String type = null;
			        
			        // 无法解码成JSONObject对象 表示接受到的不是 JSON 字符串
			        if(recvObj != null)
			        	type = (String)recvObj.get("type");
			        
			        System.out.println("收到的消息格式：     " + type);
			        
			        // 能正确解码的字符串才能 进入其他过程
			        if(type != null) {
			        	
			        	// 客户端之间的聊天消息
			        	if (MessageType.CHAT.equals(type)) {
			        		
			        		ChatMessage chatMessage = JSON.parseObject(receiveMsg, ChatMessage.class);
			        		String content = chatMessage.getSrcId() + " 说：  " + chatMessage.getContent();
			        		JTextPaneUtils.printTextLog(textPaneMsgRecord, content, Color.blue);
			        		
			        	// 某客户端下线的消息
			        	} else if (MessageType.OFFLINE.equals(type)) {
			        		
			        		OfflineMessage offlineMessage = JSON.parseObject(receiveMsg, OfflineMessage.class);
			        		UserPojo up = new UserPojo();
			        		up.setId(offlineMessage.getId());
			        		up.setIp(sendAddress.getHostAddress());
			        		userlist.remove(up);
			        		// 从面板上删除用户的信息
			        		onlinUserDlm.removeElement(up.getId());
			        		
			        	// 某客户端上线的消息
			        	} else if (MessageType.LOGIN.equals(type)) {
			        		
			        		LoginMessage loginMessage = JSON.parseObject(receiveMsg, LoginMessage.class);
			        		UserPojo up = new UserPojo();
			        		up.setId(loginMessage.getId());
			        		up.setIp(sendAddress.getHostAddress());
			        		userlist.put(up);
			        		
			        		System.out.println(loginMessage.getId() + "上线后，userList:" + userlist);
			        		
			        		onlinUserDlm.addElement(up.getId());
			        	
			        	// 文件请求的消息
			        	} else if (MessageType.FILE_SEND.equals(type)) {
			        		FileMessage fm = JSON.parseObject(receiveMsg, FileMessage.class);
			        		String fileSize = String.valueOf(fm.getFileSize() / 1024) + "KB";
			        		int result = JOptionPane.showConfirmDialog(Client.this,
			        				"是否接收来自" + fm.getSrcId() + "的 " + new File(fm.getFileName()).getName() + "(" + fileSize + ")文件", "提示!",
			        				JOptionPane.YES_NO_OPTION);
			        		if(result == 0) {
			        			ServerSocket serverSocket;
			        			try {
			        				serverSocket = new ServerSocket(0);// 打开一个随机端口
			        				String address = serverSocket.getInetAddress().getHostAddress();
			        				int port = serverSocket.getLocalPort();
			        				
				        			fm.setType(MessageType.FILE_REPLY_YES);
				        			fm.setSrcPort(port);
				        			fm.setSrcIp(address);
				        			fm.setDstIp(dp.getAddress().getHostAddress());
				        			UDPSocket.sendMsg(fm);
			        				
			        				System.out.println(address + "  " + port);
			        				Socket client = serverSocket.accept();
			        				new Thread() {
										@Override
										public void run() {
											try {
												System.out.println("小样:"+client.getInetAddress().getHostAddress());
												//3.获取输入流,读取客户端发来数据
												InputStream in = client.getInputStream();
												//4.创建文件的输出流,把数据写到文件中
												String picName = "D:\\"+System.currentTimeMillis()+".png";
												FileOutputStream fos = new FileOutputStream(picName);
												//5.循环 从输入流读取客户端数据, 写入到文件中
												byte[] bs = new byte[1024];
												int len = 0;
												while((len=in.read(bs))!=-1){
													fos.write(bs, 0, len);
												}//1小时
												System.out.println("客户端的文件已经保存完毕,可以查看了"+picName);
												//6.告知客户端,文件真的真的真的上传成功
												try {
													Thread.sleep(10000);
												} catch (InterruptedException e) {
													e.printStackTrace();
												}
												OutputStream out = client.getOutputStream();
												out.write("true".getBytes(charset));
												client.close();
												in.close();
												out.close();
												fos.close();
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
			        				}.start();
			        			} catch (IOException e) {
			        				e.printStackTrace();
			        			}
			        		}
			        	} else if (MessageType.FILE_REPLY_YES.equals(type)) {
			        		FileMessage fm = JSON.parseObject(receiveMsg, FileMessage.class);
			        		//1.创建Socket对象,连接服务器
			        		Socket client = new Socket(fm.getSrcId(), fm.getSrcPort());
			        		System.out.println("连接服务器成功..");
			        		//2.获取输出流,把数据写向服务器
			        		OutputStream out = client.getOutputStream();
			        		//3.创建文件的输入流,读取本地的文件数据
			        		FileInputStream fis = new FileInputStream(fm.getFileName());
			        		//4.循环,读取本地文件,写到服务器
			        		byte[] sendBs = new byte[1024];
			        		int sendLen = 0;
			        		while((sendLen=fis.read(sendBs))!=-1){
			        			out.write(sendBs, 0, sendLen);
			        		}
			        		//关闭输出流
			        		client.shutdownOutput();
			        		//5.获取服务器反馈的信息
			        		InputStream in = client.getInputStream();
			        		byte[] bs1 = new byte[1024];
			        		int len1 = in.read(bs1);
			        		System.out.println("服务器说:" + new String(bs1,0,len1));
			        		//6关闭
			        		client.close();
			        		out.close();
			        		fis.close();
			        	}
			        }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}