package unsw.blackout;

import unsw.utils.Angle;

import java.util.HashMap;
import java.util.Map;

public abstract class TechEntity {
    private String id;
    private String type;
    private double height;
    private Angle position;
    private double range;
    private Map<String, File> files;
    private Map<String, String> transferReceiveProgress;
    private Map<String, String> transferSendProgress;
    private Map<String, TechEntity[]> fileTransfer;

    // Constructor
    TechEntity (String id, String type, double height, Angle position, double range) {
        this.id = id;
        this.height = height;
        this.range = range;
        this.type = type;
        this.files = new HashMap<String, File>();
        this.transferReceiveProgress = new HashMap<String, String>();
        this.transferSendProgress = new HashMap<String, String>();
        if (position.toDegrees() < 0) {
            while (position.toDegrees() < 0) position = Angle.fromDegrees(position.toDegrees() + 360);
        } else if (position.toDegrees() >= 360) {
            while (position.toDegrees() >= 360) position = Angle.fromDegrees(position.toDegrees() - 360);
        }
        this.position = Angle.fromDegrees(position.toDegrees() % 360);
        this.fileTransfer = new HashMap<String, TechEntity[]>(); 
    }
    
    public abstract void simulateM();

    // Finds if an entity is able to be communicated with 
    abstract boolean communicable(TechEntity s, double range);

    public abstract boolean hasSendBandwidth();

    public abstract boolean hasReceiveBandwidth();

    public abstract boolean canAddFile(int numBytes);

    public abstract int[] getBandwidth();

