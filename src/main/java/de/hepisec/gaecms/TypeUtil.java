package de.hepisec.gaecms;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class to convert request parameters to primitive types
 * 
 * @author Hendrik Pilz
 */
public class TypeUtil {
    /**
     * Parse value as Integer and return the int value or the defaultValue on error
     * 
     * @param value
     * @param defaultValue
     * @return the int value or the defaultValue on error
     */
    public static int parseInt(String value, int defaultValue) {
        int returnValue = defaultValue;
        
        if (value == null) {
            return returnValue;
        }
        
        try {
            returnValue = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            Logger.getLogger(TypeUtil.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        
        return returnValue;
    }
    
    /**
     * Parse value as Long and return the long value or the defaultValue on error
     * 
     * @param value
     * @param defaultValue
     * @return the long value or the defaultValue on error
     */
    public static long parseLong(String value, long defaultValue) {
        long returnValue = defaultValue;
        
        if (value == null) {
            return returnValue;
        }        
        
        try {
            returnValue = Long.parseLong(value);
        } catch (NumberFormatException ex) {
            Logger.getLogger(TypeUtil.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        
        return returnValue;
    }  
    
    /**
     * Parse value as Double and return the double value or the defaultValue on error
     * 
     * @param value
     * @param defaultValue
     * @return the double value or the defaultValue on error
     */
    public static double parseDouble(String value, double defaultValue) {
        double returnValue = defaultValue;
        
        if (value == null) {
            return returnValue;
        }        
        
        try {
            returnValue = Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            Logger.getLogger(TypeUtil.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        
        return returnValue;
    }      
}
