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
import ink.codflow.sync.manager.*;

import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.exception.ArgumentsException;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.manager.FileSyncManager;
import ink.codflow.sync.task.SyncTask;

public class MainInterface {

	FileSyncManager fileSyncManager;

	Map<String, Endpoint> map = new HashMap<String, Endpoint>();

	Map<String, FileSyncMode> modeMap = new HashMap<String, FileSyncMode>();
	JFrame jf;
	JPanel mainpanel;
	JButton addEndPointBtn;
	JComboBox<String> modeComboBox;
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

		jf = new JFrame("File-Sync-J");

		jf.setSize(280, 250);
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

		modeComboBox = new JComboBox<>();
		loadModeSelector();
		// modeComboBox.add

		FlowLayout flayout = new FlowLayout();
		flayout.setAlignment(FlowLayout.CENTER);

		JPanel mpanel = new JPanel(flayout);

		mpanel.add(addEndPointBtn);
		JLabel modeLabel = new JLabel("Mode:");
		mpanel.add(modeLabel);
		mpanel.add(modeComboBox);

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
				Task taskBO = createTaskBO();
				if (taskBO != null) {

					if (fileSyncManager.getCurrentActiveTaskNumber() < 1) {
						SyncTask task0;
						try {
							task0 = fileSyncManager.createSyncTask(taskBO);
							fileSyncManager.launchTask(task0);

						} catch (FileException e1) {
							ErrorDialog.showQuickErrorDialog(e1);

						}
					} else {
						ErrorDialog.showQuickErrorDialog("A task is in progress now!");

					}

				}
			}
		});

		JButton pauseBtn = new JButton("Pause");
		pauseBtn.addActionListener(new ActionListener() { // NOSONAR no lambda for lower java version

			@Override
			public void actionPerformed(ActionEvent e) {
				ErrorDialog.showQuickErrorDialog("Not support yet");
			}
		});

		JButton cancleBtn = new JButton("Cancel");
		cancleBtn.addActionListener(new ActionListener() { // NOSONAR no lambda for lower java version

			@Override
			public void actionPerformed(ActionEvent e) {
				ErrorDialog.showQuickErrorDialog("Not support yet");
			}
		});

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

		Map<String, Endpoint> map;

		public PanelActionListener(Map<String, Endpoint> map) {

			this.map = map;
		}

		void delete(String clientEndpointKey) {

			map.remove(clientEndpointKey);
			afterAction();
		}

		void addOrUpdate(String key, Endpoint endpointBO) {
			this.map.put(key, endpointBO);
			afterAction();
		}

		void afterAction() {
			updateSelector();
		}

	}

	void loadModeSelector() {

		this.modeMap.put("INC", FileSyncMode.FILE_INC);
		this.modeMap.put("SYNC", FileSyncMode.SYNC);

		String[] modes = this.modeMap.keySet().toArray(new String[0]);
		ComboBoxModel<String> modeModle = new DefaultComboBoxModel<>(modes);
		modeComboBox.setModel(modeModle);
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

	Endpoint getSelectedSrcEndpoint() {

		Object name = this.srcComboBox.getSelectedItem();
		return this.map.get(name);
	}

	Endpoint getSelectedDestEndpoint() {

		Object name = this.destComboBox.getSelectedItem();
		return this.map.get(name);
	}

	Link getSelectLinkBO() {
		Endpoint srcClientEndpointBO = getSelectedSrcEndpoint();
		Endpoint destClientEndpointBO = getSelectedDestEndpoint();

		if (srcClientEndpointBO != null && destClientEndpointBO != null) {
			Link linkBO = new Link();
			linkBO.setSrcEndpoint(srcClientEndpointBO);
			linkBO.setDestEndpoint(destClientEndpointBO);
			linkBO.setId(0);
			return linkBO;
		}

		throw new ArgumentsException("Endpoint are not selected");
	}

	Task createTaskBO() {

		Link linkBO = getSelectLinkBO();
		FileSyncMode mode = getSelectedMode();
		linkBO.setMode(mode);
		WorkerTask workertaskBO = new WorkerTask();
		workertaskBO.setLinkBO(linkBO);
		Task taskBO = new Task();
		taskBO.addWorkerTask(workertaskBO);
		return taskBO;
	}

	private FileSyncMode getSelectedMode() {
		Object mode = this.modeComboBox.getSelectedItem();
		return this.modeMap.get(mode);
	}

	int getLinkId() {
		return linkIdGenerator.getAndIncrement();
	}
}
