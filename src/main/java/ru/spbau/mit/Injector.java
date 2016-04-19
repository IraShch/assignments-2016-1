package ru.spbau.mit;


import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
//import  java.lang.*;
import java.util.Map;


public final class Injector {
    private static final Map<String, Object> INITIALIZED_ITEMS = new HashMap<>();

    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        if (INITIALIZED_ITEMS.containsKey(rootClassName)) {
            Object item = INITIALIZED_ITEMS.get(rootClassName);
            if (item == null) {
                throw new InjectionCycleException();
            }
            return item;
        }
        INITIALIZED_ITEMS.put(rootClassName, null);

        Class cls = Class.forName(rootClassName);
        Constructor[] c = cls.getConstructors();
        Constructor rootConstructor = c[0];

        Class[] pTypes = rootConstructor.getParameterTypes();
        if (pTypes.length == 0) {
            Object item = cls.newInstance();
            INITIALIZED_ITEMS.put(rootClassName, item);
            return item;
        }

        Object[] pInst = new Object[pTypes.length];
        for (int i = 0; i < pTypes.length; i++) {
            Class type = pTypes[i];
            int implFoundCounter = 0;
            String implementation = null;
            for (String putativeImplName : implementationClassNames) {
                Class putativeImpl = Class.forName(putativeImplName);
                if (type.isAssignableFrom(putativeImpl)) {
                    implFoundCounter += 1;
                    implementation = putativeImplName;
                }
                if (implFoundCounter == 0) {
                    throw new ImplementationNotFoundException();
                }
                if (implFoundCounter > 1) {
                    throw new AmbiguousImplementationException();
                }
            }
            pInst[i] = initialize(implementation, implementationClassNames);
        }

        Object res = rootConstructor.newInstance(pInst);
        INITIALIZED_ITEMS.put(rootClassName, res);
        return res;
    }
}
