/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

import java.net.http.HttpRequest
import java.net.http.HttpResponse

class JdkHttpClientAsyncTest extends JdkHttpClientTest {

  @Override
  HttpResponse send(HttpRequest request) {
    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get()
  }
}
