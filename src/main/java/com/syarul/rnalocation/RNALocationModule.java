package com.syarul.rnalocation;

import android.location.Location;
import android.location.LocationManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RNALocationModule extends ReactContextBaseJavaModule{

    // React Class Name as called from JS
    public static final String REACT_CLASS = "RNALocation";
    // Unique Name for Log TAG
    public static final String TAG = RNALocationModule.class.getSimpleName();
    // Save last Location Provided
    private Location mLastLocation;

    //The React Native Context
    ReactApplicationContext mReactContext;


    // Constructor Method as called in Package
    public RNALocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        // Save Context for later use
        mReactContext = reactContext;

        LocationManager locationManager = (LocationManager) mReactContext.getSystemService(Context.LOCATION_SERVICE);
        mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

    }


    @Override
    public String getName() {
        return REACT_CLASS;
    }

    /*
     * Location Callback as called by JS
     */
    @ReactMethod
    public void getLocation() {
        if (mLastLocation != null) {
            try {
                double Longitude;
                double Latitude;

                // Receive Longitude / Latitude from (updated) Last Location
                Longitude = mLastLocation.getLongitude();
                Latitude = mLastLocation.getLatitude();

                Log.i(TAG, "Got new location. Lng: " +Longitude+" Lat: "+Latitude);

                // Create Map with Parameters to send to JS
                WritableMap params = Arguments.createMap();
                params.putDouble("Longitude", Longitude);
                params.putDouble("Latitude", Latitude);

                // Send Event to JS to update Location
                sendEvent(mReactContext, "updateLocation", params);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "Location services disconnected.");
            }
        }
    }

    /*
     * Internal function for communicating with JS
     */
    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        if (reactContext.hasActiveCatalystInstance()) {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        } else {
            Log.i(TAG, "Waiting for CatalystInstance...");
        }
    }
}
