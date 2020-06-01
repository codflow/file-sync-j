package ink.codflow.sync.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ink.codflow.sync.bo.ClientEndpointBO;
import ink.codflow.sync.bo.LinkBO;
import ink.codflow.sync.bo.TaskBO;
import ink.codflow.sync.bo.WorkerTaskBO;
import ink.codflow.sync.manager.FileSyncManager;
import ink.codflow.sync.task.SyncTask;

public class MainInterface {

	FileSyncManager fileSyncManager;

	Map<String, ClientEndpointBO> map = new HashMap<String, ClientEndpointBO>();

	JPanel mainpanel;
	JButton addEndPointBtn;

	JComboBox<String> srcComboBox;
	JComboBox<String> destComboBox;
	
	static final AtomicInteger linkIdGenerator = new AtomicInteger(0);

	public MainInterface(FileSyncManager fileSyncManager) {
		this.fileSyncManager = fileSyncManager;
	}

	public static void main(String[] args) {

		MainInterface mainInterface = new MainInterface(null);
		mainInterface.loadMainPanel();
	}

	public void loadMainPanel() {

		JFrame jf = new JFrame("File-Sync-J");
		jf.setSize(250, 250);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		GridLayout layout = new GridLayout();
		layout.setColumns(1);
		layout.setRows(6);
		mainpanel = new JPanel(layout);
		addEndPointBtn = new JButton("Add Endpoint");
		addEndPointBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createAddEndpointPanel();
			}
		});

		FlowLayout flayout = new FlowLayout();
		flayout.setAlignment(FlowLayout.CENTER);
		JPanel mpanel = new JPanel(flayout);

		mpanel.add(addEndPointBtn);
		mainpanel.add(mpanel);

		JLabel srcLabel = new JLabel("Source Endpoint:");
		mainpanel.add(srcLabel);

		srcComboBox = new JComboBox<>();

		mainpanel.add(srcComboBox);

		JLabel destLabel = new JLabel("Destnation Endpoint:");
		mainpanel.add(destLabel);

		destComboBox = new JComboBox<>();

		mainpanel.add(destComboBox);

		updateSelector();

		JPanel controlPanel = loadControllPanel();

		mainpanel.add(controlPanel);

		jf.setContentPane(mainpanel);

		jf.setVisible(true);
	}

	private JPanel loadControllPanel() {

		JButton syncBtn = new JButton("Sync");
		syncBtn.addActionListener(new ActionListener() { // NOSONAR no lambda for lower java version

			@Override
			public void actionPerformed(ActionEvent e) {
				TaskBO linkBO = createTaskBO();
				if (linkBO != null) {
					SyncTask task0 =  fileSyncManager.createSyncTask(linkBO); 
					fileSyncManager.launchTask(task0);
				}
			}
		});

		JButton pauseBtn = new JButton("Pause");
		JButton cancleBtn = new JButton("Cancle");

		JPanel controlPanel = new JPanel();
		controlPanel.add(syncBtn);
		controlPanel.add(pauseBtn);
		controlPanel.add(cancleBtn);
		return controlPanel;
	}

	void createAddEndpointPanel() {

		EndpointEditor endpointEditor = new EndpointEditor();
		endpointEditor.setFileSyncManager(fileSyncManager);
		PanelActionListener cActionListener = new PanelActionListener(this.map);

		endpointEditor.setActionListener(cActionListener);

		endpointEditor.init();

	}

	class PanelActionListener {

		Map<String, ClientEndpointBO> map;

		public PanelActionListener(Map<String, ClientEndpointBO> map) {

			this.map = map;
		}

		void delete(String clientEndpointKey) {

			map.remove(clientEndpointKey);
			afterAction();
		}

		void addOrUpdate(String key, ClientEndpointBO endpointBO) {
			this.map.put(key, endpointBO);
			afterAction();
		}

		void afterAction() {
			updateSelector();
		}

	}

	void updateSelector() {

		String[] type = this.map.keySet().toArray(new String[0]);

		String srcLast = (String) srcComboBox.getSelectedItem();
		String destLast = (String) destComboBox.getSelectedItem();

		ComboBoxModel<String> srcModle = new DefaultComboBoxModel<>(type);
		if (map.containsKey(srcLast)) {
			srcModle.setSelectedItem(srcLast);
		}
		srcComboBox.setModel(srcModle);

		srcComboBox.updateUI();
		ComboBoxModel<String> destModle = new DefaultComboBoxModel<>(type);

		if (map.containsKey(destLast)) {
			destModle.setSelectedItem(destLast);
		}

		destComboBox.setModel(destModle);
		destComboBox.updateUI();

	}

	ClientEndpointBO getSelectedSrcEndpoint() {

		Object name = this.srcComboBox.getSelectedItem();
		return this.map.get(name);
	}

	ClientEndpointBO getSelectedDestEndpoint() {

		Object name = this.destComboBox.getSelectedItem();
		return this.map.get(name);
	}

	LinkBO getSelectLinkBO() {
		ClientEndpointBO srcClientEndpointBO = getSelectedSrcEndpoint();
		ClientEndpointBO destClientEndpointBO = getSelectedDestEndpoint();

		if (srcClientEndpointBO != null && destClientEndpointBO != null) {
			LinkBO linkBO = new LinkBO();
			linkBO.setSrcEndpoint(srcClientEndpointBO);
			linkBO.setDistEndpoint(destClientEndpointBO);
			linkBO.setId(0);
			return linkBO;
		}

		return null;
	}

	TaskBO createTaskBO() {
		
		LinkBO linkBO = getSelectLinkBO();
		WorkerTaskBO workertaskBO = new WorkerTaskBO();
		workertaskBO.setLinkBO(linkBO);
		TaskBO taskBO = new TaskBO();
		taskBO.addWorkerTask(workertaskBO);
		return taskBO;
	}
	
	
	int getLinkId() {
		return linkIdGenerator.getAndIncrement();
	}
}
