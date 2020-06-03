package ink.codflow.sync.task;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import ink.codflow.sync.bo.AuthenticationBO;
import ink.codflow.sync.bo.ClientEndpointBO;
import ink.codflow.sync.bo.LinkBO;
import ink.codflow.sync.bo.ObjectBO;
import ink.codflow.sync.consts.AuthDataType;
import ink.codflow.sync.consts.AuthenticationType;
import ink.codflow.sync.consts.ClientTypeEnum;
import ink.codflow.sync.consts.FileSyncMode;

public class SyncTaskConductorTest {

	@Test
	public void test() {
		
		
		LinkBO linkBO = new LinkBO();
		
		linkBO.setId(66106);
		
		AuthenticationBO sAuthenticationBO = new AuthenticationBO();
		sAuthenticationBO.setId("dn-hkuq3r2");
		sAuthenticationBO.setAuthType(AuthenticationType.PASSWORD);
		sAuthenticationBO.addParam(AuthDataType.HOST,"192.168.105.153:22");
		sAuthenticationBO.addParam(AuthDataType.USERNAME, "debian");
		sAuthenticationBO.addParam(AuthDataType.PASSWD, "deb404069");
		String rootPath = "/home/debian/skc/";
		ClientEndpointBO sClientEndpointBO =  ClientEndpointBO.builder().authenticationBO(sAuthenticationBO).type(ClientTypeEnum.SFTP).rootPath(rootPath).id(6604).build();

		linkBO.setSrcEndpoint(sClientEndpointBO);
		
		AuthenticationBO dAuthenticationBO = new AuthenticationBO();
		dAuthenticationBO.setId("dn-hkuq3r2");
		String drootPath = "/home/centos/temp/dest/";
		
		ClientEndpointBO dClientEndpointBO =  ClientEndpointBO.builder().authenticationBO(dAuthenticationBO).type(ClientTypeEnum.LOCAL).rootPath(drootPath).id(33000).build();

		linkBO.setDistEndpoint(dClientEndpointBO);
		
		SyncTaskConductor conductor = new SyncTaskConductor();
		
		List<ObjectBO> selectedObjects = new ArrayList<ObjectBO>();
		
		
		ObjectBO objectBO = new ObjectBO();
		objectBO.setLinkId(66106);
		objectBO.setFile(false);
		objectBO.setName("moewmsnf");
		objectBO.setUri("msdifnwr/");
		
		selectedObjects.add(objectBO);
		SyncTask task = conductor.createSyncTask(linkBO, selectedObjects,FileSyncMode.FILE_INC);
		
		conductor.launch(task);
	    CountDownLatch countDownLatch = new CountDownLatch(1);
	    try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fail("Not yet implemented");
	}

}
