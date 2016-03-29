package ru.nsc.interval.globalopt.v2;

import net.java.jinterval.interval.set.SetIntervalContexts;
import net.java.jinterval.rational.ExtendedRational;

class Functions {
    static Gradient area0(Gradient[] origin, Gradient[] dependent) {
        //a
        return origin[0].mul(Gradient.num(2)).div(origin[1]);
    }

    static Gradient area1(Gradient[] origin, Gradient[] dependent) {
        //b
        return Gradient.num(100).mul(Gradient.num(2)).div(origin[1]);
    }

    static Gradient area2(Gradient[] origin, Gradient[] dependent) {
        //A
        return ((dependent[0].sqr().add(
                (dependent[1].add(Gradient.num(1))).sqr()).
                sub(dependent[0].mul(Gradient.num(2)).mul(dependent[1].add(Gradient.num(1))).mul(origin[2].sin()))).
                intersectionX(SetIntervalContexts.getExact().numsToInterval(ExtendedRational.valueOf(0),ExtendedRational.POSITIVE_INFINITY))).sqrt();
    }

    static Gradient area3(Gradient[] origin, Gradient[] dependent) {
        //B
        return ((dependent[0].sqr().add(
                (dependent[1].sub(Gradient.num(1))).sqr()).
                sub(dependent[0].mul(Gradient.num(2)).mul(dependent[1].sub(Gradient.num(1))).mul(origin[2].sin()))).
                intersectionX(SetIntervalContexts.getExact().numsToInterval(ExtendedRational.valueOf(0),ExtendedRational.POSITIVE_INFINITY))).sqrt();
    }

    static Gradient area4(Gradient[] origin, Gradient[] dependent) {
        //C
        return (Gradient.num(1).add((dependent[1].sqr().sub(Gradient.num(1))).mul((origin[2].cos()).sqr()))).sqrt();
    }

    static Gradient area5(Gradient[] origin, Gradient[] dependent) {
        //D
        return ((Gradient.num(2).mul(Gradient.num(100)).sub(origin[1])).div(Gradient.num(2).mul(Gradient.num(100)).add(origin[1]))).sqrt();
    }

    static Gradient area6(Gradient[] origin, Gradient[] dependent) {
        //E
        return origin[0].mul(origin[2].cos()).div(Gradient.num(100).sub(origin[0].mul(origin[2].sin())));
    }

    static Gradient area7(Gradient[] origin, Gradient[] dependent) {
        //F
        return (dependent[1].sqr().sub(Gradient.num(1))).sqrt();
    }
}
