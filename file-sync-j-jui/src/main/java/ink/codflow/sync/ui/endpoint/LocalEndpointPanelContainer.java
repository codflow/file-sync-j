package ink.codflow.sync.ui.endpoint;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ink.codflow.sync.manager.*;
import ink.codflow.sync.consts.ClientTypeEnum;

public class LocalEndpointPanelContainer extends AbstractEndpointPanelContainer implements EndpointPanelContainer {

	public static final String TYPE = "LOCAL";


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

	public Endpoint getEndpointBO() {

		String name = nameField.getText();
		String path = pathField.getText();

		Endpoint clientEndpointBO =  new Endpoint();
		clientEndpointBO.setName(name);
		clientEndpointBO.setType(ClientTypeEnum.LOCAL);
		clientEndpointBO.setRootPath(path);
		return clientEndpointBO;
	}

	void addComponentToLayout(GridBagLayout gridBag, Component comp) {
		GridBagConstraints constraints = new GridBagConstraints();
		gridBag.addLayoutComponent(comp, constraints);

	}

	void addComponentToLayoutEndLine(GridBagLayout gridBag, Component comp) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.BOTH;
		gridBag.addLayoutComponent(comp, constraints);

	}

}
