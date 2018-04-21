package foo.bar.checks;

import net.netzgut.integral.canary.beans.CanaryCheck;
import net.netzgut.integral.canary.beans.CanaryResult;

/**
 * A simple check that fails on Mondays, degrades on Tuesdays and is fine otherwise.
 */
public class SimpleCheck implements CanaryCheck {

    @Override
    public CanaryResult run() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case Calendar.MONDAY: {
                return CanaryResult.Failed(getIdentifier(), "It's Monday");
            }
            case Calendar.TUESDAY: {
                return CanaryResult.Degraded(getIdentifier(), "It's Tuesday");
            }
            default: {
                return CanaryResult.Ok(getIdentifier());
            }
        }
    }

    @Override
    public String getIdentifier() {
        return "foo.bar.checks.simple-check";
    }

}
