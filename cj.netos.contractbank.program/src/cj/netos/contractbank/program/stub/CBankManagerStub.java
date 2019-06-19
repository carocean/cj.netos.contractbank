package cj.netos.contractbank.program.stub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cj.netos.contractbank.args.BState;
import cj.netos.contractbank.args.CBankInfo;
import cj.netos.contractbank.args.CBankState;
import cj.netos.contractbank.bs.ICBankInfoBS;
import cj.netos.contractbank.bs.ICBankStateBS;
import cj.netos.contractbank.stub.ICBankManagerStub;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.stub.GatewayAppSiteRestStub;
import cj.ultimate.util.StringUtil;

@CjService(name = "/manager.service")
public class CBankManagerStub extends GatewayAppSiteRestStub implements ICBankManagerStub {
	@CjServiceRef(refByName = "CoreEngine.cbankInfoBS")
	ICBankInfoBS cbankInfoBS;

	@CjServiceRef(refByName = "CoreEngine.cbankStateBS")
	ICBankStateBS cbankStateBS;

	@Override
	public String registerBank(String bankName, String president, String company, String expiredDate)
			throws CircuitException {
		if (StringUtil.isEmpty(bankName)) {
			throw new CircuitException("404", String.format("市场名为空"));
		}
		if (StringUtil.isEmpty(president)) {
			throw new CircuitException("404", String.format("场长为空"));
		}
		if (StringUtil.isEmpty(expiredDate)) {
			throw new CircuitException("404", String.format("到期日期为空"));
		}

		CBankInfo info = new CBankInfo();
		info.setCode(null);
		info.setPresident(president);
		info.setCompany(company);
		info.setName(bankName);
		info.setCtime(System.currentTimeMillis());
		CBankState state = new CBankState();
		state.setState(BState.opened);
		info.setBstate(state.getState());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date expire = null;
		try {
			expire = sdf.parse(expiredDate);
		} catch (ParseException e) {
			throw new CircuitException("500", e);
		}
		info.setExpiredTime(expire.getTime());

		cbankInfoBS.saveCBank(info);
		// 插入营业状态为正常
		state.setBank(info.getCode());
		state.setCtime(System.currentTimeMillis());
		cbankStateBS.save(state);
		return info.getCode();
	}

	@Override
	public void updateBankName(String bank, String name) throws CircuitException {
		this.cbankInfoBS.updateCBankName(bank, name);
	}

	@Override
	public void updateBankPresident(String bank, String president) throws CircuitException {
		this.cbankInfoBS.updateCBankPresident(bank, president);
	}

	@Override
	public void updateBankCompany(String bank, String company) throws CircuitException {
		this.cbankInfoBS.updateCBankCompany(bank, company);
	}

	@Override
	public void revokeBank(String bank) throws CircuitException {
		this.cbankStateBS.revokeCBank(bank);
	}

	@Override
	public void freezeBank(String bank) {
		this.cbankStateBS.freezeCBank(bank);
	}

	@Override
	public void closedBank(String bank) {
		this.cbankStateBS.closedCBank(bank);
	}

	@Override
	public void resumeBank(String bank) {
		this.cbankStateBS.resumeCBank(bank);
	}

	@Override
	public CBankInfo getBankInfo(String bank) {
		return cbankInfoBS.getCBankInfo(bank);
	}

	@Override
	public List<CBankInfo> pageBankInfo(int currPage, int pageSize) {
		return this.cbankInfoBS.pageCBankInfo(currPage, pageSize);
	}

	@Override
	public BState getBankState(String bank) {
		return this.cbankStateBS.getState(bank).getState();
	}

}
