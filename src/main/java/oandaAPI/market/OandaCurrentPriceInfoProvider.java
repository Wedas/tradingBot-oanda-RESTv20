package oandaAPI.market;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.common.collect.Maps;

import oandaAPI.account.OandaConstants;
import oandaAPI.account.OandaJsonKeys;
import oandaAPI.util.OandaUtils;
import tradingAPI.instruments.TradeableInstrument;
import tradingAPI.market.CurrentPriceInfoProvider;
import tradingAPI.market.Price;
import tradingAPI.util.TradingConstants;
import tradingAPI.util.TradingUtils;

public class OandaCurrentPriceInfoProvider implements CurrentPriceInfoProvider<String> {

	private static final Logger LOG = Logger.getLogger(OandaCurrentPriceInfoProvider.class);

	private final String url;
	private final BasicHeader authHeader;

	public OandaCurrentPriceInfoProvider(String url, String accessToken) {
		this.url = url;
		this.authHeader = OandaUtils.createAuthHeader(accessToken);
	}

	CloseableHttpClient getHttpClient() {
		return HttpClientBuilder.create().build();
	}

	@Override
	public Map<TradeableInstrument<String>, Price<String>> getCurrentPricesForInstruments(
			Collection<TradeableInstrument<String>> instruments, String accountID) {
		StringBuilder instrumentCsv = new StringBuilder();
		boolean firstTime = true;
		for (TradeableInstrument<String> instrument : instruments) {
			if (firstTime) {
				firstTime = false;
			} else {
				instrumentCsv.append(TradingConstants.ENCODED_COMMA);
			}
			instrumentCsv.append(instrument.getInstrument());
		}

		Map<TradeableInstrument<String>, Price<String>> pricesMap = Maps.newHashMap();
		CloseableHttpClient httpClient = getHttpClient();
		try {
			HttpUriRequest httpGet = new HttpGet(this.url +OandaConstants.ACCOUNTS_RESOURCE
					+ TradingConstants.FWD_SLASH+accountID+TradingConstants.FWD_SLASH+OandaConstants.PRICES_RESOURCE
					+ "?instruments=" + instrumentCsv.toString());
			httpGet.setHeader(this.authHeader);
			httpGet.setHeader(OandaConstants.UNIX_DATETIME_HEADER);
			LOG.info(TradingUtils.executingRequestMsg(httpGet));
			HttpResponse resp = httpClient.execute(httpGet);
			String strResp = TradingUtils.responseToString(resp);
			if (strResp != StringUtils.EMPTY) {
				Object obj = JSONValue.parse(strResp);
				JSONObject jsonResp = (JSONObject) obj;
				JSONArray prices = (JSONArray) jsonResp.get(OandaJsonKeys.PRICES);
				for (Object price : prices) {
					JSONObject trade = (JSONObject) price;
					Long priceTime = Long.parseLong(trade.get(OandaJsonKeys.TIME).toString());
					TradeableInstrument<String> instrument = new TradeableInstrument<String>((String) trade
							.get(OandaJsonKeys.INSTRUMENT));
					Price<String> pi = new Price<String>(instrument, ((Number) trade.get(OandaJsonKeys.BID)).doubleValue(),
							((Number) trade.get(OandaJsonKeys.ASK)).doubleValue(), new DateTime(TradingUtils
									.toMillisFromNanos(priceTime)));
					pricesMap.put(instrument, pi);
				}
			} else {
				TradingUtils.printErrorMsg(resp);
			}
		} catch (Exception ex) {
			LOG.error(ex);
		} finally {
			TradingUtils.closeSilently(httpClient);
		}
		return pricesMap;
	}

}
