package com.prezi.fail.api.db

import com.prezi.fail.api.ScheduledFailure
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling


// If anyone has infinite time, this could be generated.

[DynamoDBTable(tableName = "fail_ScheduledFailure")]
class DBScheduledFailure([DynamoDBIgnore] val model: ScheduledFailure = ScheduledFailure()) {
    protected var _id: String? = null

    [DynamoDBAutoGeneratedKey]
    [DynamoDBHashKey(attributeName = "Id")]
    public fun getId(): String? = _id
    public fun setId(v: String?): DBScheduledFailure? { _id = v; return this}

    [DynamoDBAttribute(attributeName = "Period")]
    public fun getPeriod(): String? = model.getPeriod()
    public fun setPeriod(v: String?): DBScheduledFailure? { model.setPeriod(v); return this }

    [DynamoDBAttribute(attributeName = "SearchTerm")]
    public fun getSearchTerm(): String? = model.getSearchTerm()
    public fun setSearchTerm(v: String?): DBScheduledFailure? { model.setSearchTerm(v); return this }

    [DynamoDBAttribute(attributeName = "Sapper")]
    public fun getSapper(): String? = model.getSapper()
    public fun setSapper(v: String?): DBScheduledFailure? { model.setSapper(v); return this }

    [DynamoDBAttribute(attributeName = "Duration")]
    public fun getDuration(): Int? = model.getDuration()
    public fun setDuration(v: Int?): DBScheduledFailure? { model.setDuration(v); return this }

    [DynamoDBAttribute(attributeName = "SapperArgs")]
    [DynamoDBMarshalling(marshallerClass = javaClass<StringArrayMarshaller>())]
    public fun getSapperArgs(): StringArray? = model.getSapperArgs()
    public fun setSapperArgs(v: StringArray?): DBScheduledFailure? { model.setSapperArgs(v); return this }

    [DynamoDBAttribute(attributeName = "ScheduledAt")]
    public fun getScheduledAt(): Long? = model.getScheduledAt()
    public fun setScheduledAt(v: Long?): DBScheduledFailure? { model.setScheduledAt(v); return this }

    [DynamoDBAttribute(attributeName = "ScheduledBy")]
    public fun getScheduledBy(): String? = model.getScheduledBy()
    public fun setScheduledBy(v: String?): DBScheduledFailure? { model.setScheduledBy(v); return this }

    [DynamoDBAttribute(attributeName = "Configuration")]
    [DynamoDBMarshalling(marshallerClass = javaClass<StringMapMarshaller>())]
    public fun getConfiguration(): StringMap? = model.getConfiguration()
    public fun setConfiguration(v: StringMap?): DBScheduledFailure? { model.setConfiguration(v); return this }

    public override fun toString(): String = "${_id}${model}"
}
