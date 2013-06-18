package com.bytedistillers.payment.sofort.gateway.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionErrors {

  private List<ErrorData> errorDataList;

  public void addErrorData(ErrorData errorData) {
    if (errorDataList == null) {
      errorDataList = new ArrayList<ErrorData>();
    }
    errorDataList.add(errorData);
  }

  public List<ErrorData> getErrorDataList() {
    return Collections.unmodifiableList(errorDataList);
  }

  public void setErrorDataList(List<ErrorData> errorData) {
    this.errorDataList = errorData;
  }
}
