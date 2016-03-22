import net.java.jinterval.interval.set.SetInterval;
import net.java.jinterval.interval.set.SetIntervalContext;
import net.java.jinterval.interval.set.SetIntervalContexts;
import net.java.jinterval.rational.BinaryValueSet;
import net.java.jinterval.rational.ExtendedRational;

public class Main {
    public static void main(String[] args) throws Exception {
        {
            SetIntervalContext ic = SetIntervalContexts.getPlain();
            Gradient[] box = Gradient.init(new SetInterval[]{ic.numsToInterval(19.1,55.2), ic.numsToInterval(15.2,34.8), ic.numsToInterval(0,1.18)},ic);
            IAOGO alghoritm = new IAOGO();
            ExtendedRational min = alghoritm.start(box, ExtendedRational.valueOf(1E-3), ic);
            System.out.println(min.doubleValue());
        }
    }
}
