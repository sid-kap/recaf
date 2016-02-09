package recaf.comefrom;

import java.util.HashMap;
import java.util.Map;

import recaf.core.AbstractJavaImpl;
import recaf.core.functional.ED;
import recaf.core.functional.K0;
import recaf.core.functional.SD;

public class ComeFrom<R> extends AbstractJavaImpl<R> {

	private final static Map<String, K0> ks = new HashMap<>();
	
	public R Method(SD<R> body) {
		return typePreserving(body);
	}

	public SD<R> ComeFrom(ED<String> label) {
		return (rho, sigma, brk, contin, err) -> {
			label.accept(l -> {
				ks.put(l, sigma);
				sigma.call();
			}, err);
		};
	}
	
	@Override
	public SD<R> Labeled(String label, SD<R> s) {
		return (rho, sigma, brk, contin, err) -> {
			if (ks.containsKey(label)) {
				ks.get(label).call();
				return;
			}
			s.accept(rho, sigma, brk, contin, err);
		};
	}
	
}