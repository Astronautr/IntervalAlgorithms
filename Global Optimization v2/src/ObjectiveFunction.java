
public class ObjectiveFunction {
    static Gradient areaValues(Gradient[] origin, Gradient[] dependent) { //Fv
        return (Gradient.num(1.0 / Math.PI).
                mul(dependent[6].neg().mul(dependent[5].atan()).
                        add(dependent[6].
                                mul(dependent[0].sqr().add((dependent[1].add(Gradient.num(1))).sqr()).add(Gradient.num(-2).mul(dependent[1]).
                                        mul(Gradient.num(1).add(dependent[0].mul(origin[2].sin()))))
                                ).div(dependent[2].mul(dependent[3])).mul((dependent[2].mul(dependent[5]).div(dependent[3])).atan()).
                                add(origin[2].cos().div(dependent[4]).
                                        mul(((dependent[0].mul(dependent[1]).sub(dependent[7].sqr().mul(origin[2].sin()))).div(dependent[7].mul(dependent[4]))).atan().
                                                add((dependent[7].mul(origin[2].sin()).div(dependent[4])).atan()
                                                )
                                        )
                                )
                        )
                )).neg();
    }
}
