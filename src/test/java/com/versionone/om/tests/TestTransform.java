package com.versionone.om.tests;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import com.versionone.DB;
import com.versionone.DB.DateTime;
import com.versionone.om.TransformIterable;
import com.versionone.om.TransformIterable.ITransformer;

public class TestTransform {

    public static class NoopTransformer<T> implements IReversibleTransformer<T, T> {
        public T transform(T input) {
            return input;
        }

        public ITransformer<T, T> getReverseTransformer() {
            return this;
        }
    }

    public interface IReversibleTransformer<S, T> extends ITransformer<S, T> {
        ITransformer<T, S> getReverseTransformer();
    }

    public static class UTCToLocal implements IReversibleTransformer {
        public static final IReversibleTransformer instance = new UTCToLocal();

        public Object transform(Object input) {
            if (input instanceof DB) {
                return input;
            }
            return ((Date) ((DateTime) input).getValue()).getTime();
        }

        public ITransformer getReverseTransformer() {
            return LocalToUTC.instance;
        }
    }

    public static class LocalToUTC implements IReversibleTransformer {
        public static final IReversibleTransformer instance = new LocalToUTC();

        public Object transform(Object input) {

            if (input instanceof DB) {
                return input;
            }

            return ((DateTime) input).getValue();
        }

        public ITransformer getReverseTransformer() {
            return UTCToLocal.instance;
        }
    }

    public static class TransformCollection<S, D> extends TransformIterable<S, D>
            implements Collection<D> {
        protected static final String READ_ONLY = "Cannot modify a read-only list";

        public TransformCollection(Collection<S> wrapped,
                                   ITransformerGeneric<S, D> xform, Class<D> clazz) {
            super(wrapped, xform, clazz);
        }

        protected Collection<S> getWrapped() {
            return (Collection<S>) wrapped;
        }

        public int size() {
            return getWrapped().size();
        }

        public boolean add(Object o) {
            throw new UnsupportedOperationException(READ_ONLY);
        }

        public boolean addAll(Collection c) {
            throw new UnsupportedOperationException(READ_ONLY);
        }

        public void clear() {
            throw new UnsupportedOperationException(READ_ONLY);
        }

        public boolean contains(Object o) {
            for (Object item : this) {
                if (item.equals(o)) {
                    return true;
                }
            }
            return false;
        }

        public boolean containsAll(Collection<?> c) {
            for (Object o : c) {
                if (!contains(o)) {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty() {
            return getWrapped().isEmpty();
        }

        public boolean remove(Object o) {
            throw new UnsupportedOperationException(READ_ONLY);
        }

        public boolean removeAll(Collection c) {
            throw new UnsupportedOperationException(READ_ONLY);
        }

        public boolean retainAll(Collection c) {
            throw new UnsupportedOperationException(READ_ONLY);
        }

        public void copyTo(Array array, int index) {
            for (Object item : this) {
                Array.set(array, index++, item);
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] array) {
            if (array.length < size())
                array = (T[]) java.lang.reflect.Array.
                        newInstance(array.getClass().getComponentType(), size());

            int i = 0;
            for (S o : getWrapped()) {
                array[i++] = (T) xform.transform(o, clazz);
            }
            if (array.length > size())
                array[size()] = null;
            return array;
        }
    }

    public static class TransformList<S, D>
            extends TransformCollection<S, D> implements List<D> {

        public TransformList(Collection<S> wrapped,
                             ITransformerGeneric<S, D> xform, Class<D> clazz) {
            super(wrapped, xform, clazz);
        }

        protected List<S> getWrapped() {
            return (List<S>) wrapped;
        }

        public boolean addAll(int index, Collection<? extends D> c) {
            throw new UnsupportedOperationException(READ_ONLY);
        }

        public D get(int index) {
            return xform.transform(getWrapped().get(index), clazz);
        }

        public D set(int index, D element) {
            throw new UnsupportedOperationException(READ_ONLY);
        }

        public void add(int index, D element) {
            throw new UnsupportedOperationException(READ_ONLY);
        }

        public D remove(int index) {
            throw new UnsupportedOperationException(READ_ONLY);
        }

        public int indexOf(Object o) {
            ListIterator<D> e = listIterator();
            if (o == null) {
                while (e.hasNext())
                    if (e.next() == null)
                        return e.previousIndex();
            } else {
                while (e.hasNext())
                    if (o.equals(e.next()))
                        return e.previousIndex();
            }
            return -1;
        }

        public int lastIndexOf(Object o) {
            ListIterator<D> e = listIterator(size());
            if (o == null) {
                while (e.hasPrevious())
                    if (e.previous() == null)
                        return e.nextIndex();
            } else {
                while (e.hasPrevious())
                    if (o.equals(e.previous()))
                        return e.nextIndex();
            }
            return -1;
        }

        public ListIterator<D> listIterator() {
            return listIterator(0);
        }

        public ListIterator<D> listIterator(int index) {
            if (index < 0 || index > size())
                throw new IndexOutOfBoundsException("Index: " + index);

            return new ListItr<S, D>(getWrapped().listIterator(index), xform, clazz);
        }

        public List<D> subList(int fromIndex, int toIndex) {
            return new TransformList<S, D>(
                    getWrapped().subList(fromIndex, toIndex), xform, clazz);
        }

        private class ListItr<S, D> implements ListIterator<D> {
            private ListIterator<S> wrappedItr;
            private final ITransformerGeneric<S, D> xform;
            private final Class<D> clazz;

            private ListItr(ListIterator<S> sourceItr,
                            ITransformerGeneric<S, D> xform, Class<D> clazz) {
                this.wrappedItr = sourceItr;
                this.xform = xform;
                this.clazz = clazz;
            }

            public boolean hasNext() {
                return wrappedItr.hasNext();
            }

            public D next() {
                final S s = wrappedItr.next();
                return xform.transform(s, clazz);
            }

            public boolean hasPrevious() {
                return wrappedItr.hasPrevious();
            }

            public D previous() {
                S s = wrappedItr.previous();
                return xform.transform(s, clazz);
            }

            public int nextIndex() {
                return wrappedItr.nextIndex();
            }

            public int previousIndex() {
                return wrappedItr.previousIndex();
            }

            public void remove() {
                throw new UnsupportedOperationException(READ_ONLY);
            }

            public void set(D o) {
                throw new UnsupportedOperationException(READ_ONLY);
            }

            public void add(D o) {
                throw new UnsupportedOperationException(READ_ONLY);
            }
        }
    }
}
