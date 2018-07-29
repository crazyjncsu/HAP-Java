package com.beowulfe.hap.impl.pairing;

import java.io.IOException;

public interface PairingListener {
    void onPairingChanged() throws IOException;
}