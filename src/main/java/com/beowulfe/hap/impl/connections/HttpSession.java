package com.beowulfe.hap.impl.connections;

import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beowulfe.hap.HomekitAccessory;
import com.beowulfe.hap.HomekitAuthInfo;
import com.beowulfe.hap.impl.HomekitRegistry;
import com.beowulfe.hap.impl.http.HomekitClientConnection;
import com.beowulfe.hap.impl.http.HttpRequest;
import com.beowulfe.hap.impl.http.HttpResponse;
import com.beowulfe.hap.impl.jmdns.JmdnsHomekitAdvertiser;
import com.beowulfe.hap.impl.json.AccessoryController;
import com.beowulfe.hap.impl.json.CharacteristicsController;
import com.beowulfe.hap.impl.pairing.PairVerificationManager;
import com.beowulfe.hap.impl.pairing.PairingManager;
import com.beowulfe.hap.impl.pairing.PairingUpdateController;
import com.beowulfe.hap.impl.responses.InternalServerErrorResponse;
import com.beowulfe.hap.impl.responses.NotFoundResponse;
import com.beowulfe.hap.impl.pairing.PairingListener;

class HttpSession {
	
	private volatile PairingManager pairingManager;
	private volatile PairVerificationManager pairVerificationManager;
	private volatile AccessoryController accessoryController;
	private volatile CharacteristicsController characteristicsController;

	private final HomekitAuthInfo authInfo;
	private final HomekitRegistry registry;
	private final SubscriptionManager subscriptions;
	private final HomekitClientConnection connection;
	private final PairingListener pairingListener;
	
	private final static Logger logger = LoggerFactory.getLogger(HttpSession.class);
	
	public HttpSession(HomekitAuthInfo authInfo, HomekitRegistry registry, SubscriptionManager subscriptions,
			HomekitClientConnection connection, PairingListener pairingListener) {
		this.authInfo = authInfo;
		this.registry = registry;
		this.subscriptions = subscriptions;
		this.connection = connection;
		this.pairingListener = pairingListener;
	}

	public HttpResponse handleRequest(HttpRequest request) throws IOException {
		switch(request.getUri()) {
		case "/pair-setup":
			return handlePairSetup(request);
			
		case "/pair-verify":
			return handlePairVerify(request);
			
		default:
			if (registry.isAllowUnauthenticatedRequests()) {
				return handleAuthenticatedRequest(request);
			} else {
				logger.info("Unrecognized request for "+request.getUri());
				return new NotFoundResponse();
			}
		}
	}
	
	public HttpResponse handleAuthenticatedRequest(HttpRequest request) throws IOException {
		try {
			switch(request.getUri()) {
			case "/accessories":
				return getAccessoryController().listing();
				
			case "/characteristics":
				switch(request.getMethod()) {
				case PUT:
					return getCharacteristicsController().put(request, connection);
					
				default:
					logger.info("Unrecognized method for "+request.getUri());
					return new NotFoundResponse();
				}
				
			case "/pairings":
				return new PairingUpdateController(authInfo, pairingListener).handle(request);
				
			default:
				if (request.getUri().startsWith("/characteristics?")) {
					return getCharacteristicsController().get(request);
				}
				logger.info("Unrecognized request for "+request.getUri());
				return new NotFoundResponse();
			}
		} catch (Exception e) {
			logger.error("Could not handle request", e);
			return new InternalServerErrorResponse(e);
		}
	}
		
	private HttpResponse handlePairSetup(HttpRequest request) {
		if (pairingManager == null) {
			synchronized(HttpSession.class) {
				if (pairingManager == null) {
					pairingManager = new PairingManager(authInfo, registry, pairingListener);
				}
			}
		}
		try {
			return pairingManager.handle(request);
		} catch (Exception e) {
			logger.error("Exception encountered during pairing", e);
			return new InternalServerErrorResponse(e);
		}
	}
	
	private HttpResponse handlePairVerify(HttpRequest request) {
		if (pairVerificationManager == null) {
			synchronized(HttpSession.class) {
				if (pairVerificationManager == null) {
					pairVerificationManager = new PairVerificationManager(authInfo, registry);
				}
			}
		}
		try {
			return pairVerificationManager.handle(request);
		} catch (Exception e) {
			logger.error("Excepton encountered while verifying pairing", e);
			return new InternalServerErrorResponse(e);
		}
	}
	
	private synchronized AccessoryController getAccessoryController() {		
		if (accessoryController == null) {
			accessoryController = new AccessoryController(registry);
		}
		return accessoryController;
	}
	
	private synchronized CharacteristicsController getCharacteristicsController() {
		if (characteristicsController == null) {
			characteristicsController = new CharacteristicsController(registry, subscriptions);
		}
		return characteristicsController;
	}
	
	public static class SessionKey {
		private final InetAddress address;
		private final HomekitAccessory accessory;
		
		public SessionKey(InetAddress address, HomekitAccessory accessory) {
			this.address = address;
			this.accessory = accessory;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof SessionKey) {
				return address.equals(((SessionKey) obj).address) && 
						accessory.equals(((SessionKey) obj).accessory);
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			int hash = 1;
		    hash = hash * 31 + address.hashCode();
		    hash = hash * 31 + accessory.hashCode();
		    return hash;
		}
	}

}
