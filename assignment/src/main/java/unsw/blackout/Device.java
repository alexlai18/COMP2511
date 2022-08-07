package unsw.blackout;

import unsw.utils.Angle;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import unsw.utils.MathsHelper;

public abstract class Device extends TechEntity {

    Device (String id, String type, Angle position, double range) {
        super(id, type, RADIUS_OF_JUPITER, position, range);
    }

    @Override
    boolean communicable(TechEntity s, double range) {
        if (s instanceof Device) {
            return false;
        } else {
            if (s.communicable(this, range)) {
                return (MathsHelper.getDistance(s.getHeight(), s.getPosition(), this.getPosition()) <= range 
                    && MathsHelper.isVisible(s.getHeight(), s.getPosition(), this.getPosition()));
            } else {
                return false;
            }
        }
    }

    @Override
    public int[] getBandwidth() {
        return new int[2];
    }
}