    public double getRange() {
        return this.range;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public double getHeight() {
        return this.height;
    }

    public Angle getPosition() {
        return this.position;
    }

    public void setPosition(Angle change) {
        this.position = change;
    }

    public Map<String, File> getFiles() {
        return this.files;
    }

    public void addFiles(String filename, String content) {
        File fileInfo = new File(filename, content, content.length(), true);
        this.files.put(filename, fileInfo);
    }

    public void removeFiles(String key) {
        this.files.remove(key);
    }

    public void removeTBytes(String key) {
        File fileInfo = this.files.get(key);
        fileInfo.setContent(fileInfo.getContent().replace("t", ""));
    }

    public Map<String, TechEntity[]> getFileTransfer() {
        return this.fileTransfer;
    }

    public int getFilesStored() {
        return files.size();
    }

    public int getBytesStored() {
        int size = 0;
        for (File f : files.values()) {
            size += f.getFileLength();
        }
        return size;
    }

    public Map<String, String> getTransferReceiveProgress() {
        return this.transferReceiveProgress;
    }

    public Map<String, String> getTransferSendProgress() {
        return this.transferSendProgress;
    }

    public TechEntity[] getTransferEntities(String key) {
        return this.fileTransfer.get(key);
    }

    /**
     * This function adds an array of entities involved with the transfer of this file
     * @param fileName
     * @param content
     * @param from
     * @param to
     */
    public void addTransferReceive(String fileName, String content, TechEntity from, TechEntity to) {
        transferReceiveProgress.put(fileName, content);
        TechEntity[] filesInvolved = {from, to};
        fileTransfer.put(fileName, filesInvolved);
    }

    /**
     * This function adds an array of entities involved with the transfer of this file
     * @param fileName
     * @param content
     * @param from
     * @param to
     */
    public void addTransferSend(String fileName, String content, TechEntity from, TechEntity to) {
        TechEntity[] filesInvolved = {from, to};
        fileTransfer.put(fileName, filesInvolved);
        transferSendProgress.put(fileName, content);
    }

    /**
     * Function that updates the receive transfer attribute for simulate.
     * It either updates the file content being transferred or is removed (if completed)
     * It also removes the file from the receiving entity if entities go out of range
     * 
     * @param fileName
     * @return boolean
     */
    public boolean updateReceiveTransfer(String fileName) {
        if (this.files.get(fileName) == null || this.files.get(fileName).getTransferStatus()) {
            return false;
        }

        int rate = findRate(fileName, true);
        
        if (rate > this.transferReceiveProgress.get(fileName).length()) {
            rate = this.transferReceiveProgress.get(fileName).length();
        }

        this.files.get(fileName).appendContent(this.transferReceiveProgress.get(fileName).substring(0, rate));
        this.transferReceiveProgress.put(fileName, this.transferReceiveProgress.get(fileName).substring(rate));

        if (this.transferReceiveProgress.get(fileName).length() == 0) {
            this.files.get(fileName).setTransferStatus(true);
            return false;
        }

        Map<String, TechEntity[]> files = getFileTransfer();
        TechEntity[] transferInfo = files.get(fileName);

        if (transferInfo[0] instanceof Satellite || transferInfo[1] instanceof Satellite) {
            if (!transferInfo[0].communicable(transferInfo[1], transferInfo[0].getRange())) {
                if(!getFiles().get(fileName).getTransferStatus()) {
                    this.transferReceiveProgress.remove(fileName);
                    this.files.remove(fileName);
                }
            }
        }
        return true;
    }

    /**
     * Function that updates the receive transfer attribute for simulate.
     * It either updates the file content being transferred or is removed (if completed)
     * 
     * @param fileName
     * @return boolean
     */
    public boolean updateSendTransfer(String fileName) {
        Map<String, TechEntity[]> files = getFileTransfer();
        TechEntity[] transferInfo = files.get(fileName);

        if (transferInfo[0] instanceof Satellite || transferInfo[1] instanceof Satellite) {
            if (!transferInfo[0].communicable(transferInfo[1], transferInfo[0].getRange())) {
                this.transferSendProgress.remove(fileName);
                return false;
            }
        }

        int rate = findRate(fileName, false);
        
        if (rate > this.transferSendProgress.get(fileName).length()) {
            rate = this.transferSendProgress.get(fileName).length();
        }

        this.transferSendProgress.put(fileName, this.transferSendProgress.get(fileName).substring(rate));
        if (this.transferSendProgress.get(fileName).length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Finds the rate at which a file is transferred
     * @param fileName
     * @param receive
     * @return Integer
     */

    public int findRate(String fileName, boolean receive) {
        // We need to check if this is a device, if not then we take the individual rates
        // If it is, it must take the rate of the satellite sending it
        Map<String, TechEntity[]> files = getFileTransfer();
        TechEntity[] transferInfo = files.get(fileName);
        if (this instanceof Device) {
            if (transferInfo[0] instanceof Satellite) {
                if (transferInfo[0].getTransferSendProgress().size() > 0) {
                    return findBandwidth(transferInfo[0], receive) / transferInfo[0].getTransferSendProgress().size();
                } else {
                    return findBandwidth(transferInfo[0], receive);
                }
            } else {
                if (transferInfo[0].getTransferSendProgress().size() > 0) {
                    return findBandwidth(transferInfo[1], receive) / transferInfo[0].getTransferSendProgress().size();
                } else {
                    return findBandwidth(transferInfo[1], receive);
                }
            }
        } else {
            if (getId() == transferInfo[0].getId()) {
                if (transferInfo[0].getTransferSendProgress().size() > 0) {
                    return findBandwidth(transferInfo[0], receive) / transferInfo[0].getTransferSendProgress().size();
                } else {
                    return findBandwidth(transferInfo[0], receive);
                }
            } else {
                if (transferInfo[0].getTransferSendProgress().size() > 0) {
                    return findBandwidth(transferInfo[1], receive) / transferInfo[0].getTransferSendProgress().size();
                } else {
                    return findBandwidth(transferInfo[1], receive);
                }
            }
        }
    }

    public int findBandwidth(TechEntity entity, boolean receive) {
        String type = entity.getType();
        switch(type) {
            case "StandardSatellite":
                if (receive) {
                    return entity.getBandwidth()[0];
                } else {
                    return entity.getBandwidth()[1];
                }
            case "TeleportingSatellite":
                if (receive) {
                    return entity.getBandwidth()[0];
                } else {
                    return entity.getBandwidth()[1];
                }
        }
        return 0;
    }
}
