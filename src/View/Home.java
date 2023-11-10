package View;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import Model.SinhVien;

public class Home {
	
	private JFrame mFrame;
	private DefaultTableModel mTable;
	private JButton addBtn, deleteBtn, sortBtn, exportBtn;
	private JTextArea idField;
	private JTextArea tenField;
	private JTextArea lopField;
	private JPanel bottomPanel;
	private ArrayList<SinhVien> data;
	private String[] columnName = {"Mã sinh viên", "Tên sinh viên", "Lớp"};
	private JTable jTable;
	public static final String ApplicationName = "Quản lý sinh viên";
	
	
	private void resetField() {
		var emptyString = "";
		this.idField.setText(emptyString);
		this.tenField.setText(emptyString);
		this.lopField.setText(emptyString);
		return;
	}
	
	private Object[][] convertArrayListSinhVienToObjectArray() {
		var mData = new Object[this.data.size()][];
		for(var i = 0; i < this.data.size(); i++) {
			var e = this.data.get(i);
			mData[i] = new Object[3];
			mData[i][0] = e.getId();
			mData[i][1] = e.getTen();
			mData[i][2] = e.getLop();
		}
		return mData;
	}
	
	private void showDialog(String message) {
		JOptionPane.showMessageDialog(null, 
				message, 
				ApplicationName, 
				JOptionPane.INFORMATION_MESSAGE
				);
		return;
	}
	
	private void showDialog(String message, int messageType) {
		JOptionPane.showMessageDialog(null, 
				message, 
				ApplicationName, 
				messageType
				);
		return;
	}
	
	private void exportFile(String filePath) {
		try {
			var outputFile = new FileOutputStream(filePath);
			var writer = new ObjectOutputStream(outputFile);
			writer.writeObject(this.data);
			writer.close();
			outputFile.close();
			this.showDialog("Lưu tệp thành công");
		}
		catch(IOException e) {
			this.showDialog(e.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
		return;
	}
	
	public Home() {
		this.data = new ArrayList<SinhVien>();
		this.idField = new JTextArea(2, 10);
		this.tenField = new JTextArea(2, 10);
		this.lopField = new JTextArea(2, 10);
		this.mFrame = new JFrame();
		this.mFrame.setTitle(ApplicationName);
		this.mFrame.setSize(new Dimension(1200, 500));
		this.mFrame.setLayout(new BorderLayout());
		this.mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addBtn = new JButton("Thêm sinh viên");
		this.deleteBtn = new JButton("Xoá sinh viên");
		this.bottomPanel = new JPanel();
		this.bottomPanel.setLayout(new BoxLayout(this.bottomPanel, BoxLayout.X_AXIS));
		this.bottomPanel.add(new JLabel("Mã sinh viên"));
		this.bottomPanel.add(this.idField);
		this.bottomPanel.add(new JLabel("Tên sinh viên"));
		this.bottomPanel.add(this.tenField);
		this.bottomPanel.add(new JLabel("Lớp sinh viên"));
		this.bottomPanel.add(this.lopField);
		this.addBtn.addActionListener(e -> {
			try {
				var id = this.idField.getText();
				var hasSameId = false;
				for(var k : this.data) {
					if(Integer.compare(k.getId(), Integer.parseInt(id)) == 0) {
						hasSameId = true;
					}
				}
				if(hasSameId) {
					this.showDialog("ID đã tồn tại", JOptionPane.ERROR_MESSAGE);
				}
				else {
					var ten = this.tenField.getText();
					var lop = this.lopField.getText();
					var iListView = new Object[]{id, ten, lop};
					this.data.add(new SinhVien(Integer.parseInt(id), ten, lop));
					this.mTable.addRow(iListView);
					this.mTable.fireTableDataChanged();
					this.resetField();
					this.showDialog(String.format("Đã thêm sinh viên: %s", ten));
				}
			}
			catch(Exception exception) {
				this.showDialog(exception.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
		});
		this.deleteBtn.addActionListener(e -> {
			var current = (DefaultTableModel)this.jTable.getModel();
			if(this.jTable.getSelectedRowCount() == 1) {
				var row = this.jTable.getSelectedRow();
				var deletedData = this.data.get(row);
				current.removeRow(row);
				this.showDialog(String.format("Đã xoá sinh viên: %s", 
						deletedData.getTen()
						));
				this.data.remove(row);
			}
			else {
				var mArray = this.jTable.getSelectedRows();
				Arrays.sort(mArray);
				for(var i = mArray.length - 1; i >= 0; i--) {
					current.removeRow(mArray[i]);
					this.data.remove(i);
				}
				this.showDialog(String.format("Đã xoá %d sinh viên", 
						mArray.length
						));
			}
		});
		this.sortBtn = new JButton("Sắp xếp sinh viên");
		this.sortBtn.addActionListener(e -> {
			this.data.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
			for(var i = this.data.size() - 1; i >= 0; i--) {
				this.mTable.removeRow(i);
			}
			for(var m : this.data) {
				this.mTable.addRow(new Object[] {m.getId(), m.getTen(), m.getLop()});
			}
			this.showDialog("Đã sắp xếp danh sách sinh viên");
		});
		this.data.add(new SinhVien(103, "Đàm Gia Khánh", "CNTT01"));
		this.data.add(new SinhVien(101, "Lê Minh Hải", "CNTT01"));
		this.data.add(new SinhVien(102, "Nguyễn Ba Duy", "CNTT01"));
		this.exportBtn = new JButton("Xuất danh sách");
		this.exportBtn.addActionListener(e -> {
			var jFile = new JFileChooser();
			jFile.setDialogTitle("Chọn đường dẫn xuất tệp");
			var selection = jFile.showSaveDialog(null);
			if(selection == JFileChooser.APPROVE_OPTION) {
				this.exportFile(jFile.getSelectedFile().getAbsolutePath());
			}
			else {
				this.showDialog("Bạn chưa cung cấp đường dẫn xuất tệp", JOptionPane.ERROR_MESSAGE);
			}
		});
		this.bottomPanel.add(this.addBtn);
		this.bottomPanel.add(this.deleteBtn);
		this.bottomPanel.add(this.sortBtn);
		this.bottomPanel.add(this.exportBtn);
		this.mFrame.add(this.bottomPanel, BorderLayout.SOUTH);
		this.mTable = new DefaultTableModel(this.convertArrayListSinhVienToObjectArray(), this.columnName);
		this.jTable = new JTable(this.mTable);
		this.mFrame.add(new JScrollPane(this.jTable), BorderLayout.CENTER);
		this.mFrame.setVisible(true);
		this.mFrame.validate();
	}
	
	
}
