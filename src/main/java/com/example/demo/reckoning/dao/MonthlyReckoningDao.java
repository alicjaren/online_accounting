package com.example.demo.reckoning.dao;

import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.reckoning.model.MonthlyReckoning;

public interface MonthlyReckoningDao {

    boolean isMonthlyReckoningInDBByName(String name);

    boolean addMonthlyReckoning(MonthlyReckoning monthlyReckoning);

}
