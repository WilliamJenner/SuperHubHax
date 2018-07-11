import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Main {

	public static void main(String[] args) {
		
		// Creates a scanner so the user inputs their password, prevents my password in source code
		Scanner scan = new Scanner(System.in);
		System.out.println("Please input the password");
		String userPassword = scan.nextLine();
		scan.close();
		
		try {
		
		// connects to the superhub, and then creates a document of that connected page
		Connection hubConnection = Jsoup.connect("http://192.168.0.1/cgi-bin/VmLoginCgi");
		Connection.Response res = hubConnection.execute();
		Document loginPage = hubConnection.get();
		
		// extracts the name of the password field. For some reason, the name changes each time the page is connected/refreshed. 
		Element passwordField = loginPage.getElementById("password");
		Attributes passwordFieldAtt = passwordField.attributes();
		String passwordFieldName = passwordFieldAtt.get("name");
		System.out.println(passwordFieldName);
		
		// creates a logged in document.
		Document loggedIn = hubConnection.data(passwordFieldName, userPassword).post();
		//System.out.println(loggedIn);
		
		// gets the cookies allowing navigation around the hub.
		Map<String, String> loginCookies = res.cookies();
		
		// navigates to the MAC address filtering page
		Document macFiltering = Jsoup.connect("http://192.168.0.1/VmRgMacFiltering.html")
				.timeout(0)
				.cookies(loginCookies)
				.userAgent("Mozilla/5.0")
				.get();
		
		System.out.println(macFiltering);
		
		} catch (IOException e) {
			// Honestly I don't understand try/catch
			e.printStackTrace();
		}
				
	}

}
