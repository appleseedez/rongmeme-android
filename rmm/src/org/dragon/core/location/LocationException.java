/**
 * Copyright 2009 Joe LaPenna
 */

package org.dragon.core.location;

/**
 * 地理位置异常类
 * 
 * @author dengjie
 * @since 1.0
 */
public class LocationException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * constructor
     */
    public LocationException() {
        super("Unable to determine your location.");
    }

    /**
     * constructor
     * @param message message
     */
    public LocationException(String message) {
        super(message);
    }

}
