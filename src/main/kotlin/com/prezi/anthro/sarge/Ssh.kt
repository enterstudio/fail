package com.prezi.anthro.sarge

import org.slf4j.LoggerFactory

import com.jcraft.jsch.JSch
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.agentproxy.RemoteIdentityRepository
import com.jcraft.jsch.Session
import com.jcraft.jsch.agentproxy.ConnectorFactory

import com.prezi.anthro.inHome
import kotlin.properties.Delegates
import java.io.InputStream
import java.io.OutputStream
import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.SftpProgressMonitor


class Ssh(val host: String, val config: SshConfig = SshConfig()) {
    val logger = LoggerFactory.getLogger(this.javaClass)!!
    val session: Session by Delegates.lazy {
        val jsch = JSch()
        val session = jsch.getSession(host)!!
        if (config.shouldDisableHostKeyChecking() ) session.setConfig("StrictHostKeyChecking", "no")
        if (config.getAuthType() == AuthType.SSH_AGENT ) useSshAgent(jsch, session)
        session.connect()
        session
    }

    fun useSshAgent(jsch: JSch, session: Session) {
        logger.debug("using ssh-agent for authentication")
        session.setConfig("PreferredAuthentications", "publickey")
        val sshAgentConnector = ConnectorFactory.getDefault()?.createConnector()
        val sshAgentIdentityRepository = RemoteIdentityRepository(sshAgentConnector)
        jsch.setIdentityRepository(sshAgentIdentityRepository)
    }

    fun overChannel<T : Channel>(channelName: String, beforeConnect: ((T) -> Unit)?, f: (T) -> Unit): Ssh {
        val channel = session.openChannel(channelName) as T
        if (beforeConnect != null) beforeConnect(channel)
        channel.connect()
        f(channel)
        channel.disconnect()
        return this
    }

    fun overExecChannel(cmd: String, f: (InputStream, OutputStream, ChannelExec) -> Unit): Ssh =
            overChannel<ChannelExec>(
                    "exec",
                    {c -> c.setCommand(cmd)},
                    {c -> f(c.getInputStream()!!, c.getOutputStream()!!, c)})

    fun overSftpChannel(f: (ChannelSftp) -> Unit): Ssh = overChannel("sftp", null, f)

    fun exec(cmd: String): Ssh {
        logger.info("executing on ${host}: ${cmd}")
        return overExecChannel(cmd, { input, output, channel ->
            val reader = input.buffered().reader()
            reader.forEachLine { line -> logger.info("response line: ${line}") }
        })
    }

    fun put(src: String, dst: String, monitor: SftpProgressMonitor?): Ssh {
        logger.info("uploading ${src} to ${host}:${dst}")
        return overSftpChannel({ channel ->
            channel.put(src, dst, monitor)
        })
    }

    fun close() {
        session.disconnect()
    }
}
