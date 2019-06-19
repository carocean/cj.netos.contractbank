package cj.netos.contractbank.program;

import cj.netos.contractbank.args.BState;
import cj.netos.contractbank.args.CBankState;
import cj.netos.contractbank.bs.ICBankInfoBS;
import cj.netos.contractbank.bs.ICBankStateBS;
import cj.netos.contractbank.stub.ICBankManagerStub;
import cj.studio.ecm.Scope;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.Frame;
import cj.studio.gateway.socket.pipeline.IAnnotationInputValve;
import cj.studio.gateway.socket.pipeline.IIPipeline;
import cj.ultimate.util.StringUtil;

@CjService(name = "cbankStateVavle", scope = Scope.multiton)
public class CBankStateVavle implements IAnnotationInputValve {

	@CjServiceRef(refByName = "MarketEngine.cbankStateBS")
	ICBankStateBS cbankStateBS;
	@CjServiceRef(refByName = "MarketEngine.cbankInfoBS")
	ICBankInfoBS cbankInfoBS;

	@Override
	public void onActive(String inputName, IIPipeline pipeline) throws CircuitException {
		pipeline.nextOnActive(inputName, this);
	}

	@Override
	public void flow(Object request, Object response, IIPipeline pipeline) throws CircuitException {
		if (!(request instanceof Frame)) {
			pipeline.nextFlow(request, response, this);
			return;
		}
		Frame f = (Frame) request;
		if (ICBankManagerStub.class.getName().equals(f.head("Rest-StubFace"))) {
			pipeline.nextFlow(request, response, this);
			return;
		}
		String market = f.parameter("market");
		if (StringUtil.isEmpty(market)) {
			pipeline.nextFlow(request, response, this);
			return;
		}
		if (this.cbankInfoBS.isExpired(market)) {
			CBankState state = new CBankState();
			state.setBank(market);
			state.setCtime(System.currentTimeMillis());
			state.setState(BState.freeze);
			state.setDesc("The license expires and the bank has been frozen. Please re-apply for the license.");
			cbankStateBS.save(state);
		}
		CBankState state = cbankStateBS.getState(market);
		switch (state.getState()) {
		case closed:
		case freeze:
		case revoke:
			throw new CircuitException("308", String.format("Bank:%s Denial of Service, Reasons:%s %s", market, state.getState(),
					state.getDesc() == null ? "" : state.getDesc()));
		case opened:
			pipeline.nextFlow(request, response, this);
			break;
		}

	}

	@Override
	public void onInactive(String inputName, IIPipeline pipeline) throws CircuitException {
		pipeline.nextOnInactive(inputName, this);
	}

	@Override
	public int getSort() {
		return 0;
	}

}
