package csci310.parkhere.resource;

import java.util.ArrayList;

/**
 * Created by ivylinlaw on 10/31/16.
 */
public class TwoEntryQueue<K> extends ArrayList<K> {
    // TimeInterval(Time start, Time end)
    // Time (int year, int month, int dayOfMonth, int hourOfDay, int minute, int second)

    private int maxSize = 2;

    public TwoEntryQueue() { }

    public boolean add(K k){
        boolean r = super.add(k);
        if (size() > maxSize){
            removeRange(0, size() - maxSize - 1);
        }
        return r;
    }

    public K getSecond() {
        if(size() > 1) return get(size() - 1);
        return null;
    }

    public K getFirst() {
        if(size() > 0) return get(0);
        return null;
    }
}