package tradingAPI.account;

import java.util.Collection;

public interface AccountDataProvider <T>{
	Account<T> getLatestAccountInfo(T accountId);
	Collection <Account<T>> getLatestAccountsInfo();
}
