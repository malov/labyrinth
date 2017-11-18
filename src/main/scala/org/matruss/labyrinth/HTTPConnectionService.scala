package org.matruss.labyrinth

import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.matruss.labyrinth.config.HTTP

class HTTPConnectionService(cfg:HTTP) {
  private[this] val cm = {
    val res = new PoolingHttpClientConnectionManager
    res.setMaxTotal(cfg.totalMax)
    res.setDefaultMaxPerRoute(cfg.totalRoute)
    res
  }

  def client:CloseableHttpClient = HttpClients.custom().setConnectionManager(cm).build()
}

object HTTPConnectionService {
  def apply(cfg:HTTP):HTTPConnectionService = new HTTPConnectionService(cfg)
}
