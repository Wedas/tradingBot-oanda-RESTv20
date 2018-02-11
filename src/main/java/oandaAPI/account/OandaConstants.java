package oandaAPI.account;

import org.apache.http.message.BasicHeader;

import tradingAPI.util.TradingConstants;


public final class OandaConstants {
	private OandaConstants() {
	}

	
	public static final String		PRICES_RESOURCE			= "/pricing/stream";
	public static final String		EVENTS_RESOURCE			= "/v3/events";
	public static final String		INSTRUMENTS_RESOURCE	= "/instruments";
	public static final String		CANDLES_RESOURCE		= "/v3/candles";
	public static final String		ACCOUNTS_RESOURCE		= "/v3/accounts";
	
	public static final double		LOT_SIZE				= 10000.00;
	public static final String		ORDER_MARKET			= "market";
	public static final String		ORDER_LIMIT				= "limit";
	public static final String		ORDER_MARKET_IF_TOUCHED	= "marketIfTouched";
	
	public static final BasicHeader	UNIX_DATETIME_HEADER	= new BasicHeader("X-Accept-Datetime-Format", "UNIX");
	public static final String		CCY_PAIR_SEP			= TradingConstants.CURRENCY_PAIR_SEP_UNDERSCORE;
	public static final String		CCY_PAIR_SEP2			= "/";
	public static final String		BUY						= "buy";
	public static final String		SELL					= "sell";
	public static final String		NONE					= "none";
	public static final String		INTEREST				= "Interest";
	public static final String		BUY_MKT					= "Buy Market";
	public static final String		SELL_MKT				= "Sell Market";
	public static final String		STOP_LOSS				= "Stop Loss";
	public static final String		TAKE_PROFIT				= "Take Profit";
	public static final String		CHANGE_TRADE			= "Change Trade";
	public static final String		CLOSE_TRADE				= "Close Trade";
	public static final String		TRAILING_STOP			= "Trailing Stop";
	public static final String		DAILYFX_CALENDAR_URL	= "http://www.dailyfx.com/files/Calendar-%tm-%td-%tY.csv";
}