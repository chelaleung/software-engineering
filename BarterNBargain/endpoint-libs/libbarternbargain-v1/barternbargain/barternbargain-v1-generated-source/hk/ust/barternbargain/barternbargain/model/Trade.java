/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-04-01 18:14:47 UTC)
 * on 2014-04-05 at 17:20:13 UTC 
 * Modify at your own risk.
 */

package hk.ust.barternbargain.barternbargain.model;

/**
 * Model definition for Trade.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the barternbargain. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Trade extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String buyerId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long itemId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime postingTime;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String sellerId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String status;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getBuyerId() {
    return buyerId;
  }

  /**
   * @param buyerId buyerId or {@code null} for none
   */
  public Trade setBuyerId(java.lang.String buyerId) {
    this.buyerId = buyerId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public Trade setId(java.lang.Long id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getItemId() {
    return itemId;
  }

  /**
   * @param itemId itemId or {@code null} for none
   */
  public Trade setItemId(java.lang.Long itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getPostingTime() {
    return postingTime;
  }

  /**
   * @param postingTime postingTime or {@code null} for none
   */
  public Trade setPostingTime(com.google.api.client.util.DateTime postingTime) {
    this.postingTime = postingTime;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getSellerId() {
    return sellerId;
  }

  /**
   * @param sellerId sellerId or {@code null} for none
   */
  public Trade setSellerId(java.lang.String sellerId) {
    this.sellerId = sellerId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getStatus() {
    return status;
  }

  /**
   * @param status status or {@code null} for none
   */
  public Trade setStatus(java.lang.String status) {
    this.status = status;
    return this;
  }

  @Override
  public Trade set(String fieldName, Object value) {
    return (Trade) super.set(fieldName, value);
  }

  @Override
  public Trade clone() {
    return (Trade) super.clone();
  }

}
