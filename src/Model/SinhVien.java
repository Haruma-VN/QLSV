package Model;
import java.io.Serializable;

public class SinhVien implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String ten;
	private String lop;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	public SinhVien(int id, String ten, String lop) {
		this.id = id;
		this.ten = ten;
		this.lop = lop;
	}
	public SinhVien() {
	}
	@Override
	public String toString() {
		return "SinhVien [id=" + id + ", ten=" + ten + ", lop=" + lop + "]";
	}
	public String getLop() {
		return lop;
	}
	public void setLop(String lop) {
		this.lop = lop;
	}
}
