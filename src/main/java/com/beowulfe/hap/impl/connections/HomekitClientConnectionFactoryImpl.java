package com.beowulfe.hap.impl.connections;

import java8.util.function.Consumer;

import com.beowulfe.hap.HomekitAuthInfo;
import com.beowulfe.hap.impl.HomekitRegistry;
import com.beowulfe.hap.impl.http.HomekitClientConnection;
import com.beowulfe.hap.impl.http.HomekitClientConnectionFactory;
import com.beowulfe.hap.impl.http.HttpResponse;
import com.beowulfe.hap.impl.jmdns.JmdnsHomekitAdvertiser;
import com.beowulfe.hap.impl.pairing.PairingListener;

public class HomekitClientConnectionFactoryImpl implements HomekitClientConnectionFactory{

	private final HomekitAuthInfo authInfo;
	private final HomekitRegistry registry;
	private final SubscriptionManager subscriptions;
	private final PairingListener pairingListener;
	
	public HomekitClientConnectionFactoryImpl(HomekitAuthInfo authInfo,
			HomekitRegistry registry, SubscriptionManager subscriptions, PairingListener pairingListener) {
		this.registry = registry;
		this.authInfo = authInfo;
		this.subscriptions = subscriptions;
		this.pairingListener = pairingListener;
	}
	
	@Override
	public HomekitClientConnection createConnection(Consumer<HttpResponse> outOfBandMessageCallback) {
		return new ConnectionImpl(authInfo, registry, outOfBandMessageCallback, subscriptions, pairingListener);
	}

	
	
}
