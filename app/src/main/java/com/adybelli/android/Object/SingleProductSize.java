package com.adybelli.android.Object;

public class SingleProductSize {
    private String label;
    private int s_id;
    private int priority;
    private Integer stockQuantity;

    public SingleProductSize(String label, int s_id, int priority, Integer stockQuantity) {
        this.label = label;
        this.s_id = s_id;
        this.priority = priority;
        this.stockQuantity = stockQuantity;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
