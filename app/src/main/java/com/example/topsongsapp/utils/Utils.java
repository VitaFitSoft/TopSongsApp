package com.example.topsongsapp.utils;


import java.util.Collection;
import java.util.Map;

public class Utils {
    public static boolean isNotMissing(String s){
        return s != null && s.length() > 0;
    }

    /**
     * This method returns true if the collection is null or is empty.
     * @param collection collections list parameter
     * @return true | false
     */
    public static boolean isEmpty( Collection<?> collection ){
        return collection == null || collection.isEmpty();
    }

    /**
     * This method returns true of the map is null or is empty.
     * @param map map input parameter
     * @return true | false
     */
    public static boolean isEmpty( Map<?, ?> map ){
        return map == null || map.isEmpty();
    }

    /**
     * This method returns true if the objet is null.
     * @param object input object parameter
     * @return true | false
     */
    public boolean isEmpty( Object object ){
        return object == null;
    }

    /**
     * This method returns true if the input array is null or its length is zero.
     * @param array input object array parameter
     * @return true | false
     */
    public boolean isEmpty( Object[] array ){
        return array == null || array.length == 0;
    }

    /**
     * This method returns true if the input string is null or its length is zero.
     * @param string string input parameter
     * @return true | false
     */
    public boolean isEmpty( String string ){
        if(string == null || string.trim().length() == 0 ){
          return true;
        }
        return false;
    }
}