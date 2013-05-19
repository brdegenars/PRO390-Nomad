import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ListExample extends JFrame {

	JComboBox box;

	public ListExample() {

		super("List Example");
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement("jake");
		model.addElement("fred");
		model.addElement("bob");
		box = new JComboBox(model);
		box.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {

				JOptionPane.showMessageDialog(ListExample.this, "Selected : "
						+ box.getSelectedItem());
			}
		});
		this.add(box);
		this.pack();

	}

	public static void main(String[] args) {
		ListExample ex = new ListExample();
		ex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ex.setLocationRelativeTo(null);
		ex.setVisible(true);
	}

}
