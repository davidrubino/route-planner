package pcd.telecomnancy.model;

import java.util.HashMap;
import java.util.Observable;

/** La classe permettant de créer de nouveau modèle d'erreur **/
public class ErrorModel extends Observable {

	private HashMap<String, String> messages;

	/** Construit une instance d'un modèle d'erreur **/
	public ErrorModel() {
		this.messages = new HashMap<String, String>();
	}

	/**
	 * Ajouter un titre et un message à l'erreur
	 * 
	 * @param title
	 *            titre de l'erreur
	 * @param message
	 *            de l'erreur
	 **/
	public synchronized void addInfos(String title, String message) {
		this.messages.put(title, message);
	}

	/** Permet de rendre visible les messages d'erreur **/
	public synchronized void validate() {
		setChanged();
		notifyObservers();
		this.messages.clear();
	}

	/**
	 * Accesseur des messages
	 * 
	 * @return messages les différents messages
	 **/
	public HashMap<String, String> getMessages() {
		return messages;
	}
}
