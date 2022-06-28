package org.vhfradio.model;

import java.util.List;
import java.util.Random;

public class MMSI {

    private String mmsi;

    public MMSI(List<String> mmsiList) {
        do {
            this.mmsi = getGeneratedMMSI();
        } while (mmsiList.contains(this.mmsi));
    }

    public String getMmsi() {
        return this.mmsi;
    }

    private String getGeneratedMMSI() {
        StringBuilder mmsi = new StringBuilder("261");
        Random rand = new Random();
        // 999999 - 100000 + 1
        int random = rand.nextInt(900000) + 100000;
        mmsi = mmsi.append(String.valueOf(random));

        return mmsi.toString();
    }
}
