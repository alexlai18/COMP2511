package unsw.blackout;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;
import java.util.Arrays;

public abstract class Satellite extends TechEntity {

    Satellite (String id, String type, double height, Angle position, double range) {
        super(id, type, height, position, range);
    }

    public abstract String[] getSupport();

    @Override
    public boolean communicable(TechEntity s, double range) {
        if (s instanceof Satellite) {
            return (MathsHelper.getDistance(this.getHeight(), this.getPosition(), s.getHeight(), s.getPosition()) <= range
                && MathsHelper.isVisible(this.getHeight(), this.getPosition(), s.getHeight(), s.getPosition()));
        } else if (Arrays.asList(getSupport()).contains(s.getType())) {
            return (MathsHelper.getDistance(this.getHeight(), this.getPosition(), s.getPosition()) <= range
                && MathsHelper.isVisible(this.getHeight(), this.getPosition(), s.getPosition()));
        } else {
            return false;
        }
    }

    /**
     * This communicable function only applies to RelaySatellites to avoid repeating cases
     * @param s
     * @param range
     * @param base
     * @return
     */
    public boolean communicable(TechEntity s, double range, TechEntity base) {
        if (s instanceof Satellite && Arrays.asList(((Satellite)s).getSupport()).contains(base.getType())) {
            return (MathsHelper.getDistance(this.getHeight(), this.getPosition(), s.getHeight(), s.getPosition()) <= range
                && MathsHelper.isVisible(this.getHeight(), this.getPosition(), s.getHeight(), s.getPosition()));
        } else if (Arrays.asList(getSupport()).contains(s.getType())) {
            return (MathsHelper.getDistance(this.getHeight(), this.getPosition(), s.getPosition()) <= range
                && MathsHelper.isVisible(this.getHeight(), this.getPosition(), s.getPosition()));
        } else {
            return false;
        }
    }
}
