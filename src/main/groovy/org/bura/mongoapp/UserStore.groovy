package org.bura.mongoapp

import com.mongodb.DB

class UserStore {
	private DB db

	UserStore(DB db) {
		this.db = db
	}

	String create(String username, String passwordHash, int gender) {
		def newUser = [username: username, passwordHash: passwordHash, gender: gender]
		db.users << newUser

		newUser._id.toString()
	}

	def findByUsername(String username) {
		db.users.findOne(username: username)
	}

	def getAll() {
		db.users.find()
	}
}
