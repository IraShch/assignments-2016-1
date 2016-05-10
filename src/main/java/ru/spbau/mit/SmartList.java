package ru.spbau.mit;

import java.util.*;

public class SmartList<E> extends AbstractList<E> implements List<E> {
    private static final int ARRAY_SIZE = 5;

    private int size = 0;
    private Object data = null;

    public SmartList() {
    }

    public SmartList(Collection<E> c) {
        size = c.size();
        if (size == 1) {
            data = c.iterator().next();
        } else if (size <= ARRAY_SIZE && size != 0) {
            Object[] array = new Object[ARRAY_SIZE];
            int i = 0;
            for (E e : c) {
                array[i] = e;
                ++i;
            }
            data = array;
        } else if (size != 0) {
            data = new ArrayList<>(c);
        }
    }

    @Override
    public boolean add(E e) {
        if (size == 0) {
            data = e;
        } else if (size == 1) {
            Object[] array = new Object[ARRAY_SIZE];
            array[0] = data;
            array[1] = e;
            data = array;
        } else if (size < ARRAY_SIZE) {
        ((Object[]) data)[size] = e;
        } else if (size == ARRAY_SIZE) {
            ArrayList<Object> list = new ArrayList<>();
            for (Object i : (Object[]) data) {
                list.add(i);
            }
            list.add(e);
            data = list;
        } else {
            ((List<E>) data).add(e);
        }
        size += 1;
        return true;
    }

    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        if (size == 1) {
            return (E) data;
        }
        if (size <= ARRAY_SIZE) {
            return (E) ((Object[]) data)[index];
        }
        return ((List<E>) data).get(index);
    }

    @Override
    public E set(int index, E element) {
        E old = null;
        if (size == 1) {
            old = (E) data;
            data = element;
        } else if (size <= ARRAY_SIZE) {
            old = (E) ((Object[]) data)[index];
            ((Object[]) data)[index] = element;
        } else {
            old = ((List<E>) data).get(index);
            ((List<E>) data).set(index, element);
        }
        return old;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            this.add(e);
        }
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        E old = null;
        if (size == 1) {
            old = (E) data;
            data = null;
        } else if (size == 2) {
            old = get(index);
            if (index == 0) {
                data = get(1);
            } else {
                data = get(0);
            }
        } else if (size <= ARRAY_SIZE) {
            old = get(index);
            for (int i = index; i < size - 1; ++i) {
                ((Object[]) data)[index] = get(i + 1);
            }
            set(size - 1, null);
        } else if (size == ARRAY_SIZE + 1) {
            old = ((List<E>) data).remove(index);
            Object[] array = new Object[ARRAY_SIZE];
            int i = 0;
            for (E e : ((List<E>) data)) {
                array[i] = e;
                i += 1;
            }
            data = array;
        } else if (size > ARRAY_SIZE) {
            old = ((List<E>) data).remove(index);
        }
        size -= 1;
        return old;

    }
}
