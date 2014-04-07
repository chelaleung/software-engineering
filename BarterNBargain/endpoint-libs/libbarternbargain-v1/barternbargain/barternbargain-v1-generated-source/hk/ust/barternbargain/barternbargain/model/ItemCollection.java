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
 * on 2014-04-07 at 16:44:47 UTC 
 * Modify at your own risk.
 */

package hk.ust.barternbargain.barternbargain.model;

/**
 * Model definition for ItemCollection.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the barternbargain. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class ItemCollection extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<Item> items;

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<Item> getItems() {
    return items;
  }

  /**
   * @param items items or {@code null} for none
   */
  public ItemCollection setItems(java.util.List<Item> items) {
    this.items = items;
    return this;
  }

  @Override
  public ItemCollection set(String fieldName, Object value) {
    return (ItemCollection) super.set(fieldName, value);
  }

  @Override
  public ItemCollection clone() {
    return (ItemCollection) super.clone();
  }

}
