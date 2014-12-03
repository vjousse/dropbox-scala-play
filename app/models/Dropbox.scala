package models

import java.security.SecureRandom
import com.dropbox.core.util.StringUtil

object Dropbox {
  def generateCsrf(): String = {
    var r: SecureRandom = new SecureRandom();
    var csrfRaw: Array[Byte] = new Array[Byte](16);
    r.nextBytes(csrfRaw);

    StringUtil.urlSafeBase64Encode(csrfRaw);
  }
}
