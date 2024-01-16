package de.rauschdo.eschistory.utility.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import de.rauschdo.eschistory.ContestsProto
import java.io.InputStream
import java.io.OutputStream

object ContestItemsSerializer : Serializer<ContestsProto> {

    override val defaultValue: ContestsProto = ContestsProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ContestsProto {
        try {
            return ContestsProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: ContestsProto,
        output: OutputStream
    ) = t.writeTo(output)
}