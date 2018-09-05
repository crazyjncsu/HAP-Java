package com.beowulfe.hap;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.beowulfe.hap.impl.HomekitAdvertiser;
import com.beowulfe.hap.impl.HomekitWebHandler;

/**
 * A server for exposing standalone Homekit accessory (as opposed to a Bridge accessory which contains multiple accessories).
 * Each standalone accessory will have its own pairing information, port, and pin. Instantiate this class via 
 * {@link HomekitServer#createStandaloneAccessory(HomekitAuthInfo, HomekitAccessory)}.
 *
 * @author Andy Lintner
 */
public class HomekitStandaloneAccessoryServer {
	
	private final HomekitRoot root;

	HomekitStandaloneAccessoryServer(HomekitAccessory accessory,
			HomekitWebHandler webHandler, HomekitAdvertiser.MdnsRegistry mdnsRegistry,
			HomekitAuthInfo authInfo) throws UnknownHostException, IOException {
		root = new HomekitRoot(accessory.getLabel(), webHandler, authInfo, mdnsRegistry);
		root.addAccessory(accessory);
	}
	
	/**
	 * Begins advertising and handling requests for this accessory.
	 */
	public void start() {
		root.start();
	}

	

}
