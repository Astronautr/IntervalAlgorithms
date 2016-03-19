import com.kenai.jinterval.fast_nearest_round.DoubleInterval;
/**
Best time results (per 10 launches)
tolerance = 1E-3
real	0m0.628s
user	0m1.492s
sys	0m0.072s

*/

public class Main {
    public static void main(String[] args) {
        DoubleInterval d = DoubleInterval.valueOf(15.2,34.8);
        DoubleInterval X = DoubleInterval.valueOf(100,100);
        DoubleInterval L = DoubleInterval.valueOf(19.1,55.2);
        DoubleInterval theta = DoubleInterval.valueOf(0,1.18);
        //Here we wanted to initialize interval box = [ a b theta]
        //, where a = 2*L/d, b = 2*X/d
        DoubleInterval box[] = {L.multiply(DoubleInterval.valueOf(2,2)).divide(d), X.multiply(DoubleInterval.valueOf(2,2)).divide(d), theta};
        IAOGO algorithm = new IAOGO();
        //Now we'll launch basic version of interval algorithm of global optimization for chosen objective function,
        //interval box, tolerance and Context (ic)
        algorithm.start(box, 1E-3);
        System.out.println(algorithm.wList.peek().getAssessment());
    }
}
