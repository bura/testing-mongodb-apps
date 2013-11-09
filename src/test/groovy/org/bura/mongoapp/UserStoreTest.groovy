package org.bura.mongoapp

import com.gmongo.GMongo
import com.gmongo.GMongoClient
import com.mongodb.DB

class UserStoreTest extends MongoSpecification {
	private GMongo mng
	private DB db

	def setup() {
		mng = new GMongoClient('localhost', TEST_INSTANCE_PORT)
		db = mng.getDB('test')
	}

	def cleanup() {
		db.dropDatabase()
		mng.close()
	}

	def testCreate() {
		given:
		String userId

		when:
		userId = new UserStore(db).create('Nyota', '12345', 1)
		then:
		db.users.count() == 1
		with(db.users.findOne()) {
			_id.toString() == userId
			username == 'Nyota'
			passwordHash == '12345'
			gender == 1
		}
	}

	def testFindByUsername() {
		given:
		db.users << [username: 'McCoy', passwordHash: 'zxcv', gender: 2]
		db.users << [username: 'Chekov', passwordHash: 'qaz', gender: 2]
		def user

		when:
		user = new UserStore(db).findByUsername('McCoy')
		then:
		with(user) {
			_id != null
			username == 'McCoy'
			passwordHash == 'zxcv'
			gender == 2
		}

		when:
		user = new UserStore(db).findByUsername('Scotty')
		then:
		user == null
	}

	def testGetAll() {
		given:
		db.users << [username: 'McCoy', passwordHash: 'zxcv', gender: 2]
		db.users << [username: 'Chekov', passwordHash: 'qaz', gender: 2]
		def users

		when:
		users = new UserStore(db).getAll()
		then:
		users.size() == 2
		with(users.find { it.username == 'McCoy' }) {
			_id != null
			passwordHash == 'zxcv'
			gender == 2
		}
		with(users.find { it.username == 'Chekov' }) {
			_id != null
			passwordHash == 'qaz'
			gender == 2
		}
	}
}
