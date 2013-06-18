package com.bytedistillers.payment.wirecard.qpay;

public enum QpayPaymentType {
  //@formatter:off
  SELECT("Zahlungsmittel wird auf der Bezahlseite ausgewählt"),
  CCARD("Kreditkarte (inklusive 'Verified by Visa' und 'MasterCard SecureCode'"),
  CCARD_MOTO("Kreditkarte (exklusive 'Verified by Visa' und 'MasterCard SecureCode')"),
  MAESTRO("Maestro SecureCode"),
  PBX("Paybox - Bezahlen per Handy"),
  PSC("Paysafecard / Cash-Ticket"),
  EPS("EPS-Onlineüberweisung"),
  ELV("Elektronisches Lastschriftverfahren"),
  QUICK("@Quick"),
  IDL("iDEAL"),
  GIROPAY("giropay"),
  PAYPAL("PayPal"),
  SOFORTUEBERWEISUNG("sofortüberweisung"),
  C2P("CLICK2PAY"),
  BANCONTACT_MISTERCASH("Bancontact/MisterCash"),
  INVOICE("Kauf auf Rechnung"),
  INSTALLMENT("Kauf auf Raten"),
  P24("Przelewy24"),
  MONETA("Moneta.ru"),
  POLI("POLi"),
  EKONTO("eKonto"),
  INSTANTBANK("Instant Bank");
  //@formatter:on

  private String description;

  private QpayPaymentType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
