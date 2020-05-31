package ink.codflow.ui;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import org.apache.http.impl.auth.NTLMEngineException;

import ink.codflow.bo.ClientEndpointBO;
import ink.codflow.manager.FileSyncManager;
import ink.codflow.ui.MainInterface.PanelActionListener;
import ink.codflow.ui.endpoint.EndpointPanelContainer;
import ink.codflow.ui.endpoint.LocalEndpointPanelContainer;
import ink.codflow.ui.endpoint.SftpEndpointPanelContainer;

public class EndpointEditor {

	JFrame jf;

	JPanel panel;

	JPanel controlPanel;

	CardLayout cardLayout;

	JPanel switchPanel;

	EndpointPanelContainer currentContainer;

	Map<String, EndpointPanelContainer> map = new HashMap<>();

	PanelActionListener cActionListener;

	FileSyncManager fileSyncManager;

	ClientEndpointBO verfiedEndpointBO;

	public static void main(String[] args) {

		EndpointEditor endpointEditor = new EndpointEditor();
		endpointEditor.loadMainPanel();
	}

	public void init() {
		loadMainPanel();
	}

	void loadMainPanel() {

		jf = new JFrame("Endpoint");
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jf.setSize(300, 300);

		loadComponents();

		FlowLayout layout = new FlowLayout();

		panel = new JPanel(layout);

		String[] listData = map.keySet().toArray(new String[0]);
		final JComboBox<String> srcComboBox = new JComboBox<>(listData);

		srcComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object it = srcComboBox.getSelectedItem();
					if (it instanceof String) {
						currentContainer = map.get(it);
						switchCard((String) it);

					}
					System.out.println(it);
				}

			}
		});

		panel.add(srcComboBox);

		cardLayout = new CardLayout(10, 10);

		switchPanel = new JPanel(cardLayout);

		for (Entry<String, EndpointPanelContainer> containrEntry : map.entrySet()) {

			String name = containrEntry.getKey();
			EndpointPanelContainer container = containrEntry.getValue();
			JPanel panel0 = container.panel();
			switchPanel.add(panel0, name);
		}

		panel.add(switchPanel);

		switchCard(map.entrySet().iterator().next().getKey());

		controlPanel = loadControllPanel();

		panel.add(controlPanel);

		jf.setContentPane(panel);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);

	}

	private JPanel loadControllPanel() {

		JButton testBtn = new JButton("Test");

		testBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				testEndpoint();
			}
		});

		JButton addBtn = new JButton("Add");

		addBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ClientEndpointBO clientEndpointBO = getVerfiedEndpointBO();

				if (clientEndpointBO != null) {
					String name = clientEndpointBO.getName();
					cActionListener.addOrUpdate(name, clientEndpointBO);
					jf.dispose();
				}

			}
		});

		JButton deleteBtn = new JButton("Delete");

		deleteBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ClientEndpointBO clientEndpointBO = currentContainer.getEndpointBO();
				String name = clientEndpointBO.getName();
				if (name != null) {
					cActionListener.delete(name);
					jf.dispose();
				}

			}
		});

		JButton cancleBtn = new JButton("Cancle");
		cancleBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jf.dispose();

			}
		});

		JPanel controlPanel = new JPanel();
		controlPanel.add(testBtn);
		controlPanel.add(addBtn);
		controlPanel.add(deleteBtn);
		controlPanel.add(cancleBtn);

		return controlPanel;
	}

	void loadComponents() {

		LocalEndpointPanelContainer endpointPanelContainer0 = new LocalEndpointPanelContainer();
		endpointPanelContainer0.init();

		map.put(LocalEndpointPanelContainer.TYPE, endpointPanelContainer0);

		SftpEndpointPanelContainer sftpEndpointPanelContainer = new SftpEndpointPanelContainer();
		sftpEndpointPanelContainer.init();
		map.put(SftpEndpointPanelContainer.TYPE, sftpEndpointPanelContainer);

	}

	void switchCard(String card) {
		this.currentContainer = this.map.get(card);
		cardLayout.show(switchPanel, card);

	}

	public void setActionListener(PanelActionListener cActionListener) {
		this.cActionListener = cActionListener;

	}

	public boolean testEndpoint() {

		ClientEndpointBO clientEndpointBO = this.currentContainer.getEndpointBO();

		if (fileSyncManager.testClient(clientEndpointBO)) {
			this.verfiedEndpointBO = clientEndpointBO;
			return true;
		}
		return false;
	}

	public FileSyncManager getFileSyncManager() {
		return fileSyncManager;
	}

	public void setFileSyncManager(FileSyncManager fileSyncManager) {
		this.fileSyncManager = fileSyncManager;
	}

	public ClientEndpointBO getVerfiedEndpointBO() {
		return verfiedEndpointBO;
	}

}
