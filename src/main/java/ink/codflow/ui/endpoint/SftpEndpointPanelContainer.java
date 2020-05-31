package ink.codflow.ui.endpoint;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.hadoop.hdfs.protocol.proto.ClientNamenodeProtocolProtos.CheckAccessRequestProto;

import ink.codflow.bo.AuthenticationBO;
import ink.codflow.bo.ClientEndpointBO;
import ink.codflow.sync.consts.AuthDataType;
import ink.codflow.sync.consts.AuthenticationType;
import ink.codflow.sync.consts.ClientTypeEnum;
import ink.codflow.util.IdGen;

public class SftpEndpointPanelContainer implements EndpointPanelContainer {

	public static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 18);

	
	boolean checked;

	public static final String TYPE = "SFTP";

	ClientEndpointBO endpointBO;

	JPanel sftpPanel;
	JTextField nameField;

	JTextField hostField;

	JTextField userField;

	JTextField pathField;

	JPasswordField passwordField;
	static final JLabel nameLabel = new JLabel("Name:");
	static final JLabel hostLabel = new JLabel("Host:");
	static final JLabel userLabel = new JLabel("User:");
	static final JLabel passwordLabel = new JLabel("Password:");
	static final JLabel pathLabel = new JLabel("Path:");

	public void init() {

		GridBagLayout gridBag = new GridBagLayout();
		
		sftpPanel = new JPanel(gridBag);
		nameField = new JTextField(10);
		nameField.setFont(DEFAULT_FONT);
		hostField = new JTextField(10);
		hostField.setFont(DEFAULT_FONT);
		userField = new JTextField(10);
		userField.setFont(DEFAULT_FONT);
		passwordField = new JPasswordField(10);
		passwordField.setFont(DEFAULT_FONT);
		pathField = new JTextField(10);
		pathField.setFont(DEFAULT_FONT);

		addComponentToLayout(gridBag, nameLabel);
		addComponentToLayoutEndLine(gridBag, nameField);
		addComponentToLayout(gridBag, hostLabel);
		addComponentToLayoutEndLine(gridBag, hostField);
		addComponentToLayout(gridBag, userLabel);
		addComponentToLayoutEndLine(gridBag, userField);
		addComponentToLayout(gridBag, passwordLabel);
		addComponentToLayoutEndLine(gridBag, passwordField);
		addComponentToLayout(gridBag, pathLabel);
		addComponentToLayoutEndLine(gridBag, pathField);
		
		
		sftpPanel.add(nameLabel);
		sftpPanel.add(nameField);
		sftpPanel.add(hostLabel);
		sftpPanel.add(hostField);
		sftpPanel.add(userLabel);
		sftpPanel.add(userField);
		sftpPanel.add(passwordLabel);
		sftpPanel.add(passwordField);
		sftpPanel.add(pathLabel);
		sftpPanel.add(pathField);

	}

	public JPanel panel() {
		return this.sftpPanel;
	}

	boolean checkAccess() {

		return false;
	}

	@Override
	public ClientEndpointBO getEndpointBO() {

		String name = nameField.getText();
		String user = userField.getText();
		String host = hostField.getText();

		char[] passwd = passwordField.getPassword();

		// TODO
		String password = new String(passwd);

		String path = pathField.getText();

		AuthenticationBO authenticationBO = new AuthenticationBO();
		authenticationBO.setAuthType(AuthenticationType.PASSWORD);
		authenticationBO.addParam(AuthDataType.HOST, host);
		authenticationBO.addParam(AuthDataType.USERNAME, user);
		authenticationBO.addParam(AuthDataType.PASSWD, password);
		authenticationBO.addParam(AuthDataType.ID, IdGen.genUUID());

		return ClientEndpointBO.builder().name(name).type(ClientTypeEnum.SFTP).authenticationBO(authenticationBO).rootPath(path).build();
	}
	
 	void addComponentToLayout (GridBagLayout gridBag,Component comp) {
 		GridBagConstraints constraints = new GridBagConstraints();
		gridBag.addLayoutComponent(comp, constraints);
 		
 	}
 	void addComponentToLayoutEndLine (GridBagLayout gridBag,Component comp) {
 		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.BOTH;
		gridBag.addLayoutComponent(comp, constraints);
 		
 	}
	

}
