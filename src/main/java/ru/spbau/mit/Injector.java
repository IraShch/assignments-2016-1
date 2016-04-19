package ru.spbau.mit;


import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Injector {
    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        Map<String, Object> initializedItems = new HashMap<>();
        return helper(rootClassName, implementationClassNames, initializedItems);
    }

    private static Object helper(String rootClassName, List<String> implementationClassNames,
                                 Map<String, Object> initializedItems) throws Exception {
        if (initializedItems.containsKey(rootClassName)) {
            Object item = initializedItems.get(rootClassName);
            if (item == null) {
                throw new InjectionCycleException();
            }
            return item;
        }
        initializedItems.put(rootClassName, null);

        Class cls = Class.forName(rootClassName);
        Constructor[] c = cls.getConstructors();
        Constructor rootConstructor = c[0];

        Class[] pTypes = rootConstructor.getParameterTypes();
        if (pTypes.length == 0) {
            Object item = cls.newInstance();
            initializedItems.put(rootClassName, item);
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
            }
            if (type.isAssignableFrom(cls)) {
                implFoundCounter += 1;
                implementation = rootClassName;
            }
            if (implFoundCounter == 0) {
                throw new ImplementationNotFoundException();
            }
            if (implFoundCounter > 1) {
                throw new AmbiguousImplementationException();
            }
            pInst[i] = helper(implementation, implementationClassNames, initializedItems);
        }

        Object res = rootConstructor.newInstance(pInst);
        initializedItems.put(rootClassName, res);
        return res;
    }

}
