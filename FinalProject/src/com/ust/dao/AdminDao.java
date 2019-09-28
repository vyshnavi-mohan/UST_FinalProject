package com.ust.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ust.model.Login;
import com.ust.model.VendorContact;

public class AdminDao {
	
	JdbcTemplate template;

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	public AdminDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	//--------------------------------- LOGIN -----------------------------//
	
	public Login verifylogin(String username, String password) {
		String sql = "select userId,username,password from loginTable where username='"+ username+ "' and password='" + password + "'";
		return template.queryForObject(sql, new Object[] {},
		new BeanPropertyRowMapper<Login>(Login.class));
		}

	
	//---------------------- GET ALL VENDORS -----------------------------------//
	
		public List<VendorContact> getAllVendor() {
			return template.query("select vendorid,vendorname,vendoraddress,vendorlocation,vendorservice,vendorpincode from vendorTable where isActive=0",
							new RowMapper<VendorContact>() {
								public VendorContact mapRow(ResultSet rs, int row)
										throws SQLException {
									VendorContact vendors = new VendorContact();
									vendors.setVendorId(rs.getInt(1));
									vendors.setVendorName(rs.getString(2));
									vendors.setVendorAddress(rs.getString(3));
									vendors.setVendorLocation(rs.getString(4));
									vendors.setVendorService(rs.getString(5));
									vendors.setVendorPincode(rs.getInt(6));
									return vendors;
								}
							});
		}

		//----------------------- GET VENDOR BY NAME -----------------------------------------//
		
		public List<VendorContact> getVendorByName(String vendorname) {
			return template
					.query("select vendorid,vendorname,vendoraddress,vendorlocation,vendorservice,vendorpincode from vendorTable where isActive=0 and vendorname='"+ vendorname + "'",
							new RowMapper<VendorContact>() {
								public VendorContact mapRow(ResultSet rs, int row)
										throws SQLException {
									VendorContact vendor = new VendorContact();
									vendor.setVendorId(rs.getInt(1));
									vendor.setVendorName(rs.getString(2));
									vendor.setVendorAddress(rs.getString(3));
									vendor.setVendorLocation(rs.getString(4));
									vendor.setVendorService(rs.getString(5));
									vendor.setVendorPincode(rs.getInt(6));
									return vendor;
								}
							});
		}
		
		//----------------------------- ADD VENDOR AND CONTACT -----------------------------//
		
		public int addVendor(VendorContact vendorContact) {

			String sql1 = "insert into vendorTable(vendorname,vendoraddress,vendorlocation,vendorservice,vendorpincode,isActive) values "
					+ "('"
					+ vendorContact.getVendorName()
					+ "','"
					+ vendorContact.getVendorAddress()
					+ "','"
					+ vendorContact.getVendorLocation()
					+ "','"
					+ vendorContact.getVendorService()
					+ "',"
					+ vendorContact.getVendorPincode()
					+ ","
					+ 0
					+ ")";

			 template.update(sql1);
			 
			 Integer maxId = getSequence();
			 String sql2="insert into contactTable(vendorid,contactname,contactDepartment,contactEmail,contactPhone) values ("
					 + maxId
						+ ",'"
						+ vendorContact.getContactName()
						+ "','"
						+ vendorContact.getContactDepartment()
						+ "','"
						+ vendorContact.getContactEmail()
						+ "','" + vendorContact.getContactPhone() + "')";
			 return template.update(sql2);

					 
			 
		}
		
		//----------------------- GET VENDOR BY ID ---------------------------------//
		
		private Integer getSequence() {
			Integer seq;
			String sql = "select MAX(vendorid)from vendorTable";
			seq = template.queryForObject(sql, new Object[] {}, Integer.class);
			return seq;
		}
		
		//----------------------------- UPDATE VENDOR ------------------------------//
		
		public int updateVendor(int vendorid, VendorContact vendorCont) {

			String sql = "update vendorTable set vendorname='" + vendorCont.getVendorName()
					+ "' ,vendoraddress='" + vendorCont.getVendorAddress() + "' ,vendorlocation='"
					+ vendorCont.getVendorLocation() + "',vendorservice='" + vendorCont.getVendorService() + "',vendorpincode='" + vendorCont.getVendorPincode() + "',isActive=" + vendorCont.getIsActive() + " "
					+ "where vendorid =" + vendorid;
			template.update(sql);

			// Integer maxId = getSequence();

			String sql1 = "update contactTable set vendorid=" + vendorid + ",contactname='"
					+ vendorCont.getContactDepartment() + "',contactDepartment='"
					+ vendorCont.getContactName() + "',contactEmail='"
					+ vendorCont.getContactEmail() + "',contactPhone='" + vendorCont.getContactPhone() + "'where contactid = " + vendorCont.getContactId();

			return template.update(sql1);
		}

		//--------------------- DISABLE VENDOR -----------------------------------//
		
		public int disableVendor(int vendorId) {

			String sql = "update vendorTable set isActive='1' where vendorId=" + vendorId
					+ "";

			return template.update(sql);
		}
		
		//--------------------------- GET CONTACT BY VENDOR ID -------------------------------------//
		
		public List<VendorContact> getContactByVendorId(int vendorid) {
			return template.query("select contactid,vendorid,contactname,contactDepartment,contactEmail,contactPhone from contactTable where vendorid="+vendorid+"", new RowMapper<VendorContact>() {
				public VendorContact mapRow(ResultSet rs, int row)
						throws SQLException {
					VendorContact vendorContact = new VendorContact();
					vendorContact.setContactId(rs.getInt(1));
					vendorContact.setVendorId(rs.getInt(2));
					vendorContact.setContactName(rs.getString(3));
					vendorContact.setContactDepartment(rs.getString(4));
					vendorContact.setContactEmail(rs.getString(5));
					vendorContact.setContactPhone(rs.getString(6));
					return vendorContact;
				}
			});
			}
		
		
		//--------------------------- GET VENDOR BY ID ------------------------------//
		public VendorContact getVendorById(int vendorId) {
			
		String	sql= "select vc.vendorid,vc.vendorname,vc.vendoraddress,vc.vendorlocation,vc.vendorservice,vc.vendorpincode,cd.contactname,cd.contactDepartment,cd.contactEmail,cd.contactPhone,cd.contactid from vendortable vc join contacttable cd on vc.vendorid=cd.vendorid where vc.isActive=0 and vc.vendorid='"+ vendorId + "'";
		return template.queryForObject(sql, new Object[] {},
				new BeanPropertyRowMapper<VendorContact>(VendorContact.class));
		}
}
