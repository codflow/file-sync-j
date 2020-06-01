package ink.codflow.sync.ui.endpoint;

import java.awt.Font;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractEndpointPanelContainer implements EndpointPanelContainer{
	public static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 18);

	static final AtomicInteger endpointIdGenerator = new AtomicInteger(0);

	
	protected int generateEndpointId(){
		return endpointIdGenerator.getAndIncrement();
	}
}
