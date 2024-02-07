package evgenii.newjob.deepclone;


import evgenii.newjob.deepclone.exception.DeepCopyException;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Utility class to make deep copies of objects.
 * A deep copy of an Object is a reproduction of the original
 * object and its recursively referenced objects.
 */
public class CopyUtils {

    /**
     * Map for handling object references during deep copy process.
     * Helps in avoiding infinite recursion and duplicate copies of same object.
     */
    private static final Map<Object, Object> copiedObjects = new IdentityHashMap<>();

    /**
     * Method for performing a deep copy of an object.
     *
     * @param original The object to be deep copied.
     * @param <T>      The type of the object to be deep copied.
     * @return The deep copied object.
     */
    public static <T> T deepCopy(T original) {
        if (original == null) {
            return null;
        }
        return (T) copyObject(original);
    }

    /**
     * Inner method to handle the deep copy process.
     * Determines if object is a primitive, String, array, or Collection and
     * directs copying to relevant method. Recursive structures are handled
     * with a Map of already copied references.
     *
     * @param original The object to be deep copied.
     * @return The deep copied object.
     */
    private static Object copyObject(Object original) {
        if (original == null) {
            return null;
        }

        Class<?> clazz = original.getClass();

        // If the object is a primitive, String, or wrapper class, return the object
        if (clazz.isPrimitive() || original instanceof String || original instanceof Integer || original instanceof Double) {
            return original;
        }

        if (copiedObjects.containsKey(original)) {
            return copiedObjects.get(original);
        }

        if (clazz.isArray()) {
            return copyArray(original);
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            return copyCollection((Collection<?>) original);
        }

        if (original instanceof Map) {
            return copyMap((Map<?, ?>) original);
        }

        try {
            Object copy;
            if (!clazz.getDeclaredConstructors()[0].isAccessible()) {
                clazz.getDeclaredConstructors()[0].setAccessible(true);
            }
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            constructor.setAccessible(true);

            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameterValues = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                parameterValues[i] = getDefault(parameterTypes[i]);
            }

            copy = constructor.newInstance(parameterValues);
            copiedObjects.put(original, copy);

            // Copy fields
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object fieldValue = field.get(original);
                if (fieldValue != null) {
                    Object copiedValue = copyObject(fieldValue);
                    field.set(copy, copiedValue);
                }
            }
            return copy;
        } catch (Exception e) {
            throw new DeepCopyException("Error during object copy");
        }
    }

    /**
     * Provides default values for given types.
     *
     * @param type The class of type to provide a default value.
     * @return The default value object.
     */
    private static Object getDefault(Class<?> type) {
        if (type.isPrimitive()) {
            if (type.equals(boolean.class)) return false;
            if (type.equals(int.class)) return 0;
            if (type.equals(long.class)) return 0L;
            if (type.equals(double.class)) return 0.0d;
            if (type.equals(float.class)) return 0.0f;
            if (type.equals(char.class)) return '\u0000';
            if (type.equals(byte.class)) return (byte) 0;
            if (type.equals(short.class)) return (short) 0;
        } else if (type.equals(String.class)) {
            return "";
        } else if (Collection.class.isAssignableFrom(type)) {
            return Collections.emptyList();
        }
        return null;
    }

    /**
     * Handles copying for array types.
     *
     * @param original The array object to be deep copied.
     * @return The deep copied array.
     */
    private static Object copyArray(Object original) {
        Class<?> componentType = original.getClass().getComponentType();
        int length = Array.getLength(original);
        Object copy = Array.newInstance(componentType, length);
        copiedObjects.put(original, copy);
        for (int i = 0; i < length; i++) {
            Array.set(copy, i, copyObject(Array.get(original, i)));
        }
        return copy;
    }

    /**
     * Handles copying for java.util.Collection types.
     * Determines the appropriate sub-type of Collection and makes a new instance
     * before recursively copying elements.
     *
     * @param original The Collection object to be deep copied.
     * @return The deep copied Collection.
     */
    private static Collection<?> copyCollection(Collection<?> original) {
        Collection copy;
        if (original instanceof List) {
            copy = new ArrayList<>(original.size());
        } else if (original instanceof Set) {
            copy = new HashSet<>(Math.max((int) (original.size() / 0.75f) + 1, 16));
        } else if (original instanceof Queue) {
            copy = new LinkedList<>();
        } else {
            try {
                copy = original.getClass().getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Cannot create a copy of the collection of type " + original.getClass(), e);
            }
        }

        copiedObjects.put(original, copy);

        original.forEach(item -> copy.add(copyObject(item)));

        return copy;
    }

    /**
     * Handles copying for java.util.Map types.
     * Determines the appropriate sub-type of Map and makes a new instance
     * before recursively copying keys and values.
     *
     * @param original The Map object to be deep copied.
     * @return The deep copied Map.
     */
    private static Map<?, ?> copyMap(Map<?, ?> original) {
        Map<Object, Object> copy;
        try {
            // Attempt to create a new instance of the original map's runtime class
            copy = original.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create a copy of the map of type " + original.getClass(), e);
        }

        copiedObjects.put(original, copy);

        // Iterate through the original map, deep copying both keys and values
        for (Map.Entry<?, ?> entry : original.entrySet()) {
            Object keyCopy = copyObject(entry.getKey());
            Object valueCopy = copyObject(entry.getValue());
            copy.put(keyCopy, valueCopy);
        }

        return copy;
    }

}
