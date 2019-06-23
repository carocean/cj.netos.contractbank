package cj.netos.contractbank.program.reciever.transaction;

import java.util.HashMap;
import java.util.Map;

import cj.netos.contractbank.bs.ICBankExchangeOrderBS;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.Circuit;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.Frame;
import cj.studio.ecm.net.io.MemoryContentReciever;
import cj.studio.gateway.socket.app.IGatewayAppSiteResource;
import cj.studio.gateway.socket.app.IGatewayAppSiteWayWebView;
import cj.ultimate.gson2.com.google.gson.Gson;
import cj.ultimate.gson2.com.google.gson.reflect.TypeToken;

@CjService(name = "/reciever/transaction/exchange.service")
public class ExchangeInformer implements IGatewayAppSiteWayWebView {
	@CjServiceRef(refByName = "CoreEngine.transaction#cbankExchangeOrderBS")
	ICBankExchangeOrderBS cbankExchangeOrderBS;

	@Override
	public void flow(Frame frame, Circuit circuit, IGatewayAppSiteResource resource) throws CircuitException {
		frame.content().accept(new MemoryContentReciever() {
			@Override
			public void done(byte[] b, int pos, int length) throws CircuitException {
				super.done(b, pos, length);
				Map<String, Object> response = new Gson().fromJson(new String(frame.content().readFully()),
						new TypeToken<HashMap<String, Object>>() {
						}.getType());
				String bankno = frame.parameter("bankno");
				String orderno = frame.parameter("orderno");
				cbankExchangeOrderBS.complateOrder(bankno, orderno, response);
			}
		});

	}

}
