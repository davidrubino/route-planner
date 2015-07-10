package pcd.telecomnancy.model.member;

import java.net.URLConnection;
import java.util.Observable;


/** Etablissement d'une session de connexion **/
public class Session extends Observable {

	private String SESSID = null;
	private Member member;
	private CurrentOnlineRequestModel currentOnlineRequest;

	/** Construit une instance d'une session vide **/
	public Session() {
		member = new Member();
		currentOnlineRequest = new CurrentOnlineRequestModel();
	}

	/**
	 * Construit une instance d'une session
	 * 
	 * @param SESSID
	 *            l'id de la session
	 * @param member
	 *            le membre qui ouvre la session
	 **/
	public Session(String SESSID, Member member) {
		this.SESSID = SESSID;
		this.member = member;
	}

	/**
	 * Méthode établissant la connexion
	 * 
	 * @param connection
	 *            l'url de la connexion
	 **/
	public void buildPart(URLConnection connection) {
		if (isActive())
			connection.addRequestProperty("Cookie", "PHPSESSID=" + SESSID);
	}

	/**
	 * Accesseur de l'id de la session
	 * 
	 * @return SESSID l'id de la session
	 **/
	public String getSESSID() {
		return SESSID;
	}

	/**
	 * Accesseur du membre
	 * 
	 * @return membre le membre qui ouvre la connexion
	 **/
	public Member getMember() {
		return member;
	}

	/**
	 * Mutateur de l'id de la session
	 * 
	 * @param SESSID
	 *            le nouvel id de la session
	 **/
	public void setSESSID(String SESSID) {
		this.SESSID = SESSID;
	}

	/**
	 * Mutateur du membre
	 * 
	 * @param member
	 *            le nouveau membre
	 **/
	public void setMember(Member member) {
		this.member = member;
		setChanged();
		notifyObservers();
	}

	/**
	 * Teste l'activation de la session
	 * 
	 * @return boolean la réponse au teste de l'activation
	 **/
	private boolean isActive() {
		return SESSID != null;
	}

	/**
	 * Teste si le membre est invité
	 * 
	 * @return boolean la réponse
	 **/
	public boolean isLoggedIn() {
		return !member.isGuest();
	}

	/** Restaure la session par défaut **/
	public void reset() {
		SESSID = null;
		this.setMember(new Member());
	}

	/** Méthode utilisé lorsqu'on quitte une session **/
	public void logout() {
		// TODO Auto-generated method stub
		this.reset();
	}

	/**
	 * Accesseur de la description de la requête actuelle faite au serveur
	 * 
	 * @return description la description de la requête
	 **/
	public String getCurrentOnlineRequestDesc() {
		return currentOnlineRequest.getDescription();
	}

	/**
	 * Accesseur du modèle de la requête au serveur
	 * 
	 * @return currentOnlineRequest le modèle de la requête
	 **/
	public CurrentOnlineRequestModel getCurrentOnlineRequest() {
		return currentOnlineRequest;
	}

	/**Le modèle de la requête faite au serveur**/
	public class CurrentOnlineRequestModel extends Observable {
		private String description;

		/**Accesseur de la description de la requête 
		 * @return description la description de la requête**/
		public String getDescription() {
			return description;
		}

		/**Mutateur de la description
		 * @param description la nouvel description de la requête**/
		public void setDescription(String description) {
			this.description = description;
			setChanged();
			notifyObservers();
		}

		public void clear() {
			this.description = "";
			setChanged();
			notifyObservers();
		}
	}
}
