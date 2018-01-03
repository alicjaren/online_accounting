package com.example.demo.invoices.model;


import com.example.demo.reckoning.model.TradeRecord;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "trade_invoices", catalog = "test")
public class TradeInvoice {

    private int idTradeInvoice;
    private String invoiceNumber;
    private Date dateOfIssue;
    private long tradePartnerNIP;
    private String tradePartnerName;
    private String dealingThingName;
    private double net23;
    private double net8;
    private double net5;
    private double vat23;
    private double vat8;
    private double vat5;
    private double gross;
    private LocalDateTime dateCreated;
    private LocalDateTime dateChanged;
    private TradeRecord tradeRecord;


    public TradeInvoice() {
    }

    public TradeInvoice(String invoiceNumber, Date dateOfIssue, long tradePartnerNIP,
                        String tradePartnerName, String dealingThingName, double net23, double net8, double net5,
                        double vat23, double vat8, double vat5, double gross, TradeRecord tradeRecord) {
        this.invoiceNumber = invoiceNumber;
        this.dateOfIssue = dateOfIssue;
        this.tradePartnerNIP = tradePartnerNIP;
        this.tradePartnerName = tradePartnerName;
        this.dealingThingName = dealingThingName;
        this.net23 = net23;
        this.net8 = net8;
        this.net5 = net5;
        this.vat23 = vat23;
        this.vat8 = vat8;
        this.vat5 = vat5;
        this.gross = gross;
        this.tradeRecord = tradeRecord;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_trade_invoices", unique = true, nullable = false)
    public int getIdTradeInvoice() {
        return idTradeInvoice;
    }

    public void setIdTradeInvoice(int idTradeInvoice) {
        this.idTradeInvoice = idTradeInvoice;
    }

    @Column(name = "invoice_number", nullable = false, length = 10)
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @Column(name = "date_of_issue", nullable = false)
    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    @Column(name = "trade_partner_NIP", nullable = false, length = 10)
    public long getTradePartnerNIP() {
        return tradePartnerNIP;
    }

    public void setTradePartnerNIP(long tradePartnerNIP) {
        this.tradePartnerNIP = tradePartnerNIP;
    }

    @Column(name = "trade_partner_name", nullable = false, length = 100)
    public String getTradePartnerName() {
        return tradePartnerName;
    }

    public void setTradePartnerName(String tradePartnerName) {
        this.tradePartnerName = tradePartnerName;
    }

    @Column(name = "dealing_thing_name", nullable = false, length = 100)
    public String getDealingThingName() {
        return dealingThingName;
    }

    public void setDealingThingName(String dealingThingName) {
        this.dealingThingName = dealingThingName;
    }

    @Column(name = "net_23", nullable = false)
    public double getNet23() {
        return net23;
    }

    public void setNet23(double net23) {
        this.net23 = net23;
    }

    @Column(name = "net_8", nullable = false)
    public double getNet8() {
        return net8;
    }

    public void setNet8(double net8) {
        this.net8 = net8;
    }

    @Column(name = "net_5", nullable = false)
    public double getNet5() {
        return net5;
    }

    public void setNet5(double net5) {
        this.net5 = net5;
    }

    @Column(name = "vat_23", nullable = false)
    public double getVat23() {
        return vat23;
    }

    public void setVat23(double vat23) {
        this.vat23 = vat23;
    }

    @Column(name = "vat_8", nullable = false)
    public double getVat8() {
        return vat8;
    }

    public void setVat8(double vat8) {
        this.vat8 = vat8;
    }

    @Column(name = "vat_5", nullable = false)
    public double getVat5() {
        return vat5;
    }

    public void setVat5(double vat5) {
        this.vat5 = vat5;
    }

    @Column(name = "gross", nullable = false)
    public double getGross() {
        return gross;
    }

    public void setGross(double gross) {
        this.gross = gross;
    }

    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

   // @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_changed")
    public LocalDateTime getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(LocalDateTime dateChanged) {
        this.dateChanged = dateChanged;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_record", nullable = false)
    public TradeRecord getTradeRecord() {
        return tradeRecord;
    }

    public void setTradeRecord(TradeRecord tradeRecord) {
        this.tradeRecord = tradeRecord;
    }
}