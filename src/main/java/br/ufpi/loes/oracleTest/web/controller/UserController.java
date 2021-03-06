/**
 * 
 */
package br.ufpi.loes.oracleTest.web.controller;

import javax.inject.Inject;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.serialization.gson.WithoutRoot;
import br.com.caelum.vraptor.view.Results;
import br.ufpi.loes.oracleTest.web.model.User;
import br.ufpi.loes.oracleTest.web.model.vo.UserVO;
import br.ufpi.loes.oracleTest.web.repository.UserDao;

@Controller
@Path("/backend/users")
public class UserController {

	private final Result result;
	private final UserDao userDao;

	/**
	 * @deprecated CDI eyes only
	 */
	protected UserController() {
		this(null, null);
	}

	@Inject
	public UserController(UserDao dao, Result result) {
		this.userDao = dao;
		this.result = result;
	}

	@Consumes(value = "application/json", options = WithoutRoot.class)
	@Post(value = {"", "/"})
	public void add(User user, String repassword) {
		if (!userDao.containsUserWithEMail(user.getEmail())) {
			userDao.insert(user);
		}
	}
	
	@Consumes(value = "application/json", options = WithoutRoot.class)
	@Post(value = {"/login", "/login/"})
	public void login(User user) {
		if (userDao.containsUserWithEMail(user.getEmail())) {
			User userBD = userDao.findEmail(user.getEmail());
			if (userBD.getPassword().equals(user.getPassword())) {
				UserVO userVO = new UserVO(userBD);
				result.use(Results.json()).withoutRoot().from(userVO)
						.serialize();
			}else{
				//TODO: ADD MENSAGEM SENHA INVÁLIDA
			}
		}else{
			//TODO: ADD MENSAGEM EMAIL INVÁLIDO
		}
	}
	
	@Get(value={"","/"})
	public void getUsers(){
		result.use(Results.json()).withoutRoot().from(userDao.findAll())
		.serialize();
	}

}