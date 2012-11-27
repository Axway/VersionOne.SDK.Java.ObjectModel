/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.ArrayList;
import java.util.Iterator;

public class TransformIterable<S, D> implements Iterable<D> {

    protected final Iterable<S> wrapped;
    protected final ITransformerGeneric<S, D> xform;
    protected final Class<D> clazz;

    public TransformIterable(Iterable<S> wrapped,
                             ITransformerGeneric<S, D> xform, Class<D> clazz) {
        this.wrapped = wrapped;
        this.xform = xform;
        this.clazz = clazz;
    }

    public Iterator<D> iterator() {
        return new TransformIterator();
    }

    /**
     * Convert array from D type to type which set by {@code dClass} parameter.
     *
     * @return array with type {@code dClass}.
     */
    public Object[] toArray() {
        ArrayList<D> l = new ArrayList<D>();

        for (S o : wrapped) {
            l.add(xform.transform(o, clazz));
        }
        return l.toArray();
    }

    ////////////////////////////////// SubClasses //////////////////////////////

    public interface ITransformerGeneric<S, T> {
        T transform(S s, Class<T> dClass);
    }

    public interface ITransformer<S, T> {
        T transform(S input);
    }

    class TransformIterator implements Iterator<D> {
        private final Iterator<S> wrappedIterator;

        public TransformIterator() {
            this.wrappedIterator = TransformIterable.this.wrapped.iterator();
        }

        public boolean hasNext() {
            return this.wrappedIterator.hasNext();
        }

        public D next() {
            return xform.transform(this.wrappedIterator.next(), clazz);
        }

        public void remove() {
            this.wrappedIterator.remove();
        }
    }
}
