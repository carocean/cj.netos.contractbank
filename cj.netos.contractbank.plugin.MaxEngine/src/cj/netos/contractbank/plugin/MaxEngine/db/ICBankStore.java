package cj.netos.contractbank.plugin.MaxEngine.db;

import cj.lns.chip.sos.cube.framework.ICube;

public interface ICBankStore {

	ICube bank(String bank);

	ICube home();

}