package ink.codflow.ui.endpoint;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ink.codflow.bo.ClientEndpointBO;
import ink.codflow.sync.consts.ClientTypeEnum;

public class LocalEndpointPanelContainer implements EndpointPanelContainer {

	public static final String TYPE = "LOCAL";
	public static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 18);

	ClientEndpointBO endpointBO;

	JPanel panel;

	JTextField pathField;
	JTextField nameField;

	static final JLabel pathLabel = new JLabel("Path:");
	static final JLabel nameLabel = new JLabel("Name:");

	public void init() {
		GridBagLayout gridBag = new GridBagLayout();
		panel = new JPanel(gridBag);
		pathField = new JTextField(10);
		pathField.setFont(DEFAULT_FONT);

		nameField = new JTextField(10);
		nameField.setFont(DEFAULT_FONT);

		addComponentToLayout(gridBag, nameLabel);
		addComponentToLayoutEndLine(gridBag, nameField);
		addComponentToLayout(gridBag, pathLabel);
		addComponentToLayoutEndLine(gridBag, pathField);
		
		
		panel.add(nameLabel);

		panel.add(nameField);
		panel.add(pathLabel);

		panel.add(pathField);
	}

	public JPanel panel() {
		return this.panel;
	}

	boolean checkAccess() {

		return false;
	}

	public ClientEndpointBO getEndpointBO() {

		String name = nameField.getText();
		String path = pathField.getText();

		ClientEndpointBO endpointBO = ClientEndpointBO.builder().name(name).type(ClientTypeEnum.LOCAL).rootPath(path)
				.build();

		return endpointBO;
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
