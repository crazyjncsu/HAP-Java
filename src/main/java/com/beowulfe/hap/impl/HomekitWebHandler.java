package com.beowulfe.hap.impl;

import java8.util.concurrent.CompletableFuture;

import com.beowulfe.hap.impl.http.HomekitClientConnectionFactory;


public interface HomekitWebHandler {

	CompletableFuture<Integer> start(HomekitClientConnectionFactory clientConnectionFactory);
	
	void stop();

	void resetConnections();
	
}
