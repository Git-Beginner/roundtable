package com.roundtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public User getUser(String username) {
		return userDao.getUser(username);
	}
	
	public User saveUser(User user) {
		return userDao.saveUser(user);
	}
}
