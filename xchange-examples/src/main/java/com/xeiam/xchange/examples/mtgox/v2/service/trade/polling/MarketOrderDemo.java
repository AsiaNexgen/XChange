/**
 * Copyright (C) 2012 - 2014 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.examples.mtgox.v2.service.trade.polling;

import java.io.IOException;
import java.math.BigDecimal;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.examples.mtgox.v2.MtGoxV2ExamplesUtils;
import com.xeiam.xchange.mtgox.v2.dto.trade.polling.MtGoxGenericResponse;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxTradeServiceRaw;
import com.xeiam.xchange.service.polling.PollingTradeService;

/**
 * Test placing a market order at MtGox
 */
public class MarketOrderDemo {

  public static void main(String[] args) throws IOException {

    Exchange mtgox = MtGoxV2ExamplesUtils.createExchange();

    // Interested in the private trading functionality (authentication)
    PollingTradeService tradeService = mtgox.getPollingTradeService();
    generic(tradeService);
    raw((MtGoxTradeServiceRaw) tradeService);

  }

  private static void raw(MtGoxTradeServiceRaw tradeService) throws IOException {

    // place a market order for 1 Bitcoin at market price
    OrderType orderType = (OrderType.BID);
    BigDecimal tradeableAmount = new BigDecimal(1);
    String tradableIdentifier = "BTC";
    String transactionCurrency = "USD";

    MarketOrder marketOrder = new MarketOrder(orderType, tradeableAmount, tradableIdentifier, transactionCurrency);

    MtGoxGenericResponse orderID = tradeService.placeMtGoxMarketOrder(marketOrder);
    System.out.println("Market Order return value: " + orderID.getDataString());
  }

  private static void generic(PollingTradeService tradeService) throws IOException {

    // place a market order for 1 Bitcoin at market price
    OrderType orderType = (OrderType.BID);
    BigDecimal tradeableAmount = new BigDecimal(1);
    String tradableIdentifier = "BTC";
    String transactionCurrency = "USD";

    MarketOrder marketOrder = new MarketOrder(orderType, tradeableAmount, tradableIdentifier, transactionCurrency);

    String orderID = tradeService.placeMarketOrder(marketOrder);
    System.out.println("Market Order return value: " + orderID);
  }
}
