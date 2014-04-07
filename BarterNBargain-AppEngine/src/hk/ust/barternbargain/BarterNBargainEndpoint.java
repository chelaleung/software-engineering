package hk.ust.barternbargain;

import hk.ust.barternbargain.EMF;

import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.codec.binary.Base64;

@Api(name = "barternbargain", namespace = @ApiNamespace(ownerDomain = "ust.hk", ownerName = "ust.hk", packagePath = "barternbargain"))
public class BarterNBargainEndpoint {

	private final static String CAS_GATEWAY = "https://ihome.ust.hk/~mfptai/cas/validate";
	
	private String getHttpAuthenticationHeader(String user, String pass) {
		String combined = user + ":" + pass;
		byte[] secret = Base64.encodeBase64(combined.getBytes());
		return "Basic " + new String(secret);
	}

	@ApiMethod(name="insertSession", path="sessions", httpMethod=HttpMethod.POST)
    public Session insertSession(@Named("username") String username, @Named("password") String password)
    		throws IOException, ServiceException {
		String decodedUser = URLDecoder.decode(username, "utf-8");
		String decodedPass = URLDecoder.decode(password, "utf-8");
		URL url = new URL(CAS_GATEWAY);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		String authHeader = getHttpAuthenticationHeader(decodedUser, decodedPass);
		conn.setRequestProperty("Authorization", authHeader);
		conn.connect();
		Session session = null;
		if (conn.getResponseCode() == 200) {
			session = new Session(decodedUser);
			EntityManager mgr = getEntityManager();
			try {
				mgr.persist(session);
				/*User user = mgr.find(User.class, username);
				if (user == null) {
					mgr.persist(new User(username));
				}*/
			} finally {
				mgr.close();
			}
		} else if (conn.getResponseCode() == 401) {
			throw new UnauthorizedException("Wrong login credential");
		} else {
			throw new InternalServerErrorException("Error in communicating with CAS gateway");
		}
		return session;
    }

