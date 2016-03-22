package ru.spbau.mit;

import java.util.*;

class HashMultiset<E> extends AbstractSet<E> implements Multiset<E> {

    private LinkedHashMap<E, LinkedHashSet<E>> storage = new LinkedHashMap<>();
    private int size = 0;

    public HashMultiset() {
    }

    @Override
    public boolean add(E e) {
        if (storage.containsValue(e)) {
            storage.get(e).add(e);
            size++;
        } else {
            LinkedHashSet<E> elStorage = new LinkedHashSet<E>();
            elStorage.add(e);
            storage.put(e, elStorage);
            size++;
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int count(Object element) {
        if (storage.containsValue(element)) {
            return storage.get(element).size();

        }
        return 0;
    }

    @Override
    public boolean contains(Object o) {
        return storage.containsValue(o);
    }

    @Override
    public Set<E> elementSet() {
        return null;
    }

    @Override
    public Set<? extends Entry<E>> entrySet() {
        return null;
    }
}
