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
package com.xeiam.xchange.bter.service.polling;

import java.math.BigDecimal;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestProxyFactory;

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.bter.BTERAdapters;
import com.xeiam.xchange.bter.BTERAuthenticated;
import com.xeiam.xchange.bter.BTERHmacPostBodyDigest;
import com.xeiam.xchange.bter.dto.marketdata.BTERAccountInfoReturn;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.service.polling.PollingAccountService;

public class BTERPollingAccountService implements PollingAccountService {

  private static final long START_MILLIS = 1356998400000L; // Jan 1st, 2013 in
  // milliseconds
  // from epoch
  private static long lastCache = 0;
  private static AccountInfo accountInfo = null;
  private ExchangeSpecification exchangeSpecification;

  private BTERAuthenticated bter;
  private ParamsDigest signatureCreator;

  /**
   * Constructor
   * 
   * @param exchangeSpecification
   *          The {@link ExchangeSpecification}
   */
  public BTERPollingAccountService(ExchangeSpecification exchangeSpecification) {

    this.exchangeSpecification = exchangeSpecification;
    this.bter = RestProxyFactory.createProxy(BTERAuthenticated.class, exchangeSpecification.getSslUri());
    this.signatureCreator = BTERHmacPostBodyDigest.createInstance(exchangeSpecification.getSecretKey());
  }

  @Override
  public AccountInfo getAccountInfo() {

    if (lastCache + 10000 > System.currentTimeMillis()) {
      return accountInfo;
    }
    BTERAccountInfoReturn info = bter.getInfo(exchangeSpecification.getApiKey(), signatureCreator, nextNonce());
    // checkResult(info);
    lastCache = System.currentTimeMillis();
    return accountInfo = BTERAdapters.adaptAccountInfo(info);
  }

  private static int nextNonce() {

    // NOTE: this nonce creation formula is not bullet-proof:
    // - It allows for only one request per .25 seconds,
    // - It will cycle over MAX_INTEGER and start producing illegal negative
    // nonces on January 5, 2030

    // If you run into problems with nonces (eg. you've once submitted a
    // large nonce and can't use normal nonces any more),
    // you can request new api credentials (key, secret) with BTCE.
    return (int) ((System.currentTimeMillis() - START_MILLIS) / 250L);
  }

  @Override
  public String withdrawFunds(BigDecimal amount, String address) {

    throw new UnsupportedOperationException("Funds withdrawal not supported by BTCE API.");
  }

  @Override
  public String requestBitcoinDepositAddress(final String... arguments) {

    throw new UnsupportedOperationException("Deposit address request not supported by BTCE API.");
  }
}
