package ink.codflow.sync.ui.endpoint;

import javax.swing.JPanel;

import ink.codflow.sync.bo.ClientEndpointBO;

public interface EndpointPanelContainer {

	void init();

	JPanel panel();

	ClientEndpointBO getEndpointBO();
}
