package org.dragon.core.location;

import java.util.Date;
import java.util.List;
import java.util.Observable;

import org.dragon.rmm.RmmSettings;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * 增强功能的LocationListener，该监听器还具备观察者通知的功能，可以做到实时地理位置通知。 通过优良的算法，节约了频繁获取地理位置电力的消耗和精准度的选择。
 * 
 * @author dengjie
 * @since 1.0
 */
public class BestLocationListener extends Observable implements LocationListener {

    /**
     * location update minimize time
     */
    public static final long LOCATION_UPDATE_MIN_TIME = 0;
    /**
     * location update minimize distance
     */
    public static final long LOCATION_UPDATE_MIN_DISTANCE = 0;
    /**
     * slow location update min time
     */
    public static final long SLOW_LOCATION_UPDATE_MIN_TIME = 1000 * 60 * 5;
    /**
     * slow location update minimize distance
     */
    public static final long SLOW_LOCATION_UPDATE_MIN_DISTANCE = 50;
    /**
     * requested first search accuracy in meters
     */
    public static final float REQUESTED_FIRST_SEARCH_ACCURACY_IN_METERS = 100.0f;
    /**
     * requested first search max delta threshold
     */
    public static final int REQUESTED_FIRST_SEARCH_MAX_DELTA_THRESHOLD = 1000 * 60 * 5;
    /**
     * location update maximize delta threshold
     */
    public static final long LOCATION_UPDATE_MAX_DELTA_THRESHOLD = 1000 * 60 * 5;

    private static final String TAG = "BestLocationListener";
    private static final boolean DEBUG = RmmSettings.LOCATION_DEBUG;

    private Location mLastLocation;

    /**
     * constructor
     */
    public BestLocationListener() {
        super();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (DEBUG) {
            Log.d(TAG, "onLocationChanged: " + location);
        }
        updateLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // do nothing.
    }

    @Override
    public void onProviderEnabled(String provider) {
        // do nothing.
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // do nothing.
    }

    /**
     * on best location changed
     * 
     * @param location
     *            location
     */
    public synchronized void onBestLocationChanged(Location location) {
        if (DEBUG) {
            Log.d(TAG, "onBestLocationChanged: " + location);
        }
        mLastLocation = location;
        setChanged();
        notifyObservers(location);
    }

    /**
     * 同步获取最新地理位置信息
     * 
     * @return 最新地理位置信息
     */
    public synchronized Location getLastKnownLocation() {
        return mLastLocation;
    }

    /**
     * 同步清空最新地理位置信息
     */
    public synchronized void clearLastKnownLocation() {
        mLastLocation = null;
    }

    /**
     * 根据算法选择是否更新传入的地理位置，这个是内部算法，外部无须调用
     * 
     * @param location
     *            地理位置信息
     */
    public void updateLocation(Location location) {
        if (DEBUG) {
            Log.d(TAG, "updateLocation: Old: " + mLastLocation);
            Log.d(TAG, "updateLocation: New: " + location);
        }

        // Cases where we only have one or the other.
        if (location != null && mLastLocation == null) {
            if (DEBUG) {
                Log.d(TAG, "updateLocation: Null last location");
            }
            onBestLocationChanged(location);
            return;

        } else if (location == null) {
            if (DEBUG) {
                Log.d(TAG, "updated location is null, doing nothing");
            }
            return;
        }

        long now = new Date().getTime();
        long locationUpdateDelta = now - location.getTime();
        long lastLocationUpdateDelta = now - mLastLocation.getTime();
        boolean locationIsInTimeThreshold = locationUpdateDelta <= LOCATION_UPDATE_MAX_DELTA_THRESHOLD;
        boolean lastLocationIsInTimeThreshold = lastLocationUpdateDelta <= LOCATION_UPDATE_MAX_DELTA_THRESHOLD;
        boolean locationIsMostRecent = locationUpdateDelta <= lastLocationUpdateDelta;

        boolean accuracyComparable = location.hasAccuracy() || mLastLocation.hasAccuracy();
        boolean locationIsMostAccurate = false;
        if (accuracyComparable) {
            // If we have only one side of the accuracy, that one is more
            // accurate.
            if (location.hasAccuracy() && !mLastLocation.hasAccuracy()) {
                locationIsMostAccurate = true;
            } else if (!location.hasAccuracy() && mLastLocation.hasAccuracy()) {
                locationIsMostAccurate = false;
            } else {
                // If we have both accuracies, do a real comparison.
                locationIsMostAccurate = location.getAccuracy() <= mLastLocation.getAccuracy();
            }
        }

        if (DEBUG) {
            Log.d(TAG, "locationIsMostRecent:\t\t\t" + locationIsMostRecent);
            Log.d(TAG, "locationUpdateDelta:\t\t\t" + locationUpdateDelta);
            Log.d(TAG, "lastLocationUpdateDelta:\t\t" + lastLocationUpdateDelta);
            Log.d(TAG, "locationIsInTimeThreshold:\t\t" + locationIsInTimeThreshold);
            Log.d(TAG, "lastLocationIsInTimeThreshold:\t" + lastLocationIsInTimeThreshold);

            Log.d(TAG, "accuracyComparable:\t\t\t" + accuracyComparable);
            Log.d(TAG, "locationIsMostAccurate:\t\t" + locationIsMostAccurate);
        }

        // Update location if its more accurate and w/in time threshold or if
        // the old location is
        // too old and this update is newer.
        if (accuracyComparable && locationIsMostAccurate && locationIsInTimeThreshold) {
            onBestLocationChanged(location);
        } else if (locationIsInTimeThreshold && !lastLocationIsInTimeThreshold) {
            onBestLocationChanged(location);
        }
    }

