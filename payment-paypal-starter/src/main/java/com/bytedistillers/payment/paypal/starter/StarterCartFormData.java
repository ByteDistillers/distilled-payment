package com.bytedistillers.payment.paypal.starter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StarterCartFormData extends FormData {

  private String cmd = "_cart";
  private String upload = "1";

  private List<CartItem> items;

  public void setItems(List<CartItem> items) {
    this.items = items;
  }

  public void addItems(List<CartItem> items) {
    if (items == null) {
      items = new ArrayList<CartItem>();
    }
    this.items.addAll(items);
  }

  public void addItem(CartItem item) {
    if (items == null) {
      items = new ArrayList<CartItem>();
    }
    items.add(item);
  }

  public List<CartItem> getItems() {
    List<CartItem> result;
    if (items != null) {
      result = Collections.unmodifiableList(items);
    } else {
      result = Collections.EMPTY_LIST;
    }
    return result;
  }

  public void removeItemByName(String name) {
  }

  public void removeItemByNumber(String number) {
  }

  public void removeItemByIndex(int index) {
  }
}
