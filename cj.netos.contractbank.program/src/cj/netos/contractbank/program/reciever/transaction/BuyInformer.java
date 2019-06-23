package cj.netos.contractbank.program.reciever.transaction;

import java.util.HashMap;
import java.util.Map;

import cj.netos.contractbank.bs.ICBankBuyOrderBS;
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
@CjService(name = "/reciever/transaction/buy.service")
public class BuyInformer implements IGatewayAppSiteWayWebView {
	@CjServiceRef(refByName = "CoreEngine.transaction#cbankBuyOrderBS")
	ICBankBuyOrderBS cbankBuyOrderBS;
	@Override
	public void flow(Frame frame, Circuit circuit, IGatewayAppSiteResource resource) throws CircuitException {
		frame.content().accept(new MemoryContentReciever() {
			@Override
			public void done(byte[] b, int pos, int length) throws CircuitException {
				super.done(b, pos, length);
				Map<String, Object> response=new Gson().fromJson(new String(frame.content().readFully()), new TypeToken<HashMap<String,Object>>(){}.getType());
				String bankno=frame.parameter("bankno");
				String orderno=frame.parameter("orderno");
				cbankBuyOrderBS.complateOrder(bankno,orderno,response);
			}
		});

	}

}
