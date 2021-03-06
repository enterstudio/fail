package com.prezi.fail.api.cli

import com.linkedin.data.template.StringMap
import com.prezi.fail.cli.Action
import org.slf4j.LoggerFactory
import com.prezi.fail.api.db.DBScheduledFailure
import com.prezi.fail.api.db.DB
import com.prezi.fail.api.db.DBRun
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.prezi.fail.config.FailConfig
import com.prezi.fail.api.db.Flag

public class ActionUnschedule(val args: Array<String>, systemProperties: StringMap) : Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val config = FailConfig().withConfigMap(systemProperties) as FailConfig

    class object {
        val requiredArgCount = 1
        val verb = "unschedule"
        val cmdLineSyntax = "${verb} id"
    }

    override public fun run() {
        val config = FailConfig()

        val id = args[0]
        logger.debug("Handling delete request for runs of ScheduledFailure ${id}")

        val db = DB()

        if (Flag.PANIC.get(db.mapper)) {
            logger.warn("NOTE: Panic mode is engaged, no runs are being scheduled.")
        }

        val dbScheduledFailure = db.mapper.load(javaClass<DBScheduledFailure>(), id)
        if (dbScheduledFailure == null) {
            logger.warn("No scheduled failure found with id ${id}. You can use `${ActionList.cmdLineSyntax}` to find the IDs")
            return
        }

        val runs = db.mapper.query(
                javaClass<DBRun>(),
                DynamoDBQueryExpression<DBRun>()
                    .withIndexName("ScheduledFailureId-At-index")
                    .withHashKeyValues(DBRun().setScheduledFailureId(id)?.setId("this-is-unused"))
                    .withConsistentRead(false)  // Consistent reads are not supported on global secondary indexes
        )
        logger.debug("Found ${runs.size} runs")

        if (config.isDryRun()) {
            logger.info("Collected schedule and ${runs.size} runs, not deleting anything because this is a dry-run")
        } else {
            db.mapper.batchDelete(runs)
            logger.info("Deleted ${runs.size} runs")

            db.mapper.delete(dbScheduledFailure)
            logger.info("Deleted schedule: ${dbScheduledFailure}")
        }
    }
}
