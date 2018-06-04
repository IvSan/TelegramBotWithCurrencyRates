package xyz.hardliner.counselor.datacollector.datasources;

import xyz.hardliner.counselor.datacollector.CurrencyData;

public interface DataSource {

	CurrencyData updateData(CurrencyData data);

}
