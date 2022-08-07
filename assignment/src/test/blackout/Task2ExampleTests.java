package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;
import unsw.utils.MathsHelper;

import java.util.Arrays;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExampleTests {
    @Test
    public void testEntitiesInRange() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(315));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(180));
        controller.createSatellite("Satellite3", "StandardSatellite", 2000 + RADIUS_OF_JUPITER, Angle.fromDegrees(175));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite2"), controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1"), controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2", "Satellite1"), controller.communicableEntitiesInRange("DeviceB"));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceD"), controller.communicableEntitiesInRange("Satellite3"));
    }

    @Test
    public void testSomeExceptionsForSend() {
        // just some of them... you'll have to test the rest
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertThrows(FileTransferException.VirtualFileNotFoundException.class, () -> controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        controller.simulate(msg.length() * 2);
        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class, () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
    }

    @Test
    public void testMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(337.95), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testExample() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        controller.simulate(msg.length() * 2);
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        controller.simulate(msg.length());
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        // Hints for further testing:
        // - What about checking about the progress of the message half way through?
        // - Device/s get out of range of satellite
        // ... and so on.
    }

    @Test
    public void testRelayMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(180));

        // moves in negative direction
        assertEquals(
                        new EntityInfoResponse("Satellite1", Angle.fromDegrees(180), 100 + RADIUS_OF_JUPITER,
                                        "RelaySatellite"),
                        controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(178.77), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(177.54), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(176.31), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));

        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(170.18), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(24);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        // edge case
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(139.49), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        // coming back
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(146.85), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testTeleportingMovement() {
        // Test for expected teleportation movement behaviour
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                        Angle.fromDegrees(0));

        // Satellite position should increase if going anticlockwise (except from 360 -> 0)
        // Verify that Satellite1 is going in a anticlockwise direction (default)
        controller.simulate();
        Angle clockwiseOnFirstMovement = controller.getInfo("Satellite1").getPosition();
        controller.simulate();
        Angle clockwiseOnSecondMovement = controller.getInfo("Satellite1").getPosition();
        assertTrue(clockwiseOnSecondMovement.compareTo(clockwiseOnFirstMovement) == 1);

        // It should take 250 simulations to reach theta = 180.
        // Simulate until Satellite1 reaches theta=180
        controller.simulate(250);

        // Verify that Satellite1 is now at theta=0
        assertTrue(controller.getInfo("Satellite1").getPosition().toDegrees() % 360 == 0);
    }

    // My Tests
    @Test
    public void testStandardLargePositionInput() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER,
        Angle.fromDegrees(720));

        assertTrue(controller.getInfo("Satellite1").getPosition().toDegrees() == 0);

        controller.createSatellite("Satellite2", "StandardSatellite", 100 + RADIUS_OF_JUPITER,
        Angle.fromDegrees(717));
        
        assertTrue(controller.getInfo("Satellite2").getPosition().toDegrees() == 357);

        controller.simulate();

        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(357.95), 100 + RADIUS_OF_JUPITER,
                        "StandardSatellite"), controller.getInfo("Satellite1"));
        assertEquals(new EntityInfoResponse("Satellite2", Angle.fromDegrees(354.95), 100 + RADIUS_OF_JUPITER,
                        "StandardSatellite"), controller.getInfo("Satellite2"));
    }

    @Test
    public void testNegativePositionInput() {
        BlackoutController controller = new BlackoutController();
        
        controller.createSatellite("Standard1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(-1));
        assertTrue(controller.getInfo("Standard1").getPosition().toDegrees() == 359);

        controller.createSatellite("Teleporting1", "TeleportingSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(-100));
        assertTrue(controller.getInfo("Teleporting1").getPosition().toDegrees() == 260);

        controller.createSatellite("Relay1", "RelaySatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(-15));
        assertTrue(controller.getInfo("Relay1").getPosition().toDegrees() == 345);

        controller.simulate();

        // Checking directions
        assertTrue(controller.getInfo("Relay1").getPosition().toDegrees() > 345);
        assertTrue(controller.getInfo("Teleporting1").getPosition().toDegrees() > 260);
        assertTrue(controller.getInfo("Standard1").getPosition().toDegrees() < 359);
    }

    @Test
    public void testRemainingExceptions() {
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 1 device
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        // Check Bandwidth Exception (Standard can only send or receive 1 at a time)
        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        controller.addFileToDevice("DeviceC", "FileBeta", msg);
        assertThrows(FileTransferException.VirtualFileNoBandwidthException.class, () -> controller.sendFile("FileBeta", "DeviceC", "Satellite1"));
        
        // Check storage exception (Standard can only hold 3 files)
        controller.simulate(msg.length());
        assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceC", "Satellite1"));

        controller.simulate(msg.length());
        controller.addFileToDevice("DeviceC", "FileCharlie", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileCharlie", "DeviceC", "Satellite1"));

        controller.simulate(msg.length());
        controller.addFileToDevice("DeviceC", "FileDelta", msg);
        assertThrows(FileTransferException.VirtualFileNoStorageSpaceException.class, () -> controller.sendFile("FileDelta", "DeviceC", "Satellite1"));

        // Check storage exception (Standard can only hold 80 bytes)
        controller.createSatellite("Satellite2", "StandardSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        
        String message = "a";
        for (int i = 0; i < 82; i++) {
            message += "a";
        }

        controller.addFileToDevice("DeviceC", "FileEcho", message);
        assertThrows(FileTransferException.VirtualFileNoStorageSpaceException.class, () -> controller.sendFile("FileEcho", "DeviceC", "Satellite1"));
    }

    @Test
    public void testEntitiesInRangeAfterSim() {
        BlackoutController controller = new BlackoutController();

        // Tests the communicable function, where it finds all the entities in range
        // Different Satellites have different conditions
        controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "TeleportingSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(315));
        controller.createSatellite("Satellite3", "RelaySatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(330));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("DeviceD", "DesktopDevice", Angle.fromDegrees(180));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite2", "Satellite3"), controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1"), controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"), controller.communicableEntitiesInRange("Satellite3"));

        controller.simulate(10);
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB"), controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC", "Satellite3"), controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC", "Satellite2"), controller.communicableEntitiesInRange("Satellite3"));
    }

    @Test
    public void testInRangeExtendedByRelay() {
        BlackoutController controller = new BlackoutController();
        controller.createSatellite("Satellite1", "RelaySatellite", 88568, Angle.fromDegrees(112));
        controller.createSatellite("Satellite2", "TeleportingSatellite", 82871, Angle.fromDegrees(151));
        controller.createDevice("Device1", "DesktopDevice", Angle.fromDegrees(80));

        // Device1 is not in sight of Satellite2
        assertTrue(!MathsHelper.isVisible(82871, Angle.fromDegrees(151), Angle.fromDegrees(80)));

        // Device1 can communicate with both satellites because of the extension of RelaySatellite
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1", "Satellite2"), controller.communicableEntitiesInRange("Device1"));
    }

    @Test
    public void testMultipleFilesAtOnce() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(320));
        
        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        controller.addFileToDevice("DeviceC", "FileBeta", msg);

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceC", "Satellite1"));

        controller.simulate();

        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
    }

    @Test
    public void testAroundTheWorld() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("1", "StandardSatellite", 78880, Angle.fromDegrees(118));
        controller.createSatellite("2", "RelaySatellite", 78783, Angle.fromDegrees(71));
        controller.createSatellite("3", "RelaySatellite", 79204, Angle.fromDegrees(24));
        controller.createSatellite("4", "RelaySatellite", 76868, Angle.fromDegrees(335));
        controller.createSatellite("5", "RelaySatellite", 77307, Angle.fromDegrees(317));
        controller.createSatellite("6", "RelaySatellite", 77961, Angle.fromDegrees(275));
        controller.createSatellite("7", "RelaySatellite", 78762, Angle.fromDegrees(235));
        controller.createSatellite("8", "RelaySatellite", 79726, Angle.fromDegrees(189));

        controller.createDevice("Device", "HandheldDevice", Angle.fromDegrees(186));

        String msg = "Hey";
        controller.addFileToDevice("Device", "FileAlpha", msg);

        assertListAreEqualIgnoringOrder(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"), controller.communicableEntitiesInRange("Device"));
    }

    @Test
    public void testInRangeExtendedByRelayIsNotSupported() {
        BlackoutController controller = new BlackoutController();
        controller.createSatellite("Satellite1", "RelaySatellite", 88568, Angle.fromDegrees(112));
        controller.createSatellite("Satellite2", "StandardSatellite", 82871, Angle.fromDegrees(151));
        controller.createDevice("Device1", "DesktopDevice", Angle.fromDegrees(80));

        // Device1 is not in sight of Satellite2
        assertTrue(!MathsHelper.isVisible(82871, Angle.fromDegrees(151), Angle.fromDegrees(80)));

        // Device1 cannot communicate with Satellite2, as StandardSatellites cannot communicate with DesktopDevice
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"), controller.communicableEntitiesInRange("Device1"));
    }

    @Test
    public void testOutOfRange() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", 80000, Angle.fromDegrees(0));
        controller.createDevice("Device1", "HandheldDevice", Angle.fromDegrees(30));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"), controller.communicableEntitiesInRange("Device1"));
        
        // Creates a file and sends it to the satellite
        String msg = "Hey";
        controller.addFileToDevice("Device1", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Device1", "Satellite1"));

        controller.simulate();

        // Satellite goes out of range
        assertListAreEqualIgnoringOrder(java.util.Collections.emptyList(), controller.communicableEntitiesInRange("Device1"));

        // The file transfer is cancelled
        assertTrue(controller.getInfo("Satellite1").getFiles().isEmpty());
    }

    @Test 
    public void testDeviceToTeleportingSendFile() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 80000, Angle.fromDegrees(179));
        controller.createDevice("Device1", "HandheldDevice", Angle.fromDegrees(180));

        String msg = "Tooooooooooooooootot";

        controller.addFileToDevice("Device1", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Device1", "Satellite1"));

        controller.simulate(2);

        // The file was removed from the recipient and the sender has all the 't' bytes gone
        assertTrue(controller.getInfo("Satellite1").getFiles().isEmpty());
        assertEquals(new FileInfoResponse("FileAlpha", "Tooooooooooooooooo", 18, true), controller.getInfo("Device1").getFiles().get("FileAlpha"));
    }

    @Test 
    public void testTeleportingSender() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 80000, Angle.fromDegrees(178));
        controller.createDevice("Device1", "HandheldDevice", Angle.fromDegrees(180));
        controller.createDevice("Device2", "HandheldDevice", Angle.fromDegrees(180));

        String msg = "Tooooooooooooooooooootot";

        controller.addFileToDevice("Device1", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Device1", "Satellite1"));

        controller.simulate(2);

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "Device2"));
        
        controller.simulate(2);
        // The file transfer is completed instantly and the 't' bytes are removed
        assertEquals(new FileInfoResponse("FileAlpha", "Tooooooooooooooooooooo", 22, true), controller.getInfo("Device2").getFiles().get("FileAlpha"));

    }
}
