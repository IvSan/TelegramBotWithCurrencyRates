package xyz.hardliner.counselor.datacollector.datasources;

import xyz.hardliner.counselor.db.CurrencyData;

public interface DataSource {

	CurrencyData updateData(CurrencyData data);

}
