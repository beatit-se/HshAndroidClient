package se.beatit.homesweethome.io.restclient;

import java.util.Collections;
import java.util.List;

/**
 * Created by stefan on 2015-02-19.
 */
public class History {

    private List<Long> electricityuse = Collections.EMPTY_LIST;

    public List<Long> getElectricityuse() {
        return electricityuse;
    }

    public void setElectricityuse(List<Long> electricityuse) {
        this.electricityuse = electricityuse;
    }

}
