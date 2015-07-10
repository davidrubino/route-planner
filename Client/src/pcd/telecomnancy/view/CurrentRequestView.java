package pcd.telecomnancy.view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.member.Session;


public class CurrentRequestView extends JPanel implements Observer{
	
	private static final long serialVersionUID = 1L;
	private JLabel description;
	private Session session;
	
	public CurrentRequestView(Model model) {
		// TODO Auto-generated constructor stub
		session=model.getSession();
		session.getCurrentOnlineRequest().addObserver(this);
		description = new JLabel();
		description.setIcon(new ImageIcon(getClass().getClassLoader().getResource("loader.gif")));
		description.setVisible(false);
		this.add(description);
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(session.getCurrentOnlineRequestDesc().length()>0){
					description.setText(session.getCurrentOnlineRequestDesc()+"...");
					
					description.setVisible(true);
				}else
					description.setVisible(false);
			}
		});
		
	}

}
