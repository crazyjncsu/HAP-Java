package com.beowulfe.hap.impl.characteristics.windowcovering;

import java8.util.concurrent.CompletableFuture;

import com.beowulfe.hap.HomekitCharacteristicChangeCallback;
import com.beowulfe.hap.accessories.WindowCovering;
import com.beowulfe.hap.characteristics.EventableCharacteristic;
import com.beowulfe.hap.characteristics.IntegerCharacteristic;

public class CurrentPositionCharacteristic extends IntegerCharacteristic implements EventableCharacteristic {

	private final WindowCovering windowCovering;
	
	public CurrentPositionCharacteristic(WindowCovering windowCovering) {
		super("0000006D-0000-1000-8000-0026BB765291", false, true, "The current position", 0, 100, "%");
		this.windowCovering = windowCovering;
	}

	@Override
	protected void setValue(Integer value) throws Exception {
		//Read Only
	}

	@Override
	protected CompletableFuture<Integer> getValue() {
		return windowCovering.getCurrentPosition();
	}

	@Override
	public void subscribe(HomekitCharacteristicChangeCallback callback) {
		windowCovering.subscribeCurrentPosition(callback);
	}

	@Override
	public void unsubscribe() {
		windowCovering.unsubscribeCurrentPosition();
	}

}
