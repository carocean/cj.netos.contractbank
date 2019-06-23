package cj.netos.contractbank.plugin.CoreEngine.bs;

import java.util.ArrayList;
import java.util.List;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.contractbank.args.CBankProperty;
import cj.netos.contractbank.bs.ICBankPropertiesBS;
import cj.netos.contractbank.plugin.CoreEngine.db.ICBankStore;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

@CjService(name = "cbankPropertiesBS")
public class CBankPropertiesBS implements ICBankPropertiesBS {
	@CjServiceRef
	ICBankStore cbankStore;

	@Override
	public void remove(String bank, String key) {
		cbankStore.home().deleteDocOne(TABLE_KEY, String.format("{'tuple.bank':'%s','tuple.key':'%s'}", bank, key));
	}

	@Override
	public void put(String bank, String key, String value, String desc) {
		if (containsKey(bank, key)) {
			remove(bank, key);
		}
		CBankProperty property = new CBankProperty(bank, key, value, desc);
		cbankStore.home().saveDoc(TABLE_KEY, new TupleDocument<>(property));
	}

	@Override
	public boolean containsKey(String bank, String key) {
		return cbankStore.home().tupleCount(TABLE_KEY, String.format("{'tuple.bank':'%s','tuple.key':'%s'}", bank, key)) > 0;
	}

	@Override
	public String desc(String bank, String key) {
		String cjql = String.format(
				"select {'tuple.desc':1} from tuple %s %s where {'tuple.bank':'%s','tuple.key':'%s'}", TABLE_KEY,
				CBankProperty.class.getName(), bank, key);
		IQuery<CBankProperty> q = cbankStore.home().createQuery(cjql);
		IDocument<CBankProperty> doc = q.getSingleResult();
		if (doc == null)
			return null;
		return doc.tuple().getDesc();
	}

	@Override
	public String get(String bank, String key) {
		String cjql = String.format("select {'tuple.value':1} from tuple %s %s where {'tuple.bank':'%s','tuple.key':'%s'}",
				TABLE_KEY, CBankProperty.class.getName(), bank, key);
		IQuery<CBankProperty> q = cbankStore.home().createQuery(cjql);
		IDocument<CBankProperty> doc = q.getSingleResult();
		if (doc == null)
			return null;
		return doc.tuple().getValue();
	}

	@Override
	public String[] enumKey(String bank) {
		String cjql = String.format("select {'tuple.key':1} from tuple %s %s where {'tuple.bank':'%s'}", TABLE_KEY,
				CBankProperty.class.getName(), bank);
		IQuery<CBankProperty> q = cbankStore.home().createQuery(cjql);
		List<String> list = new ArrayList<String>();
		List<IDocument<CBankProperty>> docs = q.getResultList();
		for (IDocument<CBankProperty> doc : docs) {
			list.add(doc.tuple().getKey());
		}
		return list.toArray(new String[0]);
	}

	@Override
	public String[] pageKeys(String bank, int currPage, int pageSize) {
		String cjql = String.format(
				"select {'tuple.key':1}.limit(%s).skip(%s) from tuple %s %s where {'tuple.bank':'%s'}", pageSize,
				currPage, TABLE_KEY, CBankProperty.class.getName(), bank);
		IQuery<CBankProperty> q = cbankStore.home().createQuery(cjql);
		List<String> list = new ArrayList<String>();
		List<IDocument<CBankProperty>> docs = q.getResultList();
		for (IDocument<CBankProperty> doc : docs) {
			list.add(doc.tuple().getKey());
		}
		return list.toArray(new String[0]);
	}

	@Override
	public long count(String bank) {
		return cbankStore.home().tupleCount(TABLE_KEY, String.format("{'tuple.bank':'%s'}", bank));
	}

}
