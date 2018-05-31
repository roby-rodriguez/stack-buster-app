package com.robyrodriguez.stackbuster.transfer.stack_api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractStackItemWrapperDO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean has_more;
    private int quota_remaining;
    private List<T> items;

    public AbstractStackItemWrapperDO(){
    }

    public boolean isHas_more() {
        return this.has_more;
    }

    public void setHas_more(final boolean pHas_more) {
        has_more = pHas_more;
    }

    public int getQuota_remaining() {
        return this.quota_remaining;
    }

    public void setQuota_remaining(final int pQuota_remaining) {
        quota_remaining = pQuota_remaining;
    }

    public List<T> getItems() {
        return this.items;
    }

    public void setItems(final List<T> pItems) {
        items = pItems;
    }

    @Override
    public String toString() {
        return "AbstractStackItemWrapperDO{" + "has_more=" + has_more + ", quota_remaining=" + quota_remaining + ", items="
                + items + '}';
    }
}