	@ApiMethod(name="getSession", path="sessions/{id}", httpMethod=HttpMethod.GET)
	public Session getSession(@Named("id") String id) throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			Session session = mgr.find(Session.class, id);
			if (session == null) {
				throw new NotFoundException("Session not found");
			}
			return session;
		} finally {
			mgr.close();
		}
	}

	@ApiMethod(name="removeSession", path="sessions/{id}", httpMethod=HttpMethod.DELETE)
	public void removeSession(@Named("id") String id) throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			Session session = mgr.find(Session.class, id);
			if (session == null) {
				throw new NotFoundException("Session not found");
			}
			mgr.remove(session);
		} finally {
			mgr.close();
		}
	}

	private void checkIsUser(String sessionId, String userId) throws ServiceException {
		if (!userId.equals(getSession(sessionId).getUserId())) {
			throw new ForbiddenException("User not login");
		}
	}
	
	@ApiMethod(name="insertUser")
	public User insertUser(@Named("sessionId") String sessionId, User user)
			throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			checkIsUser(sessionId, user.getId());
			user.setRegistrationTime(new Date());
			mgr.persist(user);
		} finally {
			mgr.close();
		}
		return user;
	}

	@ApiMethod(name="getUser", path="users/{id}", httpMethod=HttpMethod.GET)
	public User getUser(@Named("id") String id)
			throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			User user = mgr.find(User.class, id);
			if (user == null) {
				throw new NotFoundException("User not found");
			}
			return user;
		} finally {
			mgr.close();
		}
	}
	
	@ApiMethod(name="updateUser")
	public User updateUser(@Named("sessionId") String sessionId, User user)
			throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			checkIsUser(sessionId, user.getId());
			mgr.persist(user);
		} finally {
			mgr.close();
		}
		return user;
	}
	
	@SuppressWarnings("unchecked")
	@ApiMethod(name="listItem", path="items", httpMethod=HttpMethod.GET)
	public List<Item> listItem() {
		EntityManager mgr = getEntityManager();
		try {
			Query query = mgr.createQuery("select from Item as Item");
			return (List<Item>) query.getResultList();
		} finally {
			mgr.close();
		}
	}

	@SuppressWarnings("unchecked")
	@ApiMethod(name="listUserItem", path="users/{userId}/items")
	public List<Item> listUserItem(@Named("userId") String userId) {
		EntityManager mgr = getEntityManager();
		try {
			Query query = mgr.createQuery("select from Item as Item where userId = :userId")
					.setParameter("userId", userId);
			return (List<Item>) query.getResultList();
		} finally {
			mgr.close();
		}
	}
	
	
	@ApiMethod(name="getItem", path="items/{id}", httpMethod=HttpMethod.GET)
	public Item getItem(@Named("id") Long id) throws ServiceException {
		EntityManager mgr = getEntityManager();
		Item item = null;
		try {
			item = mgr.find(Item.class, id);
			if (item == null) {
				throw new NotFoundException("Item not found");
			}
		} finally {
			mgr.close();
		}
		return item;
	}
	
	private void checkCanModifyItem(String sessionId, Long id)
			throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			Item item = mgr.find(Item.class, id);
			if (item == null) {
				throw new NotFoundException("Item not found");
			}
			if (!item.getUserId().equals(getSession(sessionId).getUserId())) {
				throw new ForbiddenException("Item not belong to session's user");
			}
		} finally {
			mgr.close();
		}
	}

	@ApiMethod(name="insertItem")
	public Item insertItem(@Named("sessionId") String sessionId, Item item)
			throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			if (item.getId() != null && mgr.find(Item.class, item.getId()) != null) {
				throw new BadRequestException("Item already exist");
			}
			item.setPostingTime(new Date());
			item.setUserId(getSession(sessionId).getUserId());
			mgr.persist(item);
		} finally {
			mgr.close();
		}
		return item;
	}

	@ApiMethod(name="updateItem")
	public Item updateItem(@Named("sessionId") String sessionId, Item item) 
			throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			checkCanModifyItem(sessionId, item.getId());
			mgr.persist(item);
		} finally {
			mgr.close();
		}
		return item;
	}

	@ApiMethod(name="removeItem", path="items/{id}", httpMethod=HttpMethod.DELETE)
	public void removeItem(@Named("sessionId") String sessionId, @Named("id") Long id)
			throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			checkCanModifyItem(sessionId, id);
			Item item = mgr.find(Item.class, id);
			mgr.remove(item);
		} finally {
			mgr.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	@ApiMethod(name="listComment", path="items/{itemId}/comments", httpMethod=HttpMethod.GET)
	public List<Comment> listComment(@Named("itemId") Long itemId) {
		EntityManager mgr = getEntityManager();
		try {
			Query query = mgr
					.createQuery("select from Comment as Comment where itemId = :itemId")
					.setParameter("itemId", itemId);
			return (List<Comment>) query.getResultList();
		} finally {
			mgr.close();
		}
	}
	
	private void checkCanModifyComment(String sessionId, Long commentId)
			throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			Comment comment = mgr.find(Comment.class, commentId);
			if (comment == null) {
				throw new NotFoundException("Comment not exist");
			}
			if (!comment.getUserId().equals(getSession(sessionId).getUserId())) {
				throw new ForbiddenException("Comment not belong to session's user");
			}
		} finally {
			mgr.close();
		}
	}
	
	
	@ApiMethod(name="insertComment", path="items/{itemId}/comments", httpMethod = HttpMethod.POST)
	public Comment insertComment(@Named("sessionId") String sessionId,
			@Named("itemId") Long itemId,
			@Named("message") String message) throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			Comment comment = new Comment(itemId, getSession(sessionId).getUserId(), message);
			mgr.persist(comment);
			return comment;
		} finally {
			mgr.close();
		}
	}
	
	@ApiMethod(name="updateComment")
	public Comment updateComment(@Named("sessionId") String sessionId, Comment comment)
			throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			checkCanModifyComment(sessionId, comment.getId());
			mgr.persist(comment);
		} finally {
			mgr.close();
		}
		return comment;
	}
	
	@ApiMethod(name="removeComment", path="comments/{commentId}", httpMethod = HttpMethod.DELETE)
	public void removeComment(@Named("sessionId") String sessionId, @Named("commentId") Long commentId)
			throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			checkCanModifyComment(sessionId, commentId);
			Comment comment = mgr.find(Comment.class, commentId);
			mgr.remove(comment);
		} finally {
			mgr.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	@ApiMethod(name="listUserTrade", path="users/{userId}/trades", httpMethod = HttpMethod.GET)
	public List<Trade> listUserTrade(@Named("userId") String userId) {
		EntityManager mgr = getEntityManager();
		try {
			Query query = mgr.createQuery("select from Trade as Trade where userId = :userId")
					.setParameter("userId", userId);
			return (List<Trade>) query.getResultList();
		} finally {
			mgr.close();
		}
	}
	
	@ApiMethod(name="insertTrade", path="items/{itemId}/trades", httpMethod=HttpMethod.POST)
	public Trade insertTrade(@Named("sessionId") String sessionId,
			@Named("itemId") Long itemId) {
		EntityManager mgr = getEntityManager();
		try {
			Session session = mgr.find(Session.class, sessionId);
			Item item = mgr.find(Item.class, itemId);
			User seller = mgr.find(User.class, item.getUserId());
			Trade trade = new Trade(item.getId(), seller.getId(), session.getUserId());
			mgr.persist(trade);
			return trade;
		} finally {
			mgr.close();
		}
	}
	
	private void checkCanModifyTrade(String sessionId, Trade trade) throws ServiceException {
		if (trade == null) {
			throw new NotFoundException("Trade not exist");
		}
		if (!trade.getSellerId().equals(getSession(sessionId).getUserId())) {
			throw new ForbiddenException("Trade not belong to session's user.");
		}
	}
	
	private void checkCanAcceptOrRejectTrade(Trade trade) throws ServiceException {
		if (!trade.getStatus().equals("Pending")) {
			throw new BadRequestException("Trade not in pending state.");
		}
	}
	
	@ApiMethod(name="acceptTrade", path="trades/{id}/accept", httpMethod=HttpMethod.POST)
	public Trade acceptTrade(@Named("sessionId") String sessionId,
			@Named("id") Long id) throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			Trade trade = mgr.find(Trade.class, id);
			checkCanModifyTrade(sessionId, trade);
			checkCanAcceptOrRejectTrade(trade);
			trade.setStatus("Accepted");
			mgr.persist(trade);
			return trade;
		} finally {
			mgr.close();
		}
	}
	
	@ApiMethod(name="rejectTrade", path="trades/{id}/reject", httpMethod=HttpMethod.POST)
	public Trade rejectTrade(@Named("sessionId") String sessionId,
			@Named("id") Long id) throws ServiceException {
		EntityManager mgr = getEntityManager();
		try {
			Trade trade = mgr.find(Trade.class, id);
			checkCanModifyTrade(sessionId, trade);
			checkCanAcceptOrRejectTrade(trade);
			trade.setStatus("Rejected");
			mgr.persist(trade);
			return trade;
		} finally {
			mgr.close();
		}
	}
	
	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
