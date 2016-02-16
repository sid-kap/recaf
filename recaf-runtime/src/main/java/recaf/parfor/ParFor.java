package recaf.parfor;

import java.util.Collection;
import java.util.function.Function;

import recaf.core.cps.AbstractJavaImpl;
import recaf.core.cps.ED;
import recaf.core.cps.SD;

public class ParFor<R> extends AbstractJavaImpl<R> {
	
	public R Method(SD<R> body) {
		return typePreserving(body);
	}

	public <U> SD<R> Parfor(ED<Collection<U>> coll, Function<U, SD<R>> body) {
		return (rho, sigma, brk, contin, err) -> {
			coll.accept(v -> {
				v.parallelStream().forEach(u -> {
					body.apply(u).accept(rho, sigma, brk, contin, err);
				});
			}, err);
		};
	}
}
