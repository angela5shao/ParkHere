package csci310.parkhere.controller;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Created by ivylinlaw on 10/29/16.
 */
public class MyObjectOutputStream extends ObjectOutputStream implements Serializable {
    public MyObjectOutputStream(OutputStream output) throws java.io.IOException
    {
        super(output);
    }
}
