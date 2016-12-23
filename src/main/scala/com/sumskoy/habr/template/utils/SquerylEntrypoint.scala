package com.sumskoy.habr.template.utils

import org.squeryl._
import org.squeryl.dsl._
import org.joda.time._
import java.sql.Timestamp

object SquerylEntrypoint extends PrimitiveTypeMode {


  implicit val jodaTimeTEF = new NonPrimitiveJdbcMapper[Timestamp, DateTime, TTimestamp](timestampTEF, this) {

    def convertFromJdbc(t: Timestamp) = new DateTime(t)
    def convertToJdbc(t: DateTime) = new Timestamp(t.getMillis)
  }

  implicit val optionJodaTimeTEF =
    new TypedExpressionFactory[Option[DateTime], TOptionTimestamp]
      with DeOptionizer[Timestamp, DateTime, TTimestamp, Option[DateTime], TOptionTimestamp] {

      val deOptionizer = jodaTimeTEF
    }

  implicit def jodaTimeToTE(s: DateTime) = jodaTimeTEF.create(s)

  implicit def optionJodaTimeToTE(s: Option[DateTime]) = optionJodaTimeTEF.create(s)
}
