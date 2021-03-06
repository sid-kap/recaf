package recaf.paper.stm;

import java.util.function.Function;
import java.util.function.Supplier;

public
//BEGIN_MUJAVA_ALG
interface MuJava<R, S> {
	S Exp(Supplier<Void> e);
	S If(Supplier<Boolean> c, S s1, S s2);
	<T> S For(Supplier<Iterable<T>> e, Function<T, S> s);
	<T> S Decl(Supplier<T> e, Function<T, S> s);
	S Seq(S s1, S s2);
	S Return(Supplier<R> e);
	S Empty();
}
//END_MUJAVA_ALG
