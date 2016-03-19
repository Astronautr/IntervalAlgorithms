import net.java.jinterval.interval.set.SetInterval;
import net.java.jinterval.interval.set.SetIntervalContext;
import net.java.jinterval.interval.set.SetIntervalContexts;
/**
Best time results (per 10 launches)
tolerance = 1E-3
real	0m0.447s
user	0m0.880s
sys	0m0.048s

*/

public class Main {
    public static void main(String[] args) {
        SetIntervalContext ic = SetIntervalContexts.getFast();
        SetInterval d = ic.numsToInterval(15.2,34.8);
        SetInterval X = ic.numsToInterval(100,100);
        SetInterval L = ic.numsToInterval(19.1,55.2);
        SetInterval theta = ic.numsToInterval(0,1.18);
        //Here we wanted to initialize interval box = [ a b theta]
        //, where a = 2*L/d, b = 2*X/d
        SetInterval box[] = {ic.div(ic.mul(ic.numsToInterval(2,2),L),d), ic.div(ic.numsToInterval(200,200),d),theta};
        IAOGO algorithm = new IAOGO();
        //Now we'll launch basic version of interval algorithm of global optimization for chosen objective function,
        //interval box, tolerance and Context (ic)
        algorithm.start(box, 1E-3,ic);
        System.out.println(algorithm.wList.peek().getAssessment());
    }
}
