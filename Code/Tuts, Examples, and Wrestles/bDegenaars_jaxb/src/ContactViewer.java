import generated.Contact;
import generated.MyContacts;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ContactViewer extends JFrame {

	protected static String host;
	private JLabel showContact, email, address1, address2, city, state, zip;
	private JComboBox contacts;
	private JTextField emailField, address1Field, address2Field, cityField,
			stateField, zipField;
	private JPanel textPanel, fieldPanel;
	private List<String> nameList, emailList, addressList1, addressList2,
			cityList, stateList, zipList;
	private DefaultComboBoxModel model;

	public ContactViewer() {

		textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(7, 1));

		fieldPanel = new JPanel();
		fieldPanel.setLayout(new GridLayout(7, 1));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(new Dimension(400, 250));
		this.setLayout(new BorderLayout());

		this.add(createLoadButton("Load"), BorderLayout.NORTH);
		this.add(textPanel, BorderLayout.WEST);
		this.add(fieldPanel, BorderLayout.EAST);

		createTextField();

		textPanel.add(createJLabel("Show Contact: ", showContact));
		fieldPanel.add(createContactsCombo());

		textPanel.add(createJLabel("Email: ", email));
		fieldPanel.add(emailField);

		textPanel.add(createJLabel("Address1: ", address1));
		fieldPanel.add(address1Field);

		textPanel.add(createJLabel("Address2: ", address2));
		fieldPanel.add(address2Field);

		textPanel.add(createJLabel("City: ", city));
		fieldPanel.add(cityField);

		textPanel.add(createJLabel("State: ", state));
		fieldPanel.add(stateField);

		textPanel.add(createJLabel("Zip: ", zip));
		fieldPanel.add(zipField);

		// this.pack();
		this.setVisible(true);
	}

	public JButton createLoadButton(String buttonName) {

		JButton loadButton = new JButton(buttonName);
		loadButton.setBounds(0, 0, this.getWidth(), 20);
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				host = JOptionPane.showInputDialog(ContactViewer.this,
						"Select Host : ", "localhost");

				try {
					Socket client = new Socket(host, ContactServer.PORT);
					// OutputStream serverOut = server.getOutputStream();
					InputStream serverIn = client.getInputStream();
					BufferedReader serverRead = new BufferedReader(
							new InputStreamReader(serverIn));
					String resp = serverRead.readLine();
					System.out.println(resp);
					unMarshall(serverRead);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}

				for (String s : nameList)
					model.addElement(s);
			}
		});

		return loadButton;
	}

	public JLabel createJLabel(String labelName, JLabel label) {

		label = new JLabel(labelName);

		return label;
	}

	public JComboBox createContactsCombo() {

		model = new DefaultComboBoxModel();
		contacts = new JComboBox(model);

		contacts.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {

				int position = contacts.getSelectedIndex();
				emailField.setText(emailList.get(position));
				address1Field.setText(addressList1.get(position));
				address2Field.setText(addressList2.get(position));
				cityField.setText(cityList.get(position));
				stateField.setText(stateList.get(position));
				zipField.setText(zipList.get(position));
				fieldPanel.repaint();
			}

		});

		return contacts;
	}

	public void createTextField() {

		emailField = new JTextField();
		address1Field = new JTextField();
		address2Field = new JTextField();
		cityField = new JTextField();
		stateField = new JTextField();
		zipField = new JTextField();

	}

	public void unMarshall(BufferedReader serverRead) {

		try {

			// Create a JAXB Context and Unmarshaller
			JAXBContext context = JAXBContext.newInstance("generated");
			Unmarshaller unmarsh = context.createUnmarshaller();

			// unmarshall the document
			MyContacts myContacts = (MyContacts) unmarsh.unmarshal(serverRead);

			nameList = new ArrayList<String>();
			emailList = new ArrayList<String>();
			addressList1 = new ArrayList<String>();
			addressList2 = new ArrayList<String>();
			cityList = new ArrayList<String>();
			stateList = new ArrayList<String>();
			zipList = new ArrayList<String>();

			for (Contact c : myContacts.getContact()) {
				nameList.add(c.getName());
				emailList.add(c.getEmail());
				addressList1.add(c.getAddress().getAddress1());
				if (c.getAddress().getAddress2() != null
						|| !c.getAddress().getAddress2().equals(""))
					addressList2.add(c.getAddress().getAddress2());
				else
					addressList2.add("N/A");
				cityList.add(c.getAddress().getCity());
				stateList.add(c.getAddress().getState());
				zipList.add(c.getAddress().getZip());
			}

			for (int i = 0; i < nameList.size(); i++) {
				System.out.println(nameList.get(i));
				System.out.println(emailList.get(i));
				System.out.println(addressList1.get(i));
				System.out.println(addressList2.get(i));
				System.out.println(cityList.get(i));
				System.out.println(stateList.get(i));
				System.out.println(zipList.get(i));
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ContactViewer();

	}

}
