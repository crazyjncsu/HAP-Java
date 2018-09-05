package com.beowulfe.hap.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomekitAdvertiser {
	
	private static final String SERVICE_TYPE = "_hap._tcp.local.";
	
	private final MdnsRegistry mdnsRegistry;
	private boolean discoverable = true;
	private final static Logger logger = LoggerFactory.getLogger(HomekitAdvertiser.class);
	private boolean isAdvertising = false;
	
	private String label;
	private String mac;
	private int port;
	private int configurationIndex;

	public static interface MdnsRegistry {
		public void registerService(String serviceType, String label, int port, Map<String, String> properties) throws IOException;
		public void unregisterAllServices();
	}

	public static class JmdnsRegistry implements MdnsRegistry {
		private final JmDNS jmdns;

		public JmdnsRegistry(InetAddress localAddress) throws IOException {
            this.jmdns = JmDNS.create(localAddress);
        }

		public void registerService(String serviceType, String label, int port, Map<String, String> properties) throws IOException {
			this.jmdns.registerService(ServiceInfo.create(serviceType, label, port, 1, 1, properties));
		}

		public void unregisterAllServices() {
			this.jmdns.unregisterAllServices();
		}
	}
	
	public HomekitAdvertiser(MdnsRegistry mdnsRegistry) {
		this.mdnsRegistry = mdnsRegistry;
	}

	public synchronized void advertise(String label, String mac, int port, int configurationIndex) throws Exception {
		if (isAdvertising) {
			throw new IllegalStateException("Homekit advertiser is already running");
		}
		this.label = label;
		this.mac = mac;
		this.port = port;
		this.configurationIndex = configurationIndex;
		
		logger.info("Advertising accessory "+label);
	
		registerService();

		isAdvertising = true;
	}
	
	public synchronized void stop() {
		mdnsRegistry.unregisterAllServices();
	}
	
	public synchronized void setDiscoverable(boolean discoverable) throws IOException {
		if (this.discoverable != discoverable) {
			this.discoverable = discoverable;
			if (isAdvertising) {
				logger.info("Re-creating service due to change in discoverability to "+discoverable);
				mdnsRegistry.unregisterAllServices();
				registerService();
			}
		}
	}
	
	public synchronized void setConfigurationIndex(int revision) throws IOException {
		if (this.configurationIndex != revision) {
			this.configurationIndex = revision;
			if (isAdvertising) {
				logger.info("Re-creating service due to change in configuration index to "+revision);
				mdnsRegistry.unregisterAllServices();
				registerService();
			}
		}
	}
	
	private void registerService() throws IOException {
		logger.info("Registering "+SERVICE_TYPE+" on port "+port);
		Map<String, String> props = new HashMap<>();
		props.put("sf", discoverable ? "1" : "0");
		props.put("id", mac);
		props.put("md", label);
		props.put("c#", Integer.toString(configurationIndex));
		props.put("s#", "1");
		props.put("ff", "0");
		props.put("ci", "1");
		mdnsRegistry.registerService(SERVICE_TYPE, label, port, props);
	}
}
