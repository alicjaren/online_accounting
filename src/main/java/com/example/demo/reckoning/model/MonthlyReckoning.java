package com.example.demo.reckoning.model;


import com.example.demo.users.model.User;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "monthly_reckonings", catalog = "test")
public class MonthlyReckoning {

    private int idMonthlyReckoning;
    private String name;
    private int forNextMonth;
    private int forRevenue;
    private int refund25Days;
    private int refund60Days;
    private int refund180Days;
    private User user;
    private TradeRecord tradeRecord;
    private PurchaseRecord purchaseRecord;


    public MonthlyReckoning(){

    }

    public MonthlyReckoning(String name, int forNextMonth, int forRevenue, int refund25Days, int refund60Days,
                            int refund180Days, User user, TradeRecord tradeRecord, PurchaseRecord purchaseRecord) {
        this.name = name;
        this.forNextMonth = forNextMonth;
        this.forRevenue = forRevenue;
        this.refund25Days = refund25Days;
        this.refund60Days = refund60Days;
        this.refund180Days = refund180Days;
        this.tradeRecord = tradeRecord;
        this.purchaseRecord = purchaseRecord;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_monthly_reckoning", unique = true, nullable = false)
    public int getIdMonthlyReckoning() {
        return idMonthlyReckoning;
    }

    public void setIdMonthlyReckoning(int idMonthlyReckoning) {
        this.idMonthlyReckoning = idMonthlyReckoning;
    }

    @Column(name = "name", unique = true, nullable = false, length = 10)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "for_next_month", nullable = false, length = 30)
    public int getForNextMonth() {
        return forNextMonth;
    }

    public void setForNextMonth(int forNextMonth) {
        this.forNextMonth = forNextMonth;
    }

    @Column(name = "for_revenue", nullable = false, length = 30)
    public int getForRevenue() {
        return forRevenue;
    }

    public void setForRevenue(int forRevenue) {
        this.forRevenue = forRevenue;
    }

    @Column(name = "refund_25_days", nullable = false, length = 30)
    public int getRefund25Days() {
        return refund25Days;
    }

    public void setRefund25Days(int refund25Days) {
        this.refund25Days = refund25Days;
    }

    @Column(name = "refund_60_days", nullable = false, length = 30)
    public int getRefund60Days() {
        return refund60Days;
    }

    public void setRefund60Days(int refund60Days) {
        this.refund60Days = refund60Days;
    }

    @Column(name = "refund_180_days", nullable = false, length = 30)
    public int getRefund180Days() {
        return refund180Days;
    }

    public void setRefund180Days(int refund180Days) {
        this.refund180Days = refund180Days;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trade_record")
    public TradeRecord getTradeRecord() {
        return tradeRecord;
    }

    public void setTradeRecord(TradeRecord tradeRecord) {
        this.tradeRecord = tradeRecord;
    }


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_record")
    public PurchaseRecord getPurchaseRecord() {
        return purchaseRecord;
    }

    public void setPurchaseRecord(PurchaseRecord purchaseRecord) {
        this.purchaseRecord = purchaseRecord;
    }
}
