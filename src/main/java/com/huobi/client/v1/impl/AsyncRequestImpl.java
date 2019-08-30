package com.huobi.client.v1.impl;

import com.huobi.client.v1.AsyncRequestClient;
import com.huobi.client.v1.AsyncResult;
import com.huobi.client.v1.ResponseCallback;
import com.huobi.client.v1.exception.HuobiApiException;
import com.huobi.client.v1.utils.FailedAsyncResult;
import com.huobi.client.v1.utils.SucceededAsyncResult;
import com.huobi.client.v1.model.Account;
import com.huobi.client.v1.model.Balance;
import com.huobi.client.v1.model.BatchCancelResult;
import com.huobi.client.v1.model.BestQuote;
import com.huobi.client.v1.model.Candlestick;
import com.huobi.client.v1.model.CompleteSubAccountInfo;
import com.huobi.client.v1.model.Deposit;
import com.huobi.client.v1.model.EtfSwapConfig;
import com.huobi.client.v1.model.EtfSwapHistory;
import com.huobi.client.v1.model.ExchangeInfo;
import com.huobi.client.v1.model.LastTradeAndBestQuote;
import com.huobi.client.v1.model.Loan;
import com.huobi.client.v1.model.MarginBalanceDetail;
import com.huobi.client.v1.model.MatchResult;
import com.huobi.client.v1.model.Order;
import com.huobi.client.v1.model.PriceDepth;
import com.huobi.client.v1.enums.EtfSwapType;
import com.huobi.client.v1.request.TransferMasterRequest;
import com.huobi.client.v1.model.Trade;
import com.huobi.client.v1.model.TradeStatistics;
import com.huobi.client.v1.model.Withdraw;
import com.huobi.client.v1.enums.AccountType;
import com.huobi.client.v1.enums.CandlestickInterval;
import com.huobi.client.v1.request.CancelOpenOrderRequest;
import com.huobi.client.v1.request.CandlestickRequest;
import com.huobi.client.v1.request.HistoricalOrdersRequest;
import com.huobi.client.v1.request.LoanOrderRequest;
import com.huobi.client.v1.request.MatchResultRequest;
import com.huobi.client.v1.request.NewOrderRequest;
import com.huobi.client.v1.request.OpenOrderRequest;
import com.huobi.client.v1.request.TransferRequest;
import com.huobi.client.v1.request.WithdrawRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AsyncRequestImpl implements AsyncRequestClient {

  private final RestApiRequestImpl requestImpl;

  AsyncRequestImpl(RestApiRequestImpl requestImpl) {
    this.requestImpl = requestImpl;
  }

  @Override
  public void getLatestCandlestick(String symbol, CandlestickInterval interval, int size,
                                   ResponseCallback<AsyncResult<List<Candlestick>>> callback) {
    RestApiInvoker
        .callASync(requestImpl.getCandlestick(symbol, interval, null, null, size), callback);
  }

  @Override
  public void getCandlestick(CandlestickRequest request,
                             ResponseCallback<AsyncResult<List<Candlestick>>> callback) {
    RestApiInvoker.callASync(requestImpl.getCandlestick(
        request.getSymbol(), request.getInterval(), request.getStartTime(), request.getEndTime(),
        request.getSize()),
        callback);
  }

  @Override
  public void getExchangeTimestamp(ResponseCallback<AsyncResult<Long>> callback) {
    RestApiInvoker.callASync(requestImpl.getExchangeTimestamp(), callback);
  }

  @Override
  public void getPriceDepth(String symbol, int size,
                            ResponseCallback<AsyncResult<PriceDepth>> callback) {
    RestApiInvoker.callASync(requestImpl.getPriceDepth(symbol, size), callback);
  }

  @Override
  public void getPriceDepth(String symbol, ResponseCallback<AsyncResult<PriceDepth>> callback) {
    RestApiInvoker.callASync(requestImpl.getPriceDepth(symbol, 20), callback);
  }

  @Override
  public void getLastTrade(String symbol, ResponseCallback<AsyncResult<Trade>> callback) {
    RestApiInvoker.callASync(requestImpl.getHistoricalTrade(symbol, null, 1), (tradeResult) -> {
      if (tradeResult.succeeded()) {
        if (tradeResult.getData() != null && tradeResult.getData().size() != 0) {
          SucceededAsyncResult<Trade> result = new SucceededAsyncResult<>(
              tradeResult.getData().get(tradeResult.getData().size() - 1));
          callback.onResponse(result);
        } else {
          callback.onResponse(new FailedAsyncResult<>(
              new HuobiApiException(HuobiApiException.RUNTIME_ERROR, "Null data")));
        }
      } else {
        callback.onResponse(new FailedAsyncResult<>(tradeResult.getException()));
      }
    });
  }

  @Override
  public void getHistoricalTrade(String symbol, int size,
                                 ResponseCallback<AsyncResult<List<Trade>>> callback) {
    RestApiInvoker.callASync(requestImpl.getHistoricalTrade(symbol, null, size), callback);
  }

  @Override
  public void get24HTradeStatistics(String symbol,
                                    ResponseCallback<AsyncResult<TradeStatistics>> callback) {
    RestApiInvoker.callASync(requestImpl.get24HTradeStatistics(symbol), callback);
  }

  @Override
  public void getExchangeInfo(ResponseCallback<AsyncResult<ExchangeInfo>> callback) {
    RestApiInvoker.callASync(requestImpl.getSymbols(), (symbolsResult) -> {
      if (symbolsResult.succeeded()) {
        List<String> currencies = RestApiInvoker.callSync(requestImpl.getCurrencies());
        ExchangeInfo exchangeInfo = new ExchangeInfo();
        exchangeInfo.setSymbolList(symbolsResult.getData());
        exchangeInfo.setCurrencies(currencies);
        callback.onResponse(new SucceededAsyncResult<>(exchangeInfo));
      } else {
        callback.onResponse(new FailedAsyncResult<>(symbolsResult.getException()));
      }
    });
  }

  @Override
  public void getBestQuote(String symbol, ResponseCallback<AsyncResult<BestQuote>> callback) {
    RestApiInvoker.callASync(requestImpl.getBestQuote(symbol), callback);
  }

  @Override
  public void getWithdrawHistory(String currency, long fromId, int size,
                                 ResponseCallback<AsyncResult<List<Withdraw>>> callback) {
    RestApiInvoker.callASync(requestImpl.getWithdrawHistory(currency, fromId, size), callback);
  }

  @Override
  public void getDepositHistory(String currency, long fromId, int size,
                                ResponseCallback<AsyncResult<List<Deposit>>> callback) {
    RestApiInvoker.callASync(requestImpl.getDepositHistory(currency, fromId, size), callback);
  }

  @Override
  public void transfer(TransferRequest transferRequest,
                       ResponseCallback<AsyncResult<Long>> callback) {
    RestApiInvoker.callASync(requestImpl.transfer(transferRequest), callback);
  }

  @Override
  public void applyLoan(String symbol, String currency, BigDecimal amount,
                        ResponseCallback<AsyncResult<Long>> callback) {
    RestApiInvoker.callASync(requestImpl.applyLoan(symbol, currency, amount), callback);
  }

  @Override
  public void repayLoan(long loadId, BigDecimal amount,
                        ResponseCallback<AsyncResult<Long>> callback) {
    RestApiInvoker.callASync(requestImpl.repayLoan(loadId, amount), callback);
  }

  @Override
  public void getLoanHistory(LoanOrderRequest loanOrderRequest,
                             ResponseCallback<AsyncResult<List<Loan>>> callback) {
    RestApiInvoker.callASync(requestImpl.getLoan(loanOrderRequest), callback);
  }

  @Override
  public void getLastTradeAndBestQuote(String symbol,
                                       ResponseCallback<AsyncResult<LastTradeAndBestQuote>> callback) {
    RestApiInvoker.callASync(requestImpl.getBestQuote(symbol), (bestQuoteResult) -> {
      if (bestQuoteResult.succeeded()) {
        LastTradeAndBestQuote lastTradeAndBestQuote = new LastTradeAndBestQuote();
        lastTradeAndBestQuote.setBidAmount(bestQuoteResult.getData().getBidAmount());
        lastTradeAndBestQuote.setBidPrice(bestQuoteResult.getData().getBidPrice());
        lastTradeAndBestQuote.setAskAmount(bestQuoteResult.getData().getAskAmount());
        lastTradeAndBestQuote.setAskPrice(bestQuoteResult.getData().getAskPrice());
        getLastTrade(symbol, (tradeResult) -> {
          if (tradeResult.succeeded()) {
            lastTradeAndBestQuote.setLastTradePrice(tradeResult.getData().getPrice());
            lastTradeAndBestQuote.setLastTradeAmount(tradeResult.getData().getAmount());
          } else {
            callback.onResponse(new FailedAsyncResult<>(tradeResult.getException()));
          }
        });
      } else {
        callback.onResponse(new FailedAsyncResult<>(bestQuoteResult.getException()));
      }
    });
  }

  @Override
  public void getAccountBalance(ResponseCallback<AsyncResult<List<Account>>> callback) {
    RestApiInvoker.callASync(requestImpl.getAccounts(), (accountResult) -> {
      if (accountResult.succeeded()) {
        List<Account> accountList = accountResult.getData();
        for (Account account : accountList) {
          List<Balance> balances = RestApiInvoker.callSync(requestImpl.getBalance(account));
          account.setBalances(balances);
        }
        callback.onResponse(new SucceededAsyncResult<>(accountList));
      } else {
        callback.onResponse(new FailedAsyncResult<>(accountResult.getException()));
      }
    });
  }

  @Override
  public void getAccountBalance(AccountType accountType,
                                ResponseCallback<AsyncResult<Account>> callback) {
    RestApiInvoker.callASync(requestImpl.getAccounts(), (accountResult) -> {
      if (accountResult.succeeded()) {
        for (Account account : accountResult.getData()) {
          if (account.getType() == accountType) {
            List<Balance> balances = RestApiInvoker.callSync(requestImpl.getBalance(account));
            account.setBalances(balances);
            callback.onResponse(new SucceededAsyncResult<>(account));
            break;
          }
        }
      } else {
        callback.onResponse(new FailedAsyncResult<>(accountResult.getException()));
      }
    });
  }

  @Override
  public void getTickers(ResponseCallback<AsyncResult<Map<String, TradeStatistics>>> callback) {
    RestApiInvoker.callASync(requestImpl.getTickers(), callback);
  }

  @Override
  public void createOrder(NewOrderRequest newOrderRequest,
                          ResponseCallback<AsyncResult<Long>> callback) {
    RestApiInvoker.callASync(requestImpl.createOrder(newOrderRequest), callback);
  }

  @Override
  public void getOpenOrders(OpenOrderRequest openOrderRequest,
                            ResponseCallback<AsyncResult<List<Order>>> callback) {
    RestApiInvoker.callASync(requestImpl.getOpenOrders(openOrderRequest), callback);
  }

  @Override
  public void cancelOrder(String symbol, long orderId,
                          ResponseCallback<AsyncResult<Void>> callback) {
    RestApiInvoker.callASync(requestImpl.cancelOrder(symbol, orderId), callback);
  }

  @Override
  public void cancelOrders(String symbol, List<Long> orderIds,
                           ResponseCallback<AsyncResult<Void>> callback) {
    RestApiInvoker.callASync(requestImpl.cancelOrders(symbol, orderIds), callback);
  }

  @Override
  public void cancelOpenOrders(CancelOpenOrderRequest request,
                               ResponseCallback<AsyncResult<BatchCancelResult>> callback) {
    RestApiInvoker.callASync(requestImpl.cancelOpenOrders(request), callback);
  }

  @Override
  public void getOrder(String symbol, long orderId, ResponseCallback<AsyncResult<Order>> callback) {
    RestApiInvoker.callASync(requestImpl.getOrder(symbol, orderId), callback);
  }

  @Override
  public void getMatchResults(String symbol, long orderId,
                              ResponseCallback<AsyncResult<List<MatchResult>>> callback) {
    RestApiInvoker.callASync(requestImpl.getMatchResults(symbol, orderId), callback);
  }

  @Override
  public void getMatchResults(MatchResultRequest matchResultRequest,
                              ResponseCallback<AsyncResult<List<MatchResult>>> callback) {
    RestApiInvoker.callASync(requestImpl.getMatchResults(matchResultRequest), callback);
  }

  @Override
  public void withdraw(WithdrawRequest withdrawRequest,
                       ResponseCallback<AsyncResult<Long>> callback) {
    RestApiInvoker.callASync(requestImpl.withdraw(withdrawRequest), callback);
  }

  @Override
  public void cancelWithdraw(String currency, long withdrawId,
                             ResponseCallback<AsyncResult<Void>> callback) {
    RestApiInvoker.callASync(requestImpl.cancelWithdraw(currency, withdrawId), callback);
  }

  @Override
  public void getHistoricalOrders(HistoricalOrdersRequest req,
                                  ResponseCallback<AsyncResult<List<Order>>> callback) {
    RestApiInvoker.callASync(requestImpl.getHistoricalOrders(req), callback);
  }

  @Override
  public void transferBetweenParentAndSub(TransferMasterRequest req,
                                          ResponseCallback<AsyncResult<Long>> callback) {
    RestApiInvoker.callASync(requestImpl.transferBetweenParentAndSub(req), callback);
  }

  @Override
  public void getCurrentUserAggregatedBalance(ResponseCallback<AsyncResult<List<Balance>>> callback) {
    RestApiInvoker.callASync(requestImpl.getCurrentUserAggregatedBalance(), callback);
  }

  @Override
  public void getSpecifyAccountBalance(long subId,
      ResponseCallback<AsyncResult<List<CompleteSubAccountInfo>>> callback) {
    RestApiInvoker.callASync(requestImpl.getSpecifyAccountBalance(subId), callback);
  }

  @Override
  public void getEtfSwapConfig(String etfSymbol,
                               ResponseCallback<AsyncResult<EtfSwapConfig>> callback) {
    RestApiInvoker.callASync(requestImpl.getEtfSwapConfig(etfSymbol), callback);
  }

  @Override
  public void etfSwap(String etfSymbol, int amount, EtfSwapType swapType,
                      ResponseCallback<AsyncResult<Void>> callback) {
    RestApiInvoker.callASync(requestImpl.etfSwap(etfSymbol, amount, swapType), callback);
  }

  @Override
  public void getEtfSwapHistory(String etfSymbol, int offset, int size,
                                ResponseCallback<AsyncResult<List<EtfSwapHistory>>> callback) {
    RestApiInvoker.callASync(requestImpl.getEtfSwapHistory(etfSymbol, offset, size), callback);
  }

  @Override
  public void getEtfCandlestick(String etfSymbol, CandlestickInterval interval, Integer limit,
                                ResponseCallback<AsyncResult<List<Candlestick>>> callback) {
    RestApiInvoker.callASync(requestImpl.getETFCandlestick(etfSymbol, interval, limit), callback);
  }

  @Override
  public void getMarginBalanceDetail(
      String symbol, ResponseCallback<AsyncResult<List<MarginBalanceDetail>>> callback) {
    RestApiInvoker.callASync(requestImpl.getMarginBalanceDetail(symbol), callback);
  }
}