package com.example.demo.reckoning.dao;

import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.reckoning.model.MonthlyReckoning;

public interface MonthlyReckoningDao {

    boolean isMonthlyReckoningInDBByName(String reckoningName, String userName);

    boolean addMonthlyReckoning(MonthlyReckoning monthlyReckoning);

    MonthlyReckoning getMonthlyReckoning(String userName, String reckoningName);

    String getPreviousReckoningName(String currentReckoningName);

}
