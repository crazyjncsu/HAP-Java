package com.beowulfe.hap.accessories;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import com.beowulfe.hap.HomekitAccessory;
import com.beowulfe.hap.HomekitCharacteristicChangeCallback;
import com.beowulfe.hap.Service;
import com.beowulfe.hap.accessories.properties.TemperatureUnit;
import com.beowulfe.hap.accessories.properties.ThermostatMode;
import com.beowulfe.hap.impl.services.ThermostatService;

/**
 * A thermostat with heating and cooling controls.
 *
 * @author Andy Lintner
 */
public interface Thermostat extends HomekitAccessory {

	/**
	 * Retrieves the current {@link ThermostatMode} of the thermostat.
	 * @return a future that will contain the mode.
	 */
	CompletableFuture<ThermostatMode> getCurrentMode();

	/**
	 * Subscribes to changes in the {@link ThermostatMode} of the thermostat.
	 * @param callback the function to call when the state changes.
	 */
	void subscribeCurrentMode(HomekitCharacteristicChangeCallback callback);

	/**
	 * Unsubscribes from changes in the mode of the thermostat.
	 */
	void unsubscribeCurrentMode();

	/**
	 * Sets the {@link ThermostatMode} of the thermostat.
	 * @param mode The {@link ThermostatMode} to set.
	 * @throws Exception when the change cannot be made.
	 */
	void setTargetMode(ThermostatMode mode) throws Exception;

	/**
	 * Retrieves the pending, but not yet complete, {@link ThermostatMode} of the thermostat.
	 * @return a future that will contain the target mode.
	 */
	CompletableFuture<ThermostatMode> getTargetMode();

	/**
	 * Subscribes to changes in the pending, but not yet complete, {@link ThermostatMode} of the thermostat.
	 * @param callback the function to call when the state changes.
	 */
	void subscribeTargetMode(HomekitCharacteristicChangeCallback callback);

	/**
	 * Unsubscribes from changes in the pending, but not yet complete, {@link ThermostatMode} of the thermostat.
	 */
	void unsubscribeTargetMode();

	/**
	 * Retrieves the temperature unit of the thermostat. The impact of this is unclear, as the actual temperature
	 * is always communicated in celsius degrees, and the iOS device uses the user's locale to determine
	 * the unit to convert to.
	 * @return the temperature unit of the thermostat.
	 */
	TemperatureUnit getTemperatureUnit();

	/**
	 * Retrieves the minimum temperature, in celsius degrees, the thermostat can be set to.
	 * @return the minimum temperature.
	 */
	double getMinimumTemperature();

	/**
	 * Retrieves the maximum temperature, in celsius degrees, the thermostat can be set to.
	 * @return the maximum temperature.
	 */
	double getMaximumTemperature();

	/**
	 * Retrieves the current temperature, in celsius degrees.
	 * @return a future that will contain the temperature.
	 */
	CompletableFuture<Double> getCurrentTemperature();
	
	/**
	 * Subscribes to changes in the current temperature.
	 * @param callback the function to call when the state changes.
	 */
	void subscribeCurrentTemperature(HomekitCharacteristicChangeCallback callback);

	/**
	 * Unsubscribes from changes in the current temperature.
	 */
	void unsubscribeCurrentTemperature();

	/**
	 * Retrieves the target temperature, in celsius degrees, used when the thermostat is in {@link ThermostatMode#AUTO} mode.
	 * @return a future that will contain the target temperature.
	 */
	CompletableFuture<Double> getTargetTemperature();

	/**
	 * Sets the target temperature when in {@link ThermostatMode#AUTO} mode.
	 * @param value the target temperature, in celsius degrees.
	 * @throws Exception when the temperature cannot be changed.
	 */
	void setTargetTemperature(Double value) throws Exception;
	
	/**
	 * Subscribes to changes in the target temperature.
	 * @param callback the function to call when the state changes.
	 */
	void subscribeTargetTemperature(HomekitCharacteristicChangeCallback callback);

	/**
	 * Unsubscribes from changes in the target temperature.
	 */
	void unsubscribeTargetTemperature();

	/**
	 * Retrieves the temperature below which the thermostat should begin heating. Used when the thermostat is in
	 * {@link ThermostatMode#HEAT} mode.
	 * @return a future that will contain the threshold temperature, in celsius degrees. 
	 */
	CompletableFuture<Double> getHeatingThresholdTemperature();

	/**
	 * Sets the temperature below which the thermostat should begin heating. Used when the thermostat is in
	 * {@link ThermostatMode#HEAT} mode.
	 * @param value the threshold temperature, in celsius degrees.
	 * @throws Exception when the threshold temperature cannot be changed.
	 */
	void setHeatingThresholdTemperature(Double value) throws Exception;
	
	/**
	 * Subscribes to changes in the heating threshold.
	 * @param callback the function to call when the state changes.
	 */
	void subscribeHeatingThresholdTemperature(HomekitCharacteristicChangeCallback callback);

	/**
	 * Unsubscribes from changes in the heating threshold.
	 */
	void unsubscribeHeatingThresholdTemperature();

	/**
	 * Retrieves the temperature above which the thermostat should begin cooling. Used when the thermostat is in
	 * {@link ThermostatMode#COOL} mode.
	 * @return a future that will contain the threshold temperature, in celsius degrees.
	 */
	CompletableFuture<Double> getCoolingThresholdTemperature();

	/**
	 * Sets the temperature above which the thermostat should begin cooling. Used when the thermostat is in
	 * {@link ThermostatMode#COOL} mode.
	 * @param value the threshold temperature, in celsius degrees.
	 * @throws Exception when the threshold temperature cannot be changed.
	 */
	void setCoolingThresholdTemperature(Double value) throws Exception;
	
	/**
	 * Subscribes to changes in the cooling threshold.
	 * @param callback the function to call when the state changes.
	 */
	void subscribeCoolingThresholdTemperature(HomekitCharacteristicChangeCallback callback);

	/**
	 * Unsubscribes from changes in the cooling threshold.
	 */
	void unsubscribeCoolingThresholdTemperature();
	
	@Override
	default public Collection<Service> getServices() {
		return Collections.singleton(new ThermostatService(this));
	}

}