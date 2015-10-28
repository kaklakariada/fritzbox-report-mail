//
// Built on Sat Apr 06 18:45:20 CEST 2013 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.TRACE

appender("STDOUT", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
//	pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    pattern = "%d{HH:mm:ss.SSS} %-5level %logger{36} - %msg%n"
  }
}
//root(DEBUG, ["STDOUT"])
root(DEBUG, ["STDOUT"])

logger('org.hibernate', INFO)
logger('org.chris.html.jsoup.HtmlElement', debug)
