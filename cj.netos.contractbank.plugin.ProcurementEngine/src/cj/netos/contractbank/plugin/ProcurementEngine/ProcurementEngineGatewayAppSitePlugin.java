package cj.netos.contractbank.plugin.ProcurementEngine;

import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceSite;
import cj.studio.gateway.socket.app.IGatewayAppSitePlugin;

@CjService(name="$.studio.gateway.app.plugin",isExoteric=true)
public class ProcurementEngineGatewayAppSitePlugin implements IGatewayAppSitePlugin{
	@CjServiceSite
	IServiceSite site;
	@Override
	public Object getService(String name) {
		return site.getService(name);
	}

}
