package simpledb;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        // some code goes here
        return this.tdItems.iterator();
        // return null;
    }

    private static final long serialVersionUID = 1L;
    // An List to store TDItem objects, where each TDItem contains the type and name of a field.
    private final List<TDItem> tdItems;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
        // initialize tdItems. A TDItem object is created for each field based on the provided arrays of types and field names.
        this.tdItems = new ArrayList<>(typeAr.length);
        initializeTdItems(typeAr, fieldAr);
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        // similar construction, but without fieldAr
        this.tdItems = new ArrayList<>(typeAr.length);
        initializeTdItems(typeAr, null);
    }

    // use to simplify initializing, avoiding code repeated
    private void initializeTdItems(Type[] typeAr, String[] fieldAr) {
        for (int i = 0; i < typeAr.length; i++) {
            String fieldName = (fieldAr != null && i < fieldAr.length) ? fieldAr[i] : "";
            this.tdItems.add(new TDItem(typeAr[i], fieldName));
        }
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return this.tdItems.size();
        // return 0;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here

        if (i > this.numFields() || i < 0) {
            throw new NoSuchElementException();
        }
        return this.tdItems.get(i).fieldName;
        // return null;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // some code goes here
        if (i > this.numFields() || i < 0) {
            throw new NoSuchElementException();
        }
        return this.tdItems.get(i).fieldType;
        // return null;
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        // some code goes here
        int index = 0;
        for (; index < this.numFields(); index++) {
            if (this.tdItems.get(index).fieldName.equals(name)) {
                return index;
            }
        }
        throw new NoSuchElementException();
        // return 0;
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        // Calculates and returns the total size of all fields in bytes using java8 stream
        return this.tdItems.stream()
                           .mapToInt(tdItem -> tdItem.fieldType.getLen())
                           .sum();
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
        Type[] typeAr = new Type[td1.numFields() + td2.numFields()];
        String[] fieldAr = new String[td1.numFields() + td2.numFields()];

        for(int i = 0;i < td1.numFields();i ++) {
            typeAr[i] = td1.getFieldType(i);
            fieldAr[i] = td1.getFieldName(i);
        }
        // put td2 behind td1 to merge
        for(int i = 0;i < td2.numFields();i ++) {
            typeAr[td1.numFields() + i] = td2.getFieldType(i);
            fieldAr[td1.numFields() + i] = td2.getFieldName(i);
        }

        return new TupleDesc(typeAr, fieldAr);
        // return null;
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        // some code goes here
        // using java8 character to simplify comparision logic
        if (this == o) return true;
        if (!(o instanceof TupleDesc)) return false;
        TupleDesc that = (TupleDesc) o;
        return this.numFields() == that.numFields() && 
               IntStream.range(0, this.numFields())
                        .allMatch(i -> this.getFieldType(i).equals(that.getFieldType(i)));
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        StringBuilder curString = new StringBuilder();
        for (int i = 0; i < this.numFields(); i++) {
            curString.append(this.getFieldType(i)).append("(").append(this.getFieldName(i)).append(")");
        }
        return curString.toString();
        // return "";
    }
}
