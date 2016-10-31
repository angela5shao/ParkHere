package csci310.parkhere.resource;

/**
 * Created by angela02pd2014 on 10/23/16.
 */

public enum CarType {
    Handicap(0x0001),
    Compact(0x0002),
    SUV(0x0004),
    Truck(0x0008),
    CoveredParking(0x0010),
    AnyCarType(0xFFFF);

    private int value;

    CarType(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

}
