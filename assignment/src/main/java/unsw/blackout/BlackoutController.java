package unsw.blackout;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import unsw.blackout.FileTransferException.VirtualFileNotFoundException;

//import org.junit.jupiter.api.DisplayNameGenerator.Standard;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;


public class BlackoutController {
    HashMap<String, TechEntity> techList = new HashMap<String, TechEntity>();

    public void createDevice(String deviceId, String type, Angle position) {
        switch (type) {
            case "HandheldDevice":
                techList.put(deviceId, new HandheldDevice(deviceId, type, position));
                break;
            case "LaptopDevice":
                techList.put(deviceId, new LaptopDevice(deviceId, type, position));
                break;
            case "DesktopDevice":
                techList.put(deviceId, new DesktopDevice(deviceId, type, position));
                break;
        }
    }

    public void removeDevice(String deviceId) {
        techList.remove(deviceId, techList.get(deviceId));
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        switch(type) {
            case "StandardSatellite":
                techList.put(satelliteId, new StandardSatellite(satelliteId, type, height, position));
                break;
            case "TeleportingSatellite":
                techList.put(satelliteId, new TeleportingSatellite(satelliteId, type, height, position));
                break;
            case "RelaySatellite":
                techList.put(satelliteId, new RelaySatellite(satelliteId, type, height, position));
                break;
        }
        
    }

    public void removeSatellite(String satelliteId) {
        techList.remove(satelliteId, techList.get(satelliteId));
    }

    public List<String> listDeviceIds() {
        List<String> deviceId_list = new ArrayList<String>();
        for (TechEntity entity : techList.values()) {
            if (entity instanceof Device) {
                deviceId_list.add(entity.getId());
            }
        }
        return deviceId_list;
    }

    public List<String> listSatelliteIds() {
        List<String> satelliteId_list = new ArrayList<String>();
        for (TechEntity entity : techList.values()) {
            if (entity instanceof Satellite) {
                satelliteId_list.add(entity.getId());
            }
        }
        return satelliteId_list;
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        techList.get(deviceId).addFiles(filename, content);
    }

    public EntityInfoResponse getInfo(String id) {
        TechEntity curr = techList.get(id);
        Map<String, FileInfoResponse> files = new HashMap<String, FileInfoResponse>();
        for (Map.Entry<String, File> file : curr.getFiles().entrySet()) {
            File info = file.getValue();
            files.put(file.getKey(), new FileInfoResponse(file.getKey(), info.getContent(), info.getFileLength(), info.getTransferStatus()));
            
        }
        return new EntityInfoResponse(id, curr.getPosition(), curr.getHeight(), curr.getType(), files);
    }

    public void simulate() {
        for (TechEntity entity : techList.values()) {
            entity.simulateM();
        }
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        List<String> inRange = new ArrayList<String>();
        TechEntity base = techList.get(id);
        
        double range = base.getRange();
        for (TechEntity entity : techList.values()) {
            if (inRange.contains(entity.getId())) {
                continue;
            } else if (entity != base && entity.getType() == "RelaySatellite" && base.communicable(entity, range)) {
                inRange.add(entity.getId());
                inRange = inRangetoRelay(entity, inRange, base);
            } else if (entity != base && base.communicable(entity, range) && !inRange.contains(entity.getId())) {
                inRange.add(entity.getId());
            }
        }
        return inRange;
    }

    /**
     * Helper function which extends the communicableEntitiesRange function due to RelaySatellites
     * @param relaySatellite
     * @param inRange
     * @param original
     * @return
     */
    public List<String> inRangetoRelay(TechEntity relaySatellite, List<String> inRange, TechEntity original) {
        for (TechEntity entity : techList.values()) {
            if (inRange.contains(entity.getId())) {
                continue;
            } else if (entity != relaySatellite && entity.getType() == "RelaySatellite" && 
                        ((Satellite) entity).communicable(relaySatellite, entity.getRange(), original) && entity.getId() != original.getId()) {
                inRange.add(entity.getId());
                inRange = inRangetoRelay(entity, inRange, original);
            } else if (entity != relaySatellite && ((Satellite) relaySatellite).communicable(entity, relaySatellite.getRange(), original) 
                        && entity.getId() != original.getId()) {
                inRange.add(entity.getId());
            }
        }
        return inRange;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        fileExistenceExceptions(fileName, fromId, toId);
        entityStorageExceptions(fileName, fromId, toId);

        List<String> inRange = communicableEntitiesInRange(fromId);
        TechEntity from = techList.get(fromId);
        TechEntity to = techList.get(toId);
        File transfer = new File(fileName, from.getFiles().get(fileName).getContent(), from.getFiles().get(fileName).getFileLength(), false);

        if (inRange.contains(toId) && from.hasSendBandwidth() && to.hasReceiveBandwidth() && from.getFiles().get(fileName).getTransferStatus()) {
            to.addTransferReceive(fileName, transfer.getContent(), from, to);
            from.addTransferSend(fileName, transfer.getContent(), from, to);
            transfer.setContent("");
            to.getFiles().put(fileName, transfer);
        }
    }


    /**
     * Helper function to find exceptions regarding file existence
     * @param fileName
     * @param fromId
     * @param toId
     * @throws FileTransferException
    */
     
    public void fileExistenceExceptions(String fileName, String fromId, String toId) throws FileTransferException {
        TechEntity from = techList.get(fromId);
        if (!from.getFiles().containsKey(fileName)) {
            throw new VirtualFileNotFoundException(fileName);
        }

        TechEntity target = techList.get(toId);
        if (target.getFiles().containsKey(fileName)) {
            throw new FileTransferException.VirtualFileAlreadyExistsException(fileName);
        }
    }

    
    /**
     * Helper function to find exceptions regarding file existence
     * @param fileName
     * @param fromId
     * @param toId
     * @throws FileTransferException
    */
    public void entityStorageExceptions(String fileName, String fromId, String toId) throws FileTransferException {
        TechEntity from = techList.get(fromId);
        TechEntity to = techList.get(toId);
        File transfer = from.getFiles().get(fileName);

        if (!from.hasReceiveBandwidth() || !from.hasSendBandwidth()) {
            throw new FileTransferException.VirtualFileNoBandwidthException(from.getId());
        }

        if (!to.hasReceiveBandwidth() || !to.hasSendBandwidth()) {
            throw new FileTransferException.VirtualFileNoBandwidthException(to.getId());
        }

        if (!to.canAddFile(transfer.getFileLength())) {
            throw new FileTransferException.VirtualFileNoStorageSpaceException(to.getId());
        }
    }
    

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }

}