    /**
     * is accurate enough
     * 
     * @param location
     *            location
     * @return boolean
     */
    public boolean isAccurateEnough(Location location) {
        if (location != null && location.hasAccuracy()
                && location.getAccuracy() <= REQUESTED_FIRST_SEARCH_ACCURACY_IN_METERS) {
            long locationUpdateDelta = new Date().getTime() - location.getTime();
            if (locationUpdateDelta < REQUESTED_FIRST_SEARCH_MAX_DELTA_THRESHOLD) {
                if (DEBUG) {
                    Log.d(TAG, "Location is accurate: " + location.toString());
                }
                return true;
            }
        }
        if (DEBUG) {
            Log.d(TAG, "Location is not accurate: " + location);
        }
        return false;
    }

    /**
     * 注册监听器
     * 
     * @param locationManager
     *            MierApplication传入的locationManager
     * @param gps
     *            是否使用gps定位
     */
    public void register(LocationManager locationManager, boolean gps) {
        if (DEBUG) {
            Log.d(TAG, "Registering this location listener: " + this.toString());
        }
        long updateMinTime = SLOW_LOCATION_UPDATE_MIN_TIME;
        long updateMinDistance = SLOW_LOCATION_UPDATE_MIN_DISTANCE;
        if (gps) {
            updateMinTime = LOCATION_UPDATE_MIN_TIME;
            updateMinDistance = LOCATION_UPDATE_MIN_DISTANCE;
        }
        List<String> providers = locationManager.getProviders(true);
        int providersCount = providers.size();
        for (int i = 0; i < providersCount; i++) {
            String providerName = providers.get(i);
            if (locationManager.isProviderEnabled(providerName)) {
                updateLocation(locationManager.getLastKnownLocation(providerName));
            }
            // Only register with GPS if we've explicitly allowed it.
            if (gps || !LocationManager.GPS_PROVIDER.equals(providerName)) {
                locationManager.requestLocationUpdates(providerName, updateMinTime, updateMinDistance, this);
            }
        }
    }

    /**
     * 注销监听器
     * 
     * @param locationManager
     *            MierApplication传入的locationManager
     */
    public void unregister(LocationManager locationManager) {
        if (DEBUG) {
            Log.d(TAG, "Unregistering this location listener: " + this.toString());
        }
        locationManager.removeUpdates(this);
    }

    /**
     * Updates the current location with the last known location without registering any location listeners.
     * 
     * @param locationManager
     *            the LocationManager instance from which to retrieve the latest known location
     */
    public synchronized void updateLastKnownLocation(LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        for (int i = 0, providersCount = providers.size(); i < providersCount; i++) {
            String providerName = providers.get(i);
            if (locationManager.isProviderEnabled(providerName)) {
                updateLocation(locationManager.getLastKnownLocation(providerName));
            }
        }
    }
}
