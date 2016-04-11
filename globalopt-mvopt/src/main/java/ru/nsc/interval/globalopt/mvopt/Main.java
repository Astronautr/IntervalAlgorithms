package ru.nsc.interval.globalopt.mvopt;

import net.java.jinterval.interval.set.*;
import net.java.jinterval.rational.ExtendedRational;

public class Main {
    public static void main(String[] args) {
        SetIntervalContext ic = SetIntervalContexts.getPlain();
        SetInterval[] box = new SetInterval[]{ic.numsToInterval(50.0, 170.0), ic.numsToInterval(254.0, 299.0), ic.numsToInterval(2.3, 9.0), ic.numsToInterval(15.2, 34.8)};
        Functions.initFunctions();
        IAOGO algorithm = new IAOGO();
        long startTime = System.currentTimeMillis();
        algorithm.start(box, ExtendedRational.valueOf(1E-9),ic);
        long stopTime = System.currentTimeMillis();
        System.out.println(" time=" + (stopTime - startTime) / 1E3 + "s");
    }
}
