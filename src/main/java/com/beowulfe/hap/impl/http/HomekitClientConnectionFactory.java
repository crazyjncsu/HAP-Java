package com.beowulfe.hap.impl.http;

import java8.util.function.Consumer;

public interface HomekitClientConnectionFactory {

	HomekitClientConnection createConnection(Consumer<HttpResponse> outOfBandMessageCallback);
	
}
