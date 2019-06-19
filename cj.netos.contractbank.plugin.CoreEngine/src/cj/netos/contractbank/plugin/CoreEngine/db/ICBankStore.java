package cj.netos.contractbank.plugin.CoreEngine.db;

import cj.lns.chip.sos.cube.framework.ICube;

public interface ICBankStore {

	ICube bank(String bank);

	ICube home();

}