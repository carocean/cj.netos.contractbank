package cj.netos.contractbank.plugin.ProcurementEngine.db;

import cj.lns.chip.sos.cube.framework.ICube;

public interface ICBankStore {

	ICube bank(String bank);

	ICube home();

}