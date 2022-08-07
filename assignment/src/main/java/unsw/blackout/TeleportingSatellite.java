package unsw.blackout;

import unsw.utils.Angle;
import java.util.Iterator;
import java.util.Map;

public class TeleportingSatellite extends Satellite {
    private final int speed = 1000;
    private final String[] support = {"HandheldDevice", "LaptopDevice", "DesktopDevice"};
    private boolean clockwise;
    private final int sendBandwidth = 10;
    private final int receiveBandwidth = 15;
    private int fileLimit = Integer.MAX_VALUE;
    private int byteLimit = 200;

    TeleportingSatellite(String id, String type, double height, Angle position) {
        super(id, type, height, position, 200000);
        this.clockwise = false;
    }

    @Override
    public boolean canAddFile(int numBytes) {
        if ((getFilesStored() + 1 > fileLimit) || (getBytesStored() + numBytes > byteLimit)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void simulateM() {
        double velocity = speed / getHeight();
        Angle change;

        if (this.clockwise) {
            change = Angle.fromRadians(getPosition().toRadians() - velocity);
        } else {
            change = Angle.fromRadians(getPosition().toRadians() + velocity);
        }

        if (change.toDegrees() > 180 && getPosition().toDegrees() < 180) {
            teleportingFileTransfer();
            change = new Angle();
            this.clockwise ^= true;
        } else if (change.toDegrees() < 180 && getPosition().toDegrees() > 180) {
            teleportingFileTransfer();
            change = new Angle();
            this.clockwise ^= true;
        }  else if (change.toDegrees() < 0){
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
                getTransferSendProgress().remove(item.getKey());
            }
        }
    }

    @Override
    public String[] getSupport() {
        return support;
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
    public int[] getBandwidth() {
        int[] bandwidths = {receiveBandwidth, sendBandwidth};
        return bandwidths;
    }

    /**
     * Deals with when the satellite teleports while transferring a file
     * @return boolean
     */
    public boolean teleportingFileTransfer() {
        // Teleporting during file transfer causes some side effects
        Iterator<Map.Entry<String, String>> iteratorReceive = getTransferReceiveProgress().entrySet().iterator();
        while (iteratorReceive.hasNext()) {
            Map.Entry<String, String> item = iteratorReceive.next();
            TechEntity[] transfer = getTransferEntities(item.getKey());
            // Removes file from the receiver and removes all T bytes from the sender's file
            if (transfer[0] instanceof Device) {
                removeFiles(item.getKey());
                transfer[0].removeTBytes(item.getKey());
                transfer[0].getFiles().get(item.getKey()).setFileLength();
            } else {
                String content = getTransferSendProgress().get(item.getKey()).replace("t", "");
                getTransferReceiveProgress().get(item.getKey()).replace("t", "");
                getFiles().get(item.getKey()).appendContent(content);
                getFiles().get(item.getKey()).setFileLength();
            }
        }

        Iterator<Map.Entry<String, String>> iteratorSend = getTransferSendProgress().entrySet().iterator();
        while (iteratorSend.hasNext()) {
            Map.Entry<String, String> item = iteratorSend.next();
            TechEntity[] transfer = getTransferEntities(item.getKey());
            //  Removes all T bytes and completes transfer
            String content = transfer[0].getTransferSendProgress().get(item.getKey()).replace("t", "");
            transfer[1].getFiles().get(item.getKey()).appendContent(content);
            transfer[1].getFiles().get(item.getKey()).setFileLength();
            transfer[1].getFiles().get(item.getKey()).setTransferStatus(true);
        }
        return true;
    }
}
