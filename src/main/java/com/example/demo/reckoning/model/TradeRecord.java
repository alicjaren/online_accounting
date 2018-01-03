package com.example.demo.reckoning.model;

import com.example.demo.invoices.model.TradeInvoice;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "trade_records", catalog = "test")
public class TradeRecord {

    private int idTradeRecord;
    private String name;
    private double sumNet23;
    private double sumNet8;
    private double sumNet5;
    private double sumVat23;
    private double sumVat8;
    private double sumVat5;
    private double sumGross;
    private Set<TradeInvoice> tradeInvoices = new HashSet<>();

    public TradeRecord(){

    }

    public TradeRecord(String name, double sumNet23, double sumNet8, double sumNet5, double sumVat23, double sumVat8,
                       double sumVat5, double sumGross) {
        this.name = name;
        this.sumNet23 = sumNet23;
        this.sumNet8 = sumNet8;
        this.sumNet5 = sumNet5;
        this.sumVat23 = sumVat23;
        this.sumVat8 = sumVat8;
        this.sumVat5 = sumVat5;
        this.sumGross = sumGross;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_trade_record", unique = true, nullable = false)
    public int getIdTradeRecord() {
        return idTradeRecord;
    }

    public void setIdTradeRecord(int idTradeRecord) {
        this.idTradeRecord = idTradeRecord;
    }

    @Column(name = "name", unique = true, nullable = false, length = 10)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "sum_net_23", nullable = false, length = 17)
    public double getSumNet23() {
        return sumNet23;
    }

    public void setSumNet23(double sumNet23) {
        this.sumNet23 = sumNet23;
    }

    @Column(name = "sum_net_8", nullable = false, length = 17)
    public double getSumNet8() {
        return sumNet8;
    }

    public void setSumNet8(double sumNet8) {
        this.sumNet8 = sumNet8;
    }

    @Column(name = "sum_net_5", nullable = false, length = 17)
    public double getSumNet5() {
        return sumNet5;
    }

    public void setSumNet5(double sumNet5) {
        this.sumNet5 = sumNet5;
    }

    @Column(name = "sum_vat_23", nullable = false, length = 17)
    public double getSumVat23() {
        return sumVat23;
    }

    public void setSumVat23(double sumVat23) {
        this.sumVat23 = sumVat23;
    }

    @Column(name = "sum_vat_8", nullable = false, length = 17)
    public double getSumVat8() {
        return sumVat8;
    }

    public void setSumVat8(double sumVat8) {
        this.sumVat8 = sumVat8;
    }

    @Column(name = "sum_vat_5", nullable = false, length = 17)
    public double getSumVat5() {
        return sumVat5;
    }

    public void setSumVat5(double sumVat5) {
        this.sumVat5 = sumVat5;
    }

    @Column(name = "sum_gross", nullable = false, length = 17)
    public double getSumGross() {
        return sumGross;
    }

    public void setSumGross(double sumGross) {
        this.sumGross = sumGross;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tradeRecord")
    public Set<TradeInvoice> getTradeInvoices() {
        return tradeInvoices;
    }

    public void setTradeInvoices(Set<TradeInvoice> tradeInvoices) {
        this.tradeInvoices = tradeInvoices;
    }
}
