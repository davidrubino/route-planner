package pcd.telecomnancy.model.member;

import java.util.ArrayList;
import java.util.List;

import pcd.telecomnancy.model.routes.Route;




public class Member {
	private String login;
	private List<Route> history;
	private List<Favorite> favorites;
	
	public Member(){
		this.setLogin("Invité");
		history=new ArrayList<Route>();
		favorites=new ArrayList<Favorite>();
	}
	
	public Member(String login){
		this();
		this.setLogin(login);
	}

	public List<Favorite> getFavorites() {
		return favorites;
	}

	public void setFavorites(List<Favorite> favorites) {
		this.favorites = favorites;
	}

	public List<Route> getHistory() {
		return history;
	}

	public void setHistory(List<Route> history) {
		this.history = history;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public boolean isGuest(){
		return login.equals("Invité");
	}
}
