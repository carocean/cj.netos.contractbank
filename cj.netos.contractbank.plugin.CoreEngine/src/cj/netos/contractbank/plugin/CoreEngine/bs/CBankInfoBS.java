package cj.netos.contractbank.plugin.CoreEngine.bs;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.contractbank.args.CBankInfo;
import cj.netos.contractbank.bs.ICBankInfoBS;
import cj.netos.contractbank.plugin.CoreEngine.db.ICBankStore;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;

@CjService(name = "CBankInfoBS")
public class CBankInfoBS implements ICBankInfoBS {
	@CjServiceRef
	ICBankStore marketStore;
	@Override
	public boolean existsCBankName(String name) {
		String where = String.format("{'tuple.name':'%s'}", name);
		return marketStore.home().tupleCount(TABLE_CBank_INFO, where) > 0;
	}

	@Override
	public void saveCBank(CBankInfo info) throws CircuitException {
		if (existsCBankName(info.getName())) {
			throw new CircuitException("405", "已存在市场名为：" + info.getName());
		}
		info.setCode(null);
		String id =  marketStore.home().saveDoc(TABLE_CBank_INFO, new TupleDocument<>(info));
		info.setCode(id);
	}

	@Override
	public boolean existsCBankCode(String market) {
		String where = String.format("{'_id':ObjectId('%s')}", market);
		return  marketStore.home().tupleCount(TABLE_CBank_INFO, where) > 0;
	}

	@Override
	public CBankInfo getCBankInfo(String market) {
		String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {'_id':ObjectId('%s')}",
				TABLE_CBank_INFO, CBankInfo.class.getName(), market);
		IQuery<CBankInfo> q =  marketStore.home().createQuery(cjql);
		IDocument<CBankInfo> doc = q.getSingleResult();
		if (doc == null)
			return null;
		doc.tuple().setCode(doc.docid());
		return doc.tuple();
	}

	@Override
	public List<CBankInfo> pageCBankInfo(int currPage, int pageSize) {
		String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple %s %s where {}", pageSize,
				currPage, TABLE_CBank_INFO, CBankInfo.class.getName());
		IQuery<CBankInfo> q =  marketStore.home().createQuery(cjql);
		List<IDocument<CBankInfo>> docs = q.getResultList();
		List<CBankInfo> list = new ArrayList<CBankInfo>();
		for (IDocument<CBankInfo> doc : docs) {
			doc.tuple().setCode(doc.docid());
			list.add(doc.tuple());
		}
		return list;
	}

	@Override
	public void updateCBankName(String market, String name) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", market));
		Bson update = Document.parse(String.format("{'$set':{'tuple.name':'%s'}}", name));
		 marketStore.home().updateDocOne(TABLE_CBank_INFO, filter, update);
	}

	@Override
	public void updateCBankPresident(String market, String president) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", market));
		Bson update = Document.parse(String.format("{'$set':{'tuple.president':'%s'}}", president));
		 marketStore.home().updateDocOne(TABLE_CBank_INFO, filter, update);
	}

	@Override
	public void updateCBankCompany(String market, String company) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", market));
		Bson update = Document.parse(String.format("{'$set':{'tuple.company':'%s'}}", company));
		 marketStore.home().updateDocOne(TABLE_CBank_INFO, filter, update);
	}

	@Override
	public boolean isExpired(String market) throws CircuitException {
		CBankInfo info = getCBankInfo(market);
		if (info == null) {
			throw new CircuitException("404",
					String.format("The bankno %s of silvermarket does not exist. ", market));
		}
		return info.getExpiredTime() <= System.currentTimeMillis();
	}
}
