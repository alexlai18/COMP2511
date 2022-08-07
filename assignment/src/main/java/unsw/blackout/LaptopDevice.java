package unsw.blackout;

import unsw.utils.Angle;
import java.util.Iterator;
import java.util.Map;

public class LaptopDevice extends Device {

    LaptopDevice(String id, String type, Angle position) {
        super(id, type, position, 100000);
    }

    @Override
    public void simulateM() {
        Iterator<Map.Entry<String, String>> iteratorReceive = getTransferReceiveProgress().entrySet().iterator();
        while (iteratorReceive.hasNext()) {
            Map.Entry<String, String> item = iteratorReceive.next();
            if (!updateReceiveTransfer(item.getKey())) {
                iteratorReceive.remove();
            }
        }

        Iterator<Map.Entry<String, String>> iteratorSend = getTransferSendProgress().entrySet().iterator();
        while (iteratorSend.hasNext()) {
            Map.Entry<String, String> item = iteratorSend.next();
            if (!updateSendTransfer(item.getKey())) {
                iteratorSend.remove();
            }
        }
    }

    @Override
    public boolean hasSendBandwidth() {
        return true;
    }

    @Override 
    public boolean hasReceiveBandwidth() {
        return true;
    }

    @Override
    public boolean canAddFile(int numBytes) {
        return true;
    }
}
