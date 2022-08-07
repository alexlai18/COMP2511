package unsw.blackout;

import unsw.utils.Angle;

public class RelaySatellite extends Satellite {
    private final int speed = 1500;
    private final String[] support = {"HandheldDevice", "LaptopDevice", "DesktopDevice"};
    private boolean clockwise;


    RelaySatellite(String id, String type, double height, Angle position) {
        super(id, type, height, position, 300000);
        if ((position.toDegrees() < 345) && (position.toDegrees() >= 140)) {
            this.clockwise = true;
        } else {
            this.clockwise = false;
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

        if (getPosition().toDegrees() <= 190 && getPosition().toDegrees() >= 140) {
            if (change.toDegrees() < 140) {
                this.clockwise = false;
            } else if (change.toDegrees() > 190) {
                this.clockwise = true;
            }
        }
        super.setPosition(change);
    }

    @Override
    public String[] getSupport() {
        return support;
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

    @Override
    public int[] getBandwidth() {
        return new int[2];
    }
}
