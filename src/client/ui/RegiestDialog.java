package client.ui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.alibaba.fastjson.JSONObject;

import constant.InetConfig;
import pojo.message.RegisterMessage;
import utils.RegsUtils;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingConstants;

public class RegiestDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	//父窗口
	JFrame parentFrame = null;
	
	private JTextField textFieldUserName;
	private JPasswordField passwordField;
	private JPasswordField passwordFieldCheck;

	private Charset charset = Charset.forName("utf-8");
	
	/**
	 * Create the dialog.
	 */
	public RegiestDialog(JFrame parentFrame) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//一旦出现注册界面，父界面（客户端就要隐藏）
				RegiestDialog.this.setVisible(false);
				parentFrame.setVisible(true);
			}
		});
		this.parentFrame = parentFrame;
		setResizable(false);
		setBounds(100, 100, 291, 162);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("用户名：");
		lblNewLabel.setBounds(20, 10, 54, 15);
		contentPanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("密码：");
		lblNewLabel_1.setBounds(20, 35, 73, 15);
		contentPanel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("确认密码：");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_2.setBounds(20, 60, 63, 15);
		contentPanel.add(lblNewLabel_2);
        
		textFieldUserName = new JTextField();
		textFieldUserName.setBounds(109, 7, 135, 21);
		contentPanel.add(textFieldUserName);
		textFieldUserName.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(109, 32, 135, 21);
		contentPanel.add(passwordField);

		passwordFieldCheck = new JPasswordField();
		passwordFieldCheck.setBounds(109, 57, 135, 21);
		contentPanel.add(passwordFieldCheck);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				textFieldUserName.setText("");
				passwordField.setText("");
				passwordFieldCheck.setText("");
				JButton okButton = new JButton("注册");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String userName = textFieldUserName.getText().trim();
						String pwd = new String(passwordField.getPassword());
						String pwdCheck = new String(passwordFieldCheck.getPassword());
						if (userName == null || !RegsUtils.isValidEmail(userName)) {
							JOptionPane.showMessageDialog(RegiestDialog.this, "请输入合法的邮箱！");
							return;
						} else if(pwd == null || pwdCheck == null || pwd.length() < 6) {
							JOptionPane.showMessageDialog(RegiestDialog.this, "密码的长度至少需要6位！");
							return;
						} else if (!pwd.equals(pwdCheck)) {
							JOptionPane.showMessageDialog(RegiestDialog.this, "两次密码输入不一致！");
							return;
						}
						
						SocketChannel clientChannel = null;
						try {
							clientChannel = SocketChannel.open();
							clientChannel.connect(InetConfig.inetAddress);
						} catch (IOException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "连接错误", JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						}
						
						RegisterMessage registerMessage = new RegisterMessage();
						registerMessage.setId(userName);
						registerMessage.setPwd(pwd);
						
						ByteBuffer recvBuff=ByteBuffer.allocate(1024); //接收缓冲区
					    ByteBuffer sendBuff=ByteBuffer.allocate(1024); //发送缓冲区
						System.out.println(userName + "   " + pwd);
					    sendBuff.clear(); //清空发送缓冲区
			            sendBuff=ByteBuffer.wrap(JSONObject.toJSONString(registerMessage).getBytes(charset)); //字符串包装到缓冲区
			            try {
							clientChannel.write(sendBuff);
							recvBuff.clear(); //清空接收缓冲区
							clientChannel.read(recvBuff);
							recvBuff.flip(); //指针回至收到数据的起点
				            String serverSide=charset.decode(recvBuff).toString();
				            System.out.println(serverSide);
				            if("true".equals(serverSide)){
				            	JOptionPane.showMessageDialog(null, "注冊成功！");
				            	RegiestDialog.this.setVisible(false);
								parentFrame.setVisible(true);
				            }else if("false".equals(serverSide))
				            	JOptionPane.showMessageDialog(null, "邮箱已被使用！");
							
						} catch (IOException e2) {
							e2.printStackTrace();
						}
			            
			            try {
							clientChannel.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
			            
					}
				});
				//比较触发事件的按钮是不是这个按钮
				okButton.setActionCommand("OK");  
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("取消");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						RegiestDialog.this.setVisible(false);
						parentFrame.setVisible(true);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
