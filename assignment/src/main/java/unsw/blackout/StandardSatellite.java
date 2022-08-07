package unsw.blackout;

import unsw.utils.Angle;
import java.util.Iterator;
import java.util.Map;

public class StandardSatellite extends Satellite {
    private final int speed = 2500;
    private final String[] support = {"HandheldDevice", "LaptopDevice"};
    private final int sendBandwidth = 1;
    private final int receiveBandwidth = 1;

    private int fileLimit = 3;
    private int byteLimit = 80;

    StandardSatellite(String id, String type, double height, Angle position) {
        super(id, type, height, position, 150000);
    }

    @Override
    public boolean canAddFile(int numBytes) {
        if (getFilesStored() + 1 > fileLimit) {
            return false;
        } else if (getBytesStored() + numBytes > byteLimit) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean hasSendBandwidth() {
        return (getTransferSendProgress().size() + 1 <= sendBandwidth);
    }

    @Override
    public boolean hasReceiveBandwidth() {
        return (getTransferReceiveProgress().size() + 1 <= receiveBandwidth);
    }

    @Override
    public void simulateM() {
        double velocity = speed / getHeight();
        Angle change = Angle.fromRadians(getPosition().toRadians() - velocity);

        if (change.toDegrees() < 0) {
            while (change.toDegrees() < 0) change = Angle.fromDegrees(change.toDegrees() + 360);
            change = Angle.fromDegrees(change.toDegrees() % 360);
        } else if (change.toDegrees() >= 360) {
            while (change.toDegrees() >= 360) change = Angle.fromDegrees(change.toDegrees() - 360);
            change = Angle.fromDegrees(change.toDegrees() % 360);
        }
        super.setPosition(change);
        
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
    public String[] getSupport() {
        return support;
    }

    @Override
    public int[] getBandwidth() {
        int[] bandwidths = {receiveBandwidth, sendBandwidth};
        return bandwidths;
    }
}
