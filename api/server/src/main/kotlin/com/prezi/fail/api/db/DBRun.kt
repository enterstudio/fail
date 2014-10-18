package com.prezi.fail.api.db

import com.amazonaws.services.dynamodbv2.datamodeling.*

import com.prezi.fail.api.extensions.getAtMillis
import com.prezi.fail.api.extensions.setAtMillis
import com.prezi.fail.api.Run
import com.prezi.fail.api.RunStatus
import com.linkedin.data.template.GetMode

// If anyone has infinite time, this could be generated.

[DynamoDBTable(tableName = "fail_Run")]
class DBRun([DynamoDBIgnore] val model: Run = Run()) {
    [DynamoDBAutoGeneratedKey]
    [DynamoDBHashKey(attributeName = "Id")]
    public fun getId(): String? = model.getId(GetMode.NULL)
    public fun setId(v: String?): DBRun? { model.setId(v); return this }

    [DynamoDBAttribute(attributeName = "At")]
    [DynamoDBIndexRangeKey(globalSecondaryIndexName = "ScheduledFailureId-At-index")]
    public fun getAt(): Long? = model.getAt()
    public fun setAt(v: Long?): DBRun? { model.setAt(v); return this }

    [DynamoDBIgnore]
    public fun getAtMillis(): Long? = model.getAtMillis()
    public fun setAtMillis(v: Long?): DBRun? { model.setAtMillis(v); return this }

    [DynamoDBAttribute(attributeName = "Status")]
    [DynamoDBMarshalling(marshallerClass = javaClass<EnumMarshaller>())]
    public fun getStatus(): RunStatus? = model.getStatus()
    public fun setStatus(v: RunStatus?): DBRun? { model.setStatus(v); return this }

    [DynamoDBAttribute(attributeName = "Log")]
    public fun getLog(): String? = model.getLog()
    public fun setLog(v: String?): DBRun? { model.setLog(v); return this }

    protected var _scheduledFailureId: String? = null

    [DynamoDBAttribute(attributeName = "ScheduledFailureId")]
    [DynamoDBIndexHashKey(globalSecondaryIndexName = "ScheduledFailureId-At-index")]
    public fun getScheduledFailureId(): String? = _scheduledFailureId
    public fun setScheduledFailureId(v: String?): DBRun? { _scheduledFailureId = v; return this }

    [DynamoDBIgnore]
    public fun setScheduledFailure(f: DBScheduledFailure): DBRun? = setScheduledFailureId(f.getId())
    public fun getScheduledFailure(mapper: DynamoDBMapper): DBScheduledFailure? =
            mapper.load(javaClass<DBScheduledFailure>(), _scheduledFailureId)

    public override fun toString(): String = model.toString()
}
