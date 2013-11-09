package org.bura.mongoapp

import spock.lang.Specification
import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network

class MongoSpecification extends Specification {
	private static MongodProcess mongod
	private static MongodExecutable mongodExe

	static final int TEST_INSTANCE_PORT = 12345

	def setupSpec() {
		MongodStarter runtime = MongodStarter.getDefaultInstance()
		mongodExe = runtime.prepare(new MongodConfigBuilder().version(Version.Main.PRODUCTION)
				.net(new Net(TEST_INSTANCE_PORT, Network.localhostIsIPv6())).build())

		int n = 0
		while (true) {
			// Just in case, if the previous instance of mongod has not had time to stop.
			try {
				mongod = mongodExe.start()
				break
			} catch (e) {
				n++
				if (n > 3) {
					throw e
				}
			}
		}
	}

	def cleanupSpec() {
		mongod.stop()
		mongodExe.stop()
	}
}
