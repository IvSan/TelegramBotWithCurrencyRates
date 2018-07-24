package xyz.hardliner.counselor.datacollector.datasources;

import xyz.hardliner.counselor.domain.CurrencyData;

public interface DataSource {

	CurrencyData updateData(CurrencyData data);

}
