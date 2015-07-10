package pcd.telecomnancy.serverCommunication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.serverCommunication.param.IParam;



public class ServerCommunicationImpl implements ServerCommunication{

	private ArrayList<Request> reqs;
	private ServerDataListener serverDataListener;
	private Session session;
	private boolean comError;

	public ServerCommunicationImpl(Session session) {
		reqs = new ArrayList<Request>();
		comError = false;
		this.session=session;
	}

	/**
	 * Ajoute un paramètre dans la liste des requêtes à effectuer
	 * @param param Le paramètre de la requête http
	 */
	public void addRequest(IParam param) {
		this.reqs.add(new Request(param));
	}

	public void doRequests(ServerDataListener serverDataListener) {
		this.serverDataListener = serverDataListener;
		Semaphore sem = new Semaphore(0);
		RequestResponse resp = new RequestResponse(this.reqs,
				serverDataListener);
		resp.setSem(sem);
		Thread respt = new Thread(resp);
		respt.start();
		Thread[] threads = new Thread[this.reqs.size()];
		for (int i = 0; i < this.reqs.size(); i++) {
			this.reqs.get(i).setSem(sem);
			threads[i] = new Thread(this.reqs.get(i));
			threads[i].start();
		}
	}

	private class Request implements Runnable {

		private String response;
		private Semaphore sem;
		private IParam param;

		public Request(IParam param) {
			this.param = param;
		}

		public String getResponse() {
			return response;
		}

		public void setSem(Semaphore sem) {
			this.sem = sem;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				this.response = this.send();
			} catch (IOException e) {
				comError = true;

				if (session.isLoggedIn()) {
					serverDataListener
							.onError(
									"Connexion au serveur",
									"Vous avez été deconnecté car le serveur est injoignable, veuillez vérifier votre connexion internet.");
					session.reset();
				} else
					serverDataListener
							.onError("Connexion au serveur",
									"Le serveur est injoignable, veuillez vérifier votre connexion internet.");

			} catch (ServerResponseException e) {
				// TODO Auto-generated catch block
				comError = true;
				serverDataListener.onError("Code d'erreur", e.getMessage());
			} finally {
				sem.release(); // V()
			}
		}

		public String send() throws IOException, ServerResponseException {
			// TODO Auto-generated method stub
			URL obj = new URL(this.param.getUrl() + this.param.getPage());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			//System.out.println(this.param.getUrl() + this.param.getPage());
			con.setRequestMethod(this.param.getRequestMethod());
			this.param.buildPart(con);
			session.buildPart(con);
			
			if(this.param.getRequestMethod()=="POST"){
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(this.param.getPostString());
				wr.flush();
				wr.close();
			}

			String cookie = con.getHeaderField("Set-Cookie");
			if (cookie != null) {
				String sessid = cookie.split(";")[0].split("=")[1];
				if (sessid != null) {
					if (session.getSESSID() != null) {
						if (session.getSESSID() != sessid
								&& session.isLoggedIn())
							// throw new TimeOutException();
							System.err.println("Session expirée");
					} else {
						session.setSESSID(sessid);
					}
				}
			}

			int responseCode = con.getResponseCode();
			if (responseCode < 200 || responseCode >= 300)
				throw new ServerResponseException(responseCode,
						this.param.getUrl() + this.param.getPage());

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		}
	}

	private class RequestResponse implements Runnable {
		private Semaphore sem;
		private int reqNb;
		private ArrayList<Request> reqs;
		private ServerDataListener serverDataListener;
		private List<String> response;

		public RequestResponse(ArrayList<Request> reqs,
				ServerDataListener serverDataListener) {
			this.reqs = reqs;
			this.reqNb = reqs.size();
			this.serverDataListener = serverDataListener;
			response = new ArrayList<String>(this.reqNb);
		}

		public void setSem(Semaphore sem) {
			this.sem = sem;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for (int i = 0; i < reqNb; i++) {
				try {
					sem.acquire(); // P()
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Communication serveur : une erreur de synchronisation est survenue (permission sur la sémaphore)");
				}
			}

			if (!comError) {
				for (int i = 0; i < reqs.size(); i++) {
					if (reqs.get(i).getResponse() != null) {

						response.add(reqs.get(i).getResponse());
					}
				}
				this.serverDataListener.dataReceived(this.response);
			}
			this.reqs.clear();
		}

	}
}
