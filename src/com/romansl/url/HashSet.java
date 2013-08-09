package com.romansl.url;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * A memory-efficient hash set.
 *
 * http://www.java2s.com/Code/Java/Collections-Data-Structure/Amemoryefficienthashset.htm
 *
 * @param <E> the element type
 */
class HashSet<E> extends AbstractSet<E> implements Serializable {
    private static final long serialVersionUID = -3029545013755451536L;

    /**
     * In the interest of memory-savings, we start with the smallest feasible
     * power-of-two table size that can hold three items without rehashing. If we
     * started with a size of 2, we'd have to expand as soon as the second item
     * was added.
     */
    private static final int INITIAL_TABLE_SIZE = 4;

    public static final Object NULL_ITEM = new Serializable() {
        private static final long serialVersionUID = 7917734518931172433L;

        Object readResolve() {
            return NULL_ITEM;
        }
    };

    private static Object maskNull(final Object o) {
        return o == null ? NULL_ITEM : o;
    }

    private static Object unmaskNull(final Object o) {
        return o == NULL_ITEM ? null : o;
    }

    /**
     * Number of objects in this set; transient due to custom serialization.
     * Default access to avoid synthetic accessors from inner classes.
     */
    private transient int size;

    /**
     * Backing store for all the objects; transient due to custom serialization.
     * Default access to avoid synthetic accessors from inner classes.
     */
    private transient Object[] table;

    public HashSet() {
        table = new Object[INITIAL_TABLE_SIZE];
    }

    public HashSet(final Collection<? extends E> c) {
        int newCapacity = INITIAL_TABLE_SIZE;
        final int expectedSize = c.size();
        while (newCapacity * 3 < expectedSize * 4) {
            newCapacity <<= 1;
        }

        table = new Object[newCapacity];
        super.addAll(c);
    }

    @Override
    public boolean add(final E e) {
        ensureSizeFor(size + 1);
        final int index = findOrEmpty(e);
        if (table[index] == null) {
            ++size;
            table[index] = maskNull(e);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    E get(final E e) {
        final int index = find(e);
        if (index < 0)
            return null;

        return (E) unmaskNull(table[index]);
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        ensureSizeFor(size + c.size());
        return super.addAll(c);
    }

    @Override
    public void clear() {
        table = new Object[INITIAL_TABLE_SIZE];
        size = 0;
    }

    @Override
    public boolean contains(final Object o) {
        return find(o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new RawArrayIterator<E>(table);
    }

    public <T> Iterator<T> iteratorType() {
        return new RawArrayIterator<T>(table);
    }

    @Override
    public boolean remove(final Object o) {
        final int index = find(o);
        if (index < 0)
            return false;
        internalRemove(index);
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        return toArray(new Object[size]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        int index = 0;
        for (final Object e : table) {
            if (e != null) {
                a[index++] = (T) unmaskNull(e);
            }
        }
        while (index < a.length) {
            a[index++] = null;
        }
        return a;
    }

    @SuppressWarnings("unchecked")
    protected void doReadObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        table = new Object[in.readInt()];
        final int items = in.readInt();
        for (int i = 0; i < items; i++) {
            add((E) in.readObject());
        }
    }

    protected void doWriteObject(final ObjectOutputStream out) throws IOException {
        out.writeInt(table.length);
        out.writeInt(size);
        for (final Object e : table) {
            if (e != null) {
                out.writeObject(unmaskNull(e));
            }
        }
    }

    /**
     * Returns whether two items are equal for the purposes of this set.
     */
    protected static boolean itemEquals(final Object a, final Object b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * Return the hashCode for an item.
     */
    protected static int itemHashCode(final Object o) {
        return o == null ? 0 : o.hashCode();
    }

    /**
     * Removes the item at the specified index, and performs internal management
     * to make sure we don't wind up with a hole in the table. Default access to
     * avoid synthetic accessors from inner classes.
     */
    public final void internalRemove(final int index) {
        table[index] = null;
        --size;
        plugHole(index);
    }

    /**
     * Ensures the set is large enough to contain the specified number of entries.
     */
    private void ensureSizeFor(final int expectedSize) {
        if (table.length * 3 >= expectedSize * 4)
            return;

        int newCapacity = table.length << 1;
        while (newCapacity * 3 < expectedSize * 4) {
            newCapacity <<= 1;
        }

        final Object[] oldTable = table;
        table = new Object[newCapacity];
        for (final Object o : oldTable) {
            if (o != null) {
                int newIndex = getIndex(unmaskNull(o));
                while (table[newIndex] != null) {
                    if (++newIndex == table.length) {
                        newIndex = 0;
                    }
                }
                table[newIndex] = o;
            }
        }
    }

    /**
     * Returns the index in the table at which a particular item resides, or -1 if
     * the item is not in the table.
     */
    private int find(final Object o) {
        int index = getIndex(o);
        while (true) {
            final Object existing = table[index];
            if (existing == null)
                return -1;
            if (itemEquals(o, unmaskNull(existing)))
                return index;
            if (++index == table.length) {
                index = 0;
            }
        }
    }

    /**
     * Returns the index in the table at which a particular item resides, or the
     * index of an empty slot in the table where this item should be inserted if
     * it is not already in the table.
     */
    private int findOrEmpty(final Object o) {
        int index = getIndex(o);
        while (true) {
            final Object existing = table[index];
            if (existing == null)
                return index;
            if (itemEquals(o, unmaskNull(existing)))
                return index;
            if (++index == table.length) {
                index = 0;
            }
        }
    }

    private int getIndex(final Object o) {
        int h = itemHashCode(o);
        // Copied from Apache's AbstractHashedMap; prevents power-of-two collisions.
        h += ~(h << 9);
        h ^= h >>> 14;
        h += h << 4;
        h ^= h >>> 10;
        // Power of two trick.
        return h & table.length - 1;
    }

    /**
     * Tricky, we left a hole in the map, which we have to fill. The only way to
     * do this is to search forwards through the map shuffling back values that
     * match this index until we hit a null.
     */
    private void plugHole(int hole) {
        int index = hole + 1;
        if (index == table.length) {
            index = 0;
        }
        while (table[index] != null) {
            final int targetIndex = getIndex(unmaskNull(table[index]));
            if (hole < index) {
                /*
                 * "Normal" case, the index is past the hole and the "bad range" is from
                 * hole (exclusive) to index (inclusive).
                 */
                if (!(hole < targetIndex && targetIndex <= index)) {
                    // Plug it!
                    table[hole] = table[index];
                    table[index] = null;
                    hole = index;
                }
            } else {
                /*
                 * "Wrapped" case, the index is before the hole (we've wrapped) and the
                 * "good range" is from index (exclusive) to hole (inclusive).
                 */
                if (index < targetIndex && targetIndex <= hole) {
                    // Plug it!
                    table[hole] = table[index];
                    table[index] = null;
                    hole = index;
                }
            }
            if (++index == table.length) {
                index = 0;
            }
        }
    }

    private void readObject(final ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        doReadObject(in);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        doWriteObject(out);
    }

    private static class RawArrayIterator<E> implements Iterator<E> {
        private final Object[] table;
        private int index;

        public RawArrayIterator(final Object[] src) {
            table = src;
            advanceToItem();
        }

        @Override
        public boolean hasNext() {
            return index < table.length;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            final E toReturn = (E) unmaskNull(table[index++]);
            advanceToItem();
            return toReturn;
        }

        @Override
        public void remove() {

        }

        private void advanceToItem() {
            for (; index < table.length; ++index) {
                if (table[index] != null)
                    return;
            }
        }
    }
}
