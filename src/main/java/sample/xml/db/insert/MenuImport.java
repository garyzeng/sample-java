package sample.xml.db.insert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MenuImport {
	
	private static List<String> globalGroups = new ArrayList<String>();
	
	private static List<Menu> menus = new ArrayList<Menu>();
	
	private static List<String> envs = new ArrayList<String>();
	
	private static final String MENU_FILE = "menu.xml";
	
	@SuppressWarnings("serial")
	private static final Map<String, String[]> connParams = new HashMap<String, String[]>() {
		{
			put("dev", new String[]{"jdbc:sqlserver://host:20482;DatabaseName=dbName", "userName", "password"});
			put("ft", new String[]{"jdbc:sqlserver://host:20482;DatabaseName=dbName", "userName", "password"});
			put("itg", new String[]{"jdbc:sqlserver://host:20482;DatabaseName=dbName", "userName", "password"});
			put("pro", new String[]{"jdbc:sqlserver://host:20482;DatabaseName=dbName", "userName", "password"});
			put("aws", new String[]{"jdbc:postgresql://host:12080/dbName", "userName", "password"});
		}
	};

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		parseXML();
		insertIntoDB();
	}
	
	private static void insertIntoDB() {
		
		if(envs.isEmpty()) {
			throw new IllegalArgumentException("env is not defined");
		}
		if(menus.isEmpty()) {
			System.out.println("no menu is defined");
			return;
		}
		
		for(String env : envs) {
			Connection conn = null;
			Statement menuStmt = null, mpStmt = null, groupStmt = null;
			PreparedStatement pstmt = null, mppStmt = null;
			
			try{
				if(env.equalsIgnoreCase("aws")) {
					Class.forName("org.postgresql.Driver");
				} else {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				}
				
				conn = DriverManager.getConnection(connParams.get(env)[0], connParams.get(env)[1], connParams.get(env)[2]);
				conn.setAutoCommit(false);
				
				menuStmt = conn.createStatement();
				menuStmt.execute("delete from qtca_menu");
				mpStmt = conn.createStatement();
				mpStmt.execute("delete from qtca_menu_group");
				
				pstmt = conn.prepareStatement("insert into qtca_menu(uuid, name, url, enabled, display, icon, description, parent_uuid, position) "
						+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
				Set<String> groups = new HashSet<String>();
				for(Menu menu : menus) {
					groups.addAll(menu.getGroups());
					pstmt.setString(1, menu.getUuid());
					pstmt.setString(2, menu.getName());
					pstmt.setString(3, menu.getUrl());
					pstmt.setInt(4, menu.isEnabled() ? 1 : 0);
					pstmt.setInt(5, menu.isDisplay() ? 1 : 0);
					pstmt.setString(6, menu.getIcon());
					pstmt.setString(7, menu.getDescription());
					pstmt.setString(8, menu.getParentUuid());
					pstmt.setInt(9, menu.getPosition());
					pstmt.addBatch();
				}
				pstmt.executeBatch();
				
				
				if(!groups.isEmpty()) {
					groupStmt = conn.createStatement();
					String sql = "select uuid, name from qtca_group where name like ";
					for(String group : groups) {
						sql = sql + "'" + group + "%' or name like ";
					}
					sql = sql.substring(0, sql.lastIndexOf("or") - 1);
					
					ResultSet groupSet = groupStmt.executeQuery(sql);
					Map<String, String> map = new HashMap<String, String>();
					while(groupSet.next()) {
						map.put(groupSet.getString("name"), groupSet.getString("uuid"));
					}
					
					mppStmt = conn.prepareStatement("insert into qtca_menu_group(uuid, group_uuid, menu_uuid) values(?, ?, ?)");
					for(Menu menu : menus) {
						for(String group : menu.getGroups()) {
							for(String gm : map.keySet()) {
								if(gm.toLowerCase().startsWith(group.toLowerCase())) {
									mppStmt.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
									mppStmt.setString(2, map.get(gm));
									mppStmt.setString(3, menu.getUuid());
									mppStmt.addBatch();
								}
							}
						}
					}
					mppStmt.executeBatch();
				} else {
					System.out.println("no groups is defined");
				}
				
				conn.commit();

			} catch(Exception e) {
				try {
					if(conn != null) {
						conn.rollback();
						System.out.println("-------------rollback----------------");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				try {
					if(menuStmt != null) {
						menuStmt.close();
					}
					if(mpStmt != null) {
						mpStmt.close();
					}
					if(pstmt != null) {
						pstmt.close();
					}
					if(groupStmt != null) {
						groupStmt.close();
					}
					if(mppStmt != null) {
						mppStmt.close();
					}
					if(conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void parseXML() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(MenuImport.class.getClassLoader().getResourceAsStream(MENU_FILE));
		NodeList nodeList = doc.getElementsByTagName("menus");
		
		populateMenus("0", nodeList);
		
		
	}
	
	private static void populateMenus(String parentUuid, NodeList nodeList) {
		for(int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String puuid = parentUuid;
			if(node.getNodeName().equalsIgnoreCase("menu")) {
				NamedNodeMap attr = node.getAttributes();
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				puuid = uuid;
				Menu menu = new Menu();
				menu.setDescription(attr.getNamedItem("description").getNodeValue().trim());
				menu.setDisplay(attr.getNamedItem("display").getNodeValue().trim().equalsIgnoreCase("true") ? true : false);
				menu.setEnabled(attr.getNamedItem("enabled").getNodeValue().trim().equalsIgnoreCase("true") ? true : false);
				menu.setIcon(attr.getNamedItem("icon").getNodeValue().trim());
				menu.setName(attr.getNamedItem("name").getNodeValue().trim());
				menu.setPosition(i * 100);
				menu.setUrl(attr.getNamedItem("url").getNodeValue().trim());
				menu.setParentUuid(parentUuid);
				menu.setUuid(uuid);
				
				String[] groups = attr.getNamedItem("groups") == null ? new String[0] : attr.getNamedItem("groups").getNodeValue().trim().split(",");
				List<String> gpsl = new ArrayList<String>();
				menu.setGroups(gpsl);
				for(String group : groups) {
					if(!group.trim().equals("")) {
						gpsl.add(group.trim());
					}
				}
				gpsl.addAll(globalGroups);
				
				menus.add(menu);
			} else if(node.getNodeName().equalsIgnoreCase("menus")) {
				NamedNodeMap attr = node.getAttributes();
				String[] groups = attr.getNamedItem("groups") == null ? new String[0] : attr.getNamedItem("groups").getNodeValue().trim().split(",");
				for(String group : groups) {
					if(!group.trim().equals("")) {
						globalGroups.add(group.trim());
					}
				}
				
				String[] envs = attr.getNamedItem("envs") == null ? new String[0] : attr.getNamedItem("envs").getNodeValue().trim().split(",");
				for(String env : envs) {
					MenuImport.envs.add(env.trim().toLowerCase());
				}
			}
			
			populateMenus(puuid, node.getChildNodes());
		}
	}

}
