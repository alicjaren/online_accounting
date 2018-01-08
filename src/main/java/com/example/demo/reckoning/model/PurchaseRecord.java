package com.example.demo.reckoning.model;

import com.example.demo.invoices.model.PurchaseInvoice;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "purchase_records", catalog = "test")
public class PurchaseRecord {
    private int idPurchaseRecord;
    private String name;
    private double sumNet23;
    private double sumNet8;
    private double sumNet5;
    private double sumVat23;
    private double sumVat8;
    private double sumVat5;
    private double sumGross;
    private double sumFixedAssetsNet;//TODO
    private double sumFixedAssetsVat;
    private double sumFixedAssetsGross;
    private Set<PurchaseInvoice> purchaseInvoices = new HashSet<>();


    public PurchaseRecord(){
    }

    public PurchaseRecord(String name){
        this.name = name;
        this.sumNet23 = 0;
        this.sumNet8 = 0;
        this.sumNet5 = 0;
        this.sumVat23 = 0;
        this.sumVat8 = 0;
        this.sumVat5 = 0;
        this.sumGross = 0;
    }

    public PurchaseRecord(String name, double sumNet23, double sumNet8, double sumNet5,
                          double sumVat23, double sumVat8, double sumVat5, double sumGross) {
        this.name = name;
        this.sumNet23 = sumNet23;
        this.sumNet8 = sumNet8;
        this.sumNet5 = sumNet5;
        this.sumVat23 = sumVat23;
        this.sumVat8 = sumVat8;
        this.sumVat5 = sumVat5;
        this.sumGross = sumGross;
    }

    public PurchaseRecord(String name, double sumNet23, double sumNet8, double sumNet5, double sumVat23,
                          double sumVat8, double sumVat5, double sumGross, double sumFixedAssetsNet,
                          double sumFixedAssetsVat, double sumFixedAssetsGross) {
        this.name = name;
        this.sumNet23 = sumNet23;
        this.sumNet8 = sumNet8;
        this.sumNet5 = sumNet5;
        this.sumVat23 = sumVat23;
        this.sumVat8 = sumVat8;
        this.sumVat5 = sumVat5;
        this.sumGross = sumGross;
        this.sumFixedAssetsNet = sumFixedAssetsNet;
        this.sumFixedAssetsVat = sumFixedAssetsVat;
        this.sumFixedAssetsGross = sumFixedAssetsGross;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_purchase_record", unique = true, nullable = false)
    public int getIdPurchaseRecord() {
        return idPurchaseRecord;
    }

    public void setIdPurchaseRecord(int idPurchaseRecord) {
        this.idPurchaseRecord = idPurchaseRecord;
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

    @Column(name = "sum_fixed_assets_net", nullable = false, length = 17)
    public double getSumFixedAssetsNet() {
        return sumFixedAssetsNet;
    }

    public void setSumFixedAssetsNet(double sumFixedAssetsNet) {
        this.sumFixedAssetsNet = sumFixedAssetsNet;
    }

    @Column(name = "sum_fixed_assets_vat", nullable = false, length = 17)
    public double getSumFixedAssetsVat() {
        return sumFixedAssetsVat;
    }

    public void setSumFixedAssetsVat(double sumFixedAssetsVat) {
        this.sumFixedAssetsVat = sumFixedAssetsVat;
    }

    @Column(name = "sum_fixed_assets_gross", nullable = false, length = 17)
    public double getSumFixedAssetsGross() {
        return sumFixedAssetsGross;
    }

    public void setSumFixedAssetsGross(double sumFixedAssetsGross) {
        this.sumFixedAssetsGross = sumFixedAssetsGross;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRecord")
    public Set<PurchaseInvoice> getPurchaseInvoices() {
        return purchaseInvoices;
    }

    public void setPurchaseInvoices(Set<PurchaseInvoice> purchaseInvoices) {
        this.purchaseInvoices = purchaseInvoices;
    }
}
