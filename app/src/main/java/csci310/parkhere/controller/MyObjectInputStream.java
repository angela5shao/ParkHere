package csci310.parkhere.controller;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by ivylinlaw on 10/29/16.
 */
public class MyObjectInputStream extends ObjectInputStream implements Serializable {
    MyObjectInputStream(InputStream input) throws java.io.IOException
    {
        super(input);
    }
}
