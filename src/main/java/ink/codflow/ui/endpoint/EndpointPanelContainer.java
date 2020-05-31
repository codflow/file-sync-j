package ink.codflow.ui.endpoint;

import javax.swing.JPanel;

import ink.codflow.bo.ClientEndpointBO;

public interface EndpointPanelContainer {

	void init();

	JPanel panel();

	ClientEndpointBO getEndpointBO();
}
