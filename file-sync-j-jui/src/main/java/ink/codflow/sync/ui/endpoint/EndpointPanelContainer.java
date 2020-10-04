package ink.codflow.sync.ui.endpoint;

import javax.swing.JPanel;

import ink.codflow.sync.manager.*;

public interface EndpointPanelContainer {

	void init();

	JPanel panel();

	Endpoint getEndpointBO();
}
