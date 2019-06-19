package cj.netos.contractbank.plugin.CoreEngine.bs;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.UpdateOptions;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.contractbank.args.BState;
import cj.netos.contractbank.args.CBankState;
import cj.netos.contractbank.bs.ICBankStateBS;
import cj.netos.contractbank.plugin.CoreEngine.db.ICBankStore;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

@CjService(name = "cbankStateBS")
public class CBankStateBS implements ICBankStateBS {
	@CjServiceRef
	ICBankStore bankStore;
	@Override
	public void save(CBankState state) {
		state.setId(null);
		String id = bankStore.home().saveDoc(TABLE_CBank_STATE, new TupleDocument<>(state));
		state.setId(id);

	}

	@Override
	public CBankState getState(String bank) {
		String cjql = "select {'tuple':'*'} from tuple ?(colName) ?(colType) where {'tuple.bank':'?(bank)'}";
		IQuery<CBankState> q = bankStore.home().createQuery(cjql);
		q.setParameter("colName", TABLE_CBank_STATE);
		q.setParameter("colType", CBankState.class.getName());
		q.setParameter("bank",bank);
		IDocument<CBankState> doc = q.getSingleResult();
		if (doc == null) {
			CBankState state = new CBankState();
			state.setBank(bank);
			state.setCtime(System.currentTimeMillis());
			state.setState(BState.opened);
			return state;
		}
		return doc.tuple();
	}

	@Override
	public void revokeCBank(String bank) {
		Bson filter = Document.parse(String.format("{'tuple.bank':'%s'}",bank));
		Bson update = Document.parse(String.format("{'$set':{'tuple.state':'%s'}}", BState.revoke));
		UpdateOptions uo = new UpdateOptions();
		uo.upsert(true);
		bankStore.home().updateDocOne(TABLE_CBank_STATE, filter, update, uo);
	}

	@Override
	public void freezeCBank(String bank) {
		Bson filter = Document.parse(String.format("{'tuple.bank':'%s'}",bank));
		Bson update = Document.parse(String.format("{'$set':{'tuple.state':'%s'}}", BState.freeze));
		UpdateOptions uo = new UpdateOptions();
		uo.upsert(true);
		bankStore.home().updateDocOne(TABLE_CBank_STATE, filter, update, uo);
	}

	@Override
	public void closedCBank(String bank) {
		Bson filter = Document.parse(String.format("{'tuple.bank':'%s'}",bank));
		Bson update = Document.parse(String.format("{'$set':{'tuple.state':'%s'}}", BState.closed));
		UpdateOptions uo = new UpdateOptions();
		uo.upsert(true);
		bankStore.home().updateDocOne(TABLE_CBank_STATE, filter, update, uo);
	}

	@Override
	public void resumeCBank(String bank) {
		Bson filter = Document.parse(String.format("{'tuple.bank':'%s'}",bank));
		Bson update = Document.parse(String.format("{'$set':{'tuple.state':'%s'}}", BState.opened));
		UpdateOptions uo = new UpdateOptions();
		uo.upsert(true);
		bankStore.home().updateDocOne(TABLE_CBank_STATE, filter, update, uo);
	}

}
